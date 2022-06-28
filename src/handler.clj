(ns handler
  (:require [settings :as s]
            [clojure.java.jdbc :as jdbc]
            [honey.sql :as sql]
            [db]
            [morse.api :as api]))

(def pattern #"(.+),?\s?([0-9]+)?,?\s?(.+)?")

(defn chat-id [{{id :id} :chat}]
  (api/send-text (s/get "TG_TOKEN") id (str "Chat ID: " id)))

(defn default-message [{{id :id} :chat :as message}]
  (if-some [[_ name intensity context]
            (re-matches pattern (:text message))]
    (jdbc/execute!
     db/spec
     (-> {:insert-into [:emotions]
          :values [{:name name :intensity (Integer/parseInt intensity) :context context}]}
         (sql/format)
         )
     { :return-keys true })
    (api/send-text (s/get "TG_TOKEN") (s/get "CHAT_ID") "not valid data")))

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

  (api/send-text token chatid options "how are you?" )
  )
