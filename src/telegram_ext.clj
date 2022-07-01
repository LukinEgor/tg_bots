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

(defn web-app-command?
  "Checks if message is a command with a name.
  /stars and /st are considered different."
  [update name]
  (some-> update
          :message
          :web_app_data
          :data
          (clojure.string/split #"\s+")
          (first)
          (clojure.string/split #"@")
          (first)
          (= (str "/" name))))

(defn web-app-command-fn
  "Generate web-app command handler from an update function"
  [name handler]
  (fn [update]
    (if (web-app-command? update name)
      (handler (:message update)))))

(comment
  (def token (get (System/getenv) "TG_TOKEN"))
  (def commands [{:command "/test" :description "test"}])
  (set-my-commands token commands)
  (web-app-command? { :message { :web_app_data { :data "/test aa aa" } } } "test")
  )
