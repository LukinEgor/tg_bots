(defproject . "0.1.0-SNAPSHOT"
  :description "my bots"
  :url "https://github.com/LukinEgor"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [morse "0.4.3"]
                 [com.taoensso/timbre "5.2.1"]
                 [environ "1.2.0"]
                 [reagent "1.1.0"]
                 [cljsjs/react "17.0.2-0"]
                 [cljsjs/react-dom "17.0.2-0"]
                 [org.postgresql/postgresql "42.3.3"]
                 [com.github.seancorfield/honeysql "2.2.861"]
                 [hikari-cp "2.14.0"]
                 [migratus "1.3.6"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [ring/ring-json "0.5.1"]
                 [org.clojure/tools.cli "1.0.206"]
                 [ring-logger "1.1.1"]
                 [ring/ring-defaults "0.3.2"]
                 [cheshire "5.11.0"]
                 [org.clojure/clojurescript "1.10.758"]]
  :plugins [[lein-ring "0.12.5"]
            [lein-exec "0.3.7"]
            [lein-figwheel "0.5.18"]
            [migratus-lein "0.7.3"]
            [com.github.clj-kondo/lein-clj-kondo "0.2.0"]
            [lein-binplus "0.6.6"]]
  :migratus {:store :database
             :migration-dir "migrations"
             :db {:classname "com.postgresql.jdbc.Driver"
                  :subprotocol "postgresql"
                  ;; TODO refactoring
                  :subname (str "//" (get (System/getenv) "DB_HOST") "/" (get (System/getenv) "DB_NAME"))
                  :user (get (System/getenv) "DB_USER")
                  :password (get (System/getenv) "DB_PASS")}}
  :ring {:handler server/app :open-browser? false}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}
   :cli  {:dependencies [[org.clojure/tools.cli "1.0.206"]]
          :plugins [[lein-binplus "0.6.6"]]
          :main cli}})
