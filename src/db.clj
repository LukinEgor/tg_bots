(ns db)

(def spec {:dbtype "postgresql"
           :dbname (get (System/getenv) "DB_NAME")
           :host (get (System/getenv) "DB_HOST")
           :user (get (System/getenv) "DB_USER")
           :password (get (System/getenv) "DB_PASS")})
