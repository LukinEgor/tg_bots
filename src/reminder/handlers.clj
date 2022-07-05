(ns reminder.handlers
  (:require [settings :as s]
            [clojure.java.jdbc :as jdbc]
            [honey.sql :as sql]
            [morse.api :as api]
            [db]))

(def pattern #"(.+) (.+) ([0-9]+)")

(defn- reply-markup-options [url]
  {:reply_markup
   {
    :keyboard
    [
     [{
       :text "Set Reminder"
       :web_app { :url url }
       }]
     ]
    }})

(defn start
  ([_] (start))
  ([]
   (let [token (s/get "TG_TOKEN")
         chat-id (s/get "CHAT_ID")
         reply-markup (reply-markup-options (str (s/get "HOST") "/index.html"))]
     (api/send-text token chat-id reply-markup "Reminder bot is activated"))))

(defn stop [_]
  (let [token (s/get "TG_TOKEN")
        chat-id (s/get "CHAT_ID")
        reply-markup {:reply_markup {:keyboard []}}]
  (api/send-text token chat-id reply-markup "Reminder bot is deactivated")))

(defn- on-add-tg-msg [name ts]
  (str
   name
   ", "
   (.format
    (java.text.SimpleDateFormat. "dd.MM.yyyy hh:mm")
    (new java.util.Date (Long/valueOf ts)))))

(defn add [{{data :data} :web_app_data}]
  (let [token (s/get "TG_TOKEN")
        chat-id (s/get "CHAT_ID")]
    (if-some [[_ _ name ts]
              (re-matches pattern data)]
      (do
        (jdbc/execute!
         db/spec
         (-> {:insert-into [:reminders]
              :values [{:name name
                        :state "new"
                        :notification-time (new java.sql.Timestamp (Long/valueOf ts))}]}
             (sql/format))
         { :return-keys true })
        (api/send-text token chat-id (on-add-tg-msg name ts)))
      (api/send-text token chat-id "Not valid format" ))))

(comment
  (def ts (System/currentTimeMillis))
  (jdbc/execute!
   db/spec
   (-> {:insert-into [:reminders]
        :values [{:name name
                  :state "new"
                  :notification-time (new java.sql.Timestamp (Long/valueOf ts))}]}
       (sql/format)))
  )
