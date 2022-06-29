(ns handlers
  (:require [settings :as s]
            [clojure.java.jdbc :as jdbc]
            [honey.sql :as sql]
            [db]
            [morse.api :as api]))

(defn chat-id [{{id :id} :chat}]
  (api/send-text (s/get "TG_TOKEN") id (str "Chat ID: " id)))

(defn default-message [{{id :id} :chat :as message}]
  (api/send-text (s/get "TG_TOKEN") (s/get "CHAT_ID") message))

(comment
  (def url "https://jarvis.49.12.186.131.nip.io/index.html")

  (api/set-webhook token host)
  (api/send-text (s/get "TG_TOKEN") (s/get "CHAT_ID") "how are you?")

  (def options
    {
     :reply_markup
     {
      :inline_keyboard
      [
       [{
         :text "test"
         :web_app { :url url }
        }]
       ]
      }
     })

  (def options
    {
     :reply_markup
     {
      :keyboard
      [
       [{
        :text "test"
        :web_app { :url url }
        }]
       ]
      }
     })

  (api/send-text token chatid options "how are you?" ))
