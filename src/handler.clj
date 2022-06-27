(ns handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [honey.sql :as sql]
            [db]
            [clojure.string :as str]
            [clojure.java.jdbc :as jdbc]
            [environ.core :refer [env]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [morse.api :as api]
            [morse.handlers :as h]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(def token (get (System/getenv) "TG_TOKEN"))
(def host (get (System/getenv) "HOST"))
(def chatid (get (System/getenv) "CHAT_ID"))

(def pattern #"(.+),?\s?([0-9]+)?,?\s?(.+)?")

(comment
  (re-matches pattern "test")
  (re-matches pattern "test, 5")
  (re-matches pattern "test, 5, forest")
  (def url "https://d8fb-92-54-207-203.ngrok.io")

  (api/set-webhook token host)
  (api/send-text token chatid "how are you?")

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

(h/defhandler handler
  (h/command-fn "chatid"
   (fn [{{id :id} :chat}]
     (api/send-text token id (str "Chat ID: " id))))

  ;; (h/command-fn "addnotification"
  ;;  (fn [{{id :id} :chat}]
  ;;    (api/send-text token id (str "Chat ID: " id))))

  ;; (h/command-fn "setnotification"
  ;;  (fn [{{id :id} :chat}]
  ;;    (api/send-text token id (str "Chat ID: " id))))

  (h/message-fn
    (fn [{{id :id} :chat :as message}]
      (if-some [[_ name intensity context]
                (re-matches pattern (:text message))]
        (jdbc/execute!
         db/spec
         (-> {:insert-into [:emotions]
              :values [{:name name :intensity (Integer/parseInt intensity) :context context}]}
             (sql/format)
             )
         { :return-keys true })
        (api/send-text token chatid "not valid data")))))

(defroutes app-routes
  (POST "/webhook" {body :body} (handler body))
  (route/files "/")
  (route/not-found "Not Found"))

(def app
  (->
   (handler/site app-routes)
   (wrap-json-body { :keywords? true })
   (wrap-json-response)))
