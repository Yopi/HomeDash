(ns homedash.models.migration
  (:require [clojure.java.jdbc :as sql]
            [homedash.models.apartment :as apartment]))

(defn migrated? []
  (-> (sql/query apartment/conn
                 [(str "SELECT COUNT(*) FROM information_schema.tables "
                       "WHERE table_name='apartments'")])
      first :count pos?))

(defn rollback []
  (when (migrated?))
    (print "Dropping table... ") (flush)
    (sql/db-do-commands apartment/conn
      (sql/drop-table-ddl :apartments)))

(defn migrate []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (sql/db-do-commands apartment/conn
                        (sql/create-table-ddl
                         :apartments
                         [[:id :varchar "PRIMARY KEY"]
                         [:revision :integer "DEFAULT 1"]
                         [:address :varchar "NOT NULL"]
                         [:area :varchar "NOT NULL"]
                         [:rent :integer "NOT NULL"]
                         [:type :varchar "NOT NULL"]
                         [:move_in_date :varchar "NOT NULL"]
                         [:surface :integer "NOT NULL"]
                         [:floor :integer "NOT NULL"]
                         [:elevator :boolean "DEFAULT false"]
                         [:queue_days :integer "NOT NULL"]
                         [:interested :integer "NOT NULL"]
                         [:url :varchar "NOT NULL"]
                         [:map_url :varchar "NOT NULL"]
                         [:created_at :timestamp
                          "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]]))
    (println " done")))
