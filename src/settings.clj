(ns settings
  (:require [clojure.core :as c]))

(defn get [env]
  (c/get (System/getenv) env))
