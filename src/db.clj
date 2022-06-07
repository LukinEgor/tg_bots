(ns db
  (:require [environ.core :refer [env]]))

(def spec {:dbtype "postgresql"
           :dbname (env :db-name)
           :host (env :db-host)
           :user (env :db-user)
           :password (env :db-password)})
