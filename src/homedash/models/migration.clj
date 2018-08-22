(ns homedash.models.migration
  (:require [clojure.java.jdbc :as sql]
            [homedash.models.apartment :as apartment]
            [homedash.models.user :as user]))

(defn migrated_apartments? []
  (-> (sql/query apartment/conn
                 [(str "SELECT COUNT(*) FROM information_schema.tables "
                       "WHERE table_name='apartments'")])
      first :count pos?))

(defn migrated_users? []
  (-> (sql/query user/conn
                 [(str "SELECT COUNT(*) FROM information_schema.tables "
                       "WHERE table_name='users'")])
      first :count pos?))

(defn migrated? []
  (and (migrated_apartments?) (migrated_users?)))

(defn rollback []
  (when (migrated?))
    (print "Dropping tables... ") (flush)
    (sql/db-do-commands apartment/conn
      (sql/drop-table-ddl :apartments))
    (sql/db-do-commands user/conn
      (sql/drop-table-ddl :users)))

(defn migrate []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (sql/db-do-commands apartment/conn
                        (sql/create-table-ddl
                         :apartments
                         [[:id :varchar "PRIMARY KEY"]
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
                         [:created_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]]))
    (sql/db-do-commands user/conn
                        (sql/create-table-ddl
                         :users
                         [[:id :serial "PRIMARY KEY"]
                         [:name :varchar "NOT NULL"]
                         [:email :varchar "NOT NULL"]
                         [:created_at :timestamp "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]]))
    (println " done")))
