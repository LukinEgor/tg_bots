(ns telegram-ext
  (:require [morse.api :as api]
            [clj-http.client :as http]))

(defn set-my-commands
  "setMyCommands"
  [token commands]
  (let [url   (str api/base-url token "/setMyCommands")
        body {:commands commands}]
    (http/post url {:content-type :json
                    :as           :json
                    :form-params  body})))

(comment
  (def token (get (System/getenv) "TG_TOKEN"))
  (def commands [{:command "/test" :description "test"}])
  (def commands [{:command "/addnotification" :description "Add Notification"}])
  (set-my-commands token commands)
  )
