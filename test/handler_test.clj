(ns handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [morse.handlers :as h]
            [honey.sql :as sql]
            [db]
            [handler :refer [app]]))

(def update { :message { :text "/start" } :chat { :id 1 }})
(def update { :message { :text "joy, 8" } :chat { :id 1 }})


(def a (app (-> (mock/request :post "/webhook")
                (mock/json-body update))))

a


(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Hello World"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404))))

  (testing "telegram webhook"
    (let [response (app (mock/request :post "/webhook"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Hello World")))))
