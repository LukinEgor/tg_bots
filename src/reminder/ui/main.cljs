(ns reminder.ui.main
  (:require ["luxon" :refer [DateTime]]
            ["@mui/material" :refer (TextField)]
            ["@mui/x-date-pickers/AdapterLuxon" :refer [AdapterLuxon]]
            ["@mui/x-date-pickers/StaticDateTimePicker" :refer [StaticDateTimePicker]]
            ["@mui/x-date-pickers/LocalizationProvider" :refer [LocalizationProvider]]
            [reagent.core :as r]
            [reagent.dom :as rdom]))

;; FIXME How to detect?
(defn inside-tg? [] true)
  ;; (let [web-app  (.. js/window -Telegram -WebApp)]
  ;;   (if (nil? web-app) false true)))

(defn send-tg-data [text]
  (let [web-app  (.. js/window -Telegram -WebApp)]
    (.sendData web-app text)))

(defn current-date []
  (-> (.now DateTime)
      (.toLocaleString)))

(defonce state (r/atom {:date (current-date) :name "default name"}))

(defn notification-command []
  (let [unix-time (.toUnixInteger (:date @state))]
    (str "/addreminder " unix-time " " (:name @state))))

(defn handle-change-date [changedDate]
  (swap! state assoc :date changedDate))

(defn handle-change-name [event]
  (let [name (.. event -target -value) ]
    (swap! state assoc :name name)))

(defn handle-accept-date []
  (if inside-tg?
    (-> (notification-command)
        (send-tg-data))
    (js/alert (notification-command))))

(defn component []
  [:div
   [:> TextField {:value (:name @state)
                  :onChange handle-change-name
                  :style { :width "100%" }}]
   [:> LocalizationProvider {:dateAdapter AdapterLuxon}
    [:> StaticDateTimePicker
     {:displayStaticWrapperAs "mobile"
      :openTo "day"
      :ampm false
      :renderInput (fn [params] [:> TextField { :params params }])
      :onChange handle-change-date
      :onAccept handle-accept-date
      :value (:date @state)}]]])

(defn render []
  (rdom/render
   [component]
   (js/document.getElementById "root")))

(render)
