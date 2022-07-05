(ns settings
  (:refer-clojure :exclude [get])
  (:require [clojure.core :as c]))

(defn get [env]
  (c/get (System/getenv) env))
