(ns handlers
  (:require [settings :as s]
            [db]
            [morse.api :as api]))

(defn chat-id [{{id :id} :chat}]
  (api/send-text (s/get "TG_TOKEN") id (str "Chat ID: " id)))

(defn default-message [message]
  (api/send-text (s/get "TG_TOKEN") (s/get "CHAT_ID") message))
