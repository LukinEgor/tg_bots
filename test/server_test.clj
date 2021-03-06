(ns server-test
  (:require [clojure.test :refer [deftest is]]
            [ring.mock.request :as mock]
            [clojure.java.jdbc :as jdbc]
            [settings :as s]
            [cheshire.core :refer [parse-string]]
            [morse.api :as api]
            [server :refer [app]]))

(deftest index-route
  (let [response (app (mock/request :get "/index.html"))]
    (is (= (:status response) 200))
    (is (= (type (:body response)) java.io.File))))

(deftest not-found-route
  (let [response (app (mock/request :get "/invalid"))]
    (is (= (:status response) 404))))

(deftest wehbook-getting-chat-id
  (let [chat-id (rand-int 100000)
        token (str "token_" (rand-int 100000))]
    (with-redefs [api/send-text (fn [& args] args)
                  s/get (fn [arg] (get { "TG_TOKEN" token } arg))]
      (let [params { :message { :text "/chatid" :chat { :id chat-id } } }
            response (app (-> (mock/request :post "/webhook")
                              (mock/json-body params)))]
        (is (= (:status response) 200))
        (let [[resp-token, resp-chat-id, resp-message] (parse-string (:body response))]
          (is (= resp-token token))
          (is (= resp-chat-id chat-id))
          (is (= resp-message (str "Chat ID: " chat-id))))))))

(deftest webhook-start-reminder
  (let [chat-id (rand-int 100000)
        token (str "token_" (rand-int 100000))]
    (with-redefs [api/send-text (fn [& args] args)
                  jdbc/execute! (fn [_ _ _] [1])
                  s/get (fn [arg] (get {"TG_TOKEN" token
                                        "CHAT_ID" chat-id
                                        "HOST" "localhost" } arg))]
      (let [params { :message { :text "/startreminder" :chat { :id chat-id } } }
            response (app (-> (mock/request :post "/webhook")
                              (mock/json-body params)))]
        (is (= (:status response) 200))
        (let [[resp-token, resp-chat-id, _, _] (parse-string (:body response))]
          (is (= resp-token token))
          (is (= resp-chat-id chat-id)))))))

(deftest webhook-add-reminder
  (let [chat-id (rand-int 100000)
        token (str "token_" (rand-int 100000))]
    (with-redefs [api/send-text (fn [& args] args)
                  jdbc/execute! (fn [_ _ _] [1])
                  s/get (fn [arg] (get {"TG_TOKEN" token
                                        "CHAT_ID" chat-id
                                        "HOST" "localhost" } arg))]
      (let [timestamp (System/currentTimeMillis)
            params { :message { :web_app_data { :data (str "/addreminder test " timestamp) } :chat { :id chat-id } } }
            response (app (-> (mock/request :post "/webhook")
                              (mock/json-body params)))]
        (is (= (:status response) 200))
        (let [[resp-token, resp-chat-id, _] (parse-string (:body response))]
          (is (= resp-token token))
          (is (= resp-chat-id chat-id))
          )))))

(deftest webhook-stop-reminder
  (let [chat-id (rand-int 100000)
        token (str "token_" (rand-int 100000))]
    (with-redefs [api/send-text (fn [& args] args)
                  jdbc/execute! (fn [_ _ _] [1])
                  s/get (fn [arg] (get {"TG_TOKEN" token
                                        "CHAT_ID" chat-id
                                        "HOST" "localhost" } arg))]
      (let [params { :message { :text "/stopreminder" :chat { :id chat-id } } }
            response (app (-> (mock/request :post "/webhook")
                              (mock/json-body params)))]
        (is (= (:status response) 200))
        (let [[resp-token, resp-chat-id, _, resp-message] (parse-string (:body response))]
          (is (= resp-token token))
          (is (= resp-chat-id chat-id))
          (is (= resp-message "Reminder bot is deactivated"))
          )))
    ))
