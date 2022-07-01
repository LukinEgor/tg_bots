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
