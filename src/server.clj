(ns server
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :refer [site]]
            [handler :as tg-handler]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :refer [resource-response]]
            [morse.handlers :as h]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(h/defhandler tg-routes
  (h/command-fn "chatid" tg-handler/chat-id)
  (h/message-fn tg-handler/default-message))

(defroutes app-routes
  (GET "/" req (resource-response "index.html"))
  (POST "/webhook" {body :body} (tg-routes body))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (->
   (site app-routes)
   (wrap-json-body { :keywords? true })
   (wrap-json-response)))
