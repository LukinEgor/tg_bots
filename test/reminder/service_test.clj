(ns reminder.service-test
  (:require [reminder.service :refer [notify]]
            [morse.api :as api]
            [clojure.java.jdbc :as jdbc]
            [clojure.test :refer [is deftest]]))

(def ts 1657006086564)
(def reminders
  [{ :name "test" :state "new" :notification-time (new java.sql.Timestamp ts) }])

(deftest exec-notify-with-active-reminders
  (with-redefs [jdbc/query (fn [_ _] reminders)
                jdbc/execute! (fn [_ _ _] [1])
                api/send-text (fn [_ _ _] [{ :ok true }])]
    (is (= (notify (new java.sql.Timestamp (+ ts 1))) [1]))))

(deftest exec-notify-without-active-reminders
  (with-redefs [jdbc/query (fn [_ _] [])]
    (is (= (notify (new java.sql.Timestamp (+ ts 1))) nil))))
