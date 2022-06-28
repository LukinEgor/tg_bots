(ns notifications.handler)

(def host (get (System/getenv) "HOST"))

(def web-app-url (str host "/index.html"))

(defn reply-markup-options
  {:reply_markup
   {
    :keyboard
    [
     [{
       :text "Open Datepicker"
       :web_app { :url url }
       }]
     ]
    }})

(defn set-notification []
  (api/send-text token chatid reply-markup-options "Setup notification"))

(defn add-notification [command]
  )
