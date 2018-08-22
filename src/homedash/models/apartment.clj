(ns homedash.models.apartment
  (:require [clojure.java.jdbc :as sql]))

(def conn (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/homedash"))

(defn all []
  (into [] (sql/query conn ["SELECT * FROM apartments order by move_in_date desc"])))

(defn create [apartment]
  (sql/insert! conn :apartments apartment))

(defn update-or-insert!
  "Updates columns or inserts a new row in the specified table"
  [row where-clause]
  (sql/with-db-transaction [t-con conn]
    (let [result (sql/update! t-con :apartments row where-clause)]
      (if (zero? (first result))
        (sql/insert! t-con :apartments row)
        result))))
