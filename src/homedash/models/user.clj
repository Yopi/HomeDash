(ns homedash.models.user
  (:require [clojure.java.jdbc :as sql]))

(def conn (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/homedash"))

(defn all []
  (into [] (sql/query conn ["SELECT * FROM users"])))

(defn create [user]
  (sql/insert! conn :users user))
