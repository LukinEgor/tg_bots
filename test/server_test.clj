(ns server-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [settings :as s]
            [cheshire.core :refer [parse-string]]
            [morse.api :as api]
            [server :refer [app]]))

(deftest common-handlers
  (testing "main route"
    (let [response (app (mock/request :get "/index.html"))]
      (is (= (:status response) 200))
      (is (= (type (:body response)) java.io.File))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))

(deftest webhook-handler
  (let [chat-id (rand-int 100000)
        token (str "token_" (rand-int 100000))]
    (with-redefs [api/send-text (fn [& args] args)
                  s/get (fn [arg] (get { "TG_TOKEN" token } arg))]
      (testing "telegram webhook"
        (let [params { :message { :text "/chatid" :chat { :id chat-id } } }
              response (app (-> (mock/request :post "/webhook")
                                (mock/json-body params)))]
          (is (= (:status response) 200))
          (let [[resp-token, resp-chat-id, resp-message] (parse-string (:body response))]
            (is (= resp-token token))
            (is (= resp-chat-id chat-id))
            (is (= resp-message (str "Chat ID: " chat-id)))))))))
