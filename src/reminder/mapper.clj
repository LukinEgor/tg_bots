(ns reminder.mapper
  (:require [clojure.java.jdbc :as jdbc]
            [db]
            [honey.sql :as sql]))

(defn insert! [reminder]
  (jdbc/execute!
   db/spec
   (-> {:insert-into [:reminders]
        :values [reminder]}
       (sql/format))
   { :return-keys true }))

(comment
  (def t (System/currentTimeMillis))
  t
  (Long/valueOf t)
  (def time (new java.sql.Timestamp (Long/valueOf t)))
  time

  (jdbc/execute!
   db/spec
   ["INSERT INTO reminders (name, notification_time) VALUES (?, ?)" "name" time]
   { :return-keys true })
  )

;; (def insert!
;;   (jdbc/execute!
;;    db/spec
;;    (-> {:insert-into [:reminders]
;;         :values [{:name name :notification-time notification-time}]}
;;        (sql/format)
;;        )
;;    { :return-keys true }))
