(ns reminder.service
  (:require [clojure.java.jdbc :as jdbc]
            [morse.api :as api]
            [settings :as s]
            [db]
            [honey.sql :as sql]))

(defn batch-update-sql [res]
  (-> (map
       (fn [[id ok]]
         [(if (= ok true) "completed" "failed") id]) res)
      (conj "UPDATE reminders SET state = ? where id = ?")))

(defn save-state [res]
  (jdbc/execute! db/spec (batch-update-sql res) { :multi? true }))

(defn send [reminders]
  (let [token (s/get "TG_TOKEN")
        chat-id (s/get "CHAT_ID")]
  (pmap
   (fn [{ id :id name :name }]
     (let [{ ok :ok } (api/send-text token chat-id name)]
       [id ok])) reminders)))

(defn select-active [ts]
  (jdbc/query
   db/spec
  (-> {:select :*
       :from :reminders
       :where [:and
               [:in :state ["new" "failed"]]
               [:< :notification-time ts]]}
      (sql/format))))

(defn notify [ts]
  (let [reminders (select-active ts)]
    (if (seq reminders)
      (->
       (send reminders)
       (save-state)))))

(comment
  (def ts (new java.sql.Timestamp (System/currentTimeMillis)))

  (select-active-reminders ts)
  (notify ts)

  (def sql ["UPDATE reminders SET state = ? where id = ?" ["completed" 33] ["completed" 34] ["completed" 35]])
  (jdbc/execute! db/spec sql { :multi? true })
  )
