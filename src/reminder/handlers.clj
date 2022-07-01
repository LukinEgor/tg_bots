(ns reminder.handlers
  (:require [settings :as s]
            [clojure.java.jdbc :as jdbc]
            [reminder.mapper :as m]
            [honey.sql :as sql]
            [morse.api :as api]))

(def pattern #"(.+) (.+) ([0-9]+)")

(defn- reply-markup-options [url]
  {:reply_markup
   {
    :keyboard
    [
     []
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

(defn add [{{data :data} :web_app_data}]
  (let [token (s/get "TG_TOKEN")
        chat-id (s/get "CHAT_ID")]
    (if-some [[_ _ name datetime]
              (re-matches pattern data)]
      (do
        (m/insert! {:name name
                    :notification-time (new java.sql.Timestamp (Long/valueOf datetime))})
        (api/send-text token chat-id "Reminder is added" ))
      (api/send-text token chat-id "Not valid format" ))))

(comment
  (def ts "1656552240")
  (m/insert! {:name "test"
              :notification-time (new java.sql.Timestamp (* (Long/valueOf ts) 1)) })
  )
