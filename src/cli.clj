(ns cli
  (:require [morse.api :as api])
  (:gen-class))

(def token (get (System/getenv) "TG_TOKEN"))
(def host (get (System/getenv) "HOST"))
(def chatid (get (System/getenv) "CHAT_ID"))

(defn -main [& args]
  (case (first args)
    "set-webhook" (api/set-webhook token (str host "/webhook"))
    "test-notification" (api/send-text token chatid "test")))
