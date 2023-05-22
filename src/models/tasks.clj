(ns models.tasks
  (:require [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]))

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "data/database.db"})

(defn init
  []
  (jdbc/execute! db "CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, body TEXT, status TEXT)"))

(defrecord Task [id name body status])

(defn- db-create
  [name body status]
  (jdbc/execute! db (sql/format {:insert-into :tasks :columns [:name :body :status] :values [[name body status]]}) {:returning [:id]}))

(defn create
  [name body status]
  (->Task
   (get :id (db-create name body status))
   name
   body
   status))

(defn db-get-by-id
  [id]
  (first (jdbc/query db (sql/format {:select [:*] :from [:tasks] :where [:= :id id]}))))

(defn get-by-id
  [id]
  (let [t (db-get-by-id id)]
    (->Task (get t :id) (get t :name) (get t :body) (get t :status))))
