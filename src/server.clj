(ns server
  (:require [compojure.core :refer [POST GET defroutes]]
            [compojure.route :as route]
            [compojure.handler :refer [site]]
            [ring.logger :as logger]
            [taoensso.timbre :as timbre]
            [handlers]
            [telegram-ext :as t]
            [reminder.handlers]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :refer [resource-response]]
            [morse.handlers :as h]))

(h/defhandler tg-routes
  (h/command-fn "chatid" handlers/chat-id)
  (h/command-fn "startreminder" reminder.handlers/start)
  (t/web-app-command-fn "addreminder" reminder.handlers/add)
  (h/command-fn "stopreminder" reminder.handlers/stop)
  (h/message-fn handlers/default-message))

(defroutes app-routes
  (GET "/" _ (resource-response "index.html"))
  (POST "/webhook" {body :body} (tg-routes body))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (->
   (site app-routes)
   (logger/wrap-with-logger {:log-fn (fn [{:keys [level throwable message]}]
                                       (timbre/log level throwable message))})
   (wrap-json-body { :keywords? true })
   (wrap-json-response)))
