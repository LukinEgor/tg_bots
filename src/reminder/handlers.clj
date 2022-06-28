(ns reminder.handlers
  (:require [settings :as s]
            [morse.api :as api]))

(def token (s/get "TG_TOKEN"))
(def chat-id (s/get "CHAT_ID"))
(def host (s/get "HOST"))

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

(defn start [_]
  (let [token (s/get "TG_TOKEN")
        chat-id (s/get "CHAT_ID")
        reply-markup (reply-markup-options (str (s/get "HOST") "/index.html"))]
  (api/send-text token chat-id reply-markup "Reminder bot is activated")))

(defn stop [_]
  (let [token (s/get "TG_TOKEN")
        chat-id (s/get "CHAT_ID")
        reply-markup {:reply_markup {:keyboard []}}]
  (api/send-text token chat-id reply-markup "Reminder bot is deactivated")))

(defn add [_]
  (let [token (s/get "TG_TOKEN")
        chat-id (s/get "CHAT_ID")]
  (api/send-text token chat-id "Reminder is added")))
