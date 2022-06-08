(ns cli
  (:require [morse.api :as api])
  (:gen-class))

(def token (get (System/getenv) "TG_TOKEN"))
(def host (get (System/getenv) "HOST"))
(def chatid (get (System/getenv) "CHAT_ID"))

(defn -main [& args]
  (case (first args)
    "set-webhook" (api/set-webhook token host)
    "notify" (api/send-text token chatid "how are you?")))

(defn set-webhook []
  (api/set-webhook token host))

(defn notify []
  (api/send-text token chatid "how are you?"))
