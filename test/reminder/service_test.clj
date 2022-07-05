(ns reminder.service-test
  (:require [reminder.service :refer [notify select-active]]
            [morse.api :as api]
            [clojure.java.jdbc :as jdbc]
            [clojure.test :refer :all]))

(def ts 1657006086564)
(def reminders
  [{ :name "test" :state "new" :notification-time (new java.sql.Timestamp ts) }])

(deftest check-notifications
  (with-redefs [jdbc/query (fn [_] reminders)
                jdbc/execute! (fn [_] [1])
                api/send-text (fn [_ _ _] [{ :ok true }])]
    (is (= (notify (new java.sql.Timestamp (+ ts 1))) 4))
    ))
