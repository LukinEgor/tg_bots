(ns migrations
  (:require [hikari-cp.core :as hk]
            [migratus.core :as migratus]
            [clojure.java.jdbc :as jdbc]
            [db]
            ))
;; Hikari: https://github.com/tomekw/hikari-cp

(def host (get (System/getenv) "DB_HOST"))
(def db-name (get (System/getenv) "DB_NAME"))
(def subname (str "//" host "/" db-name))
(def config {:store :database
             :migration-dir "migrations"
             :db {:classname "com.postgresql.jdbc.Driver"
                  :subprotocol "postgresql"
                  :subname subname
                  :user (get (System/getenv) "DB_USER")
                  :password (get (System/getenv) "DB_PASS")}})

(comment
  (migratus/create config "create-emotions")
  (migratus/init config)
  (migratus/migrate config)
  (migratus/rollback config)
  (migratus/up config 20111206154000)
  (migratus/down config 20111206154000)
  )
