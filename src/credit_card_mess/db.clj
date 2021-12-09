(ns credit-card-mess.db
  (:require [datomic.api :as d]
            [schema.core :as s]
            [credit_card_mess.models :as models]
            [credit-card-mess.schemas :as cs]))

(def db-uri "datomic:mem://credit-card-mess")

(defn open-connection! []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn delete-connection! []
  (d/delete-database db-uri))

(defn db [conn]
  (d/db conn))

(defn create-schema! [conn]
  (d/transact conn models/schema))

(s/defn save-clients!
        [conn clients :- [cs/Client]]
        (d/transact conn clients))

(s/defn save-credit-cards!
        [conn credit-cards :- [cs/CreditCard]]
        (d/transact conn credit-cards))

(s/defn save-purchases!
        [conn purchases]
        (d/transact conn purchases))

(defn get-clients [db]
  (d/q '[:find (pull ?entity [*])
         :where [?entity :client/id]] db))

(defn get-credit-cards [db]
  (d/q '[:find (pull ?entity [*])
         :where [?entity :credit-card/id]] db))

(defn get-purchases [db]
  (d/q '[:find (pull ?entity [*])
         :where [?entity :purchase/id]] db))

(defn get-purchases-by [db key val]
  (let [query '[:find (pull ?entity [*])
                :in $ ?key ?val
                :where [?entity ?key ?val]]]
    (d/q query db key val)))