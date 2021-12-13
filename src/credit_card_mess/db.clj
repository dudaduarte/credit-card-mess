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

(defn transaction [conn client credit-card]
  (let [tempid "tempid-1"
        client (assoc client :db/id tempid)
        credit-card (assoc credit-card :credit-card/client tempid)]
    (d/transact conn [client credit-card])))

(s/defn save-clients-and-credit-cards!
  [conn clients-and-credit-cards :- [{:client      cs/Client
                                      :credit-card cs/CreditCard}]]
  (map #(transaction conn (:client %) (:credit-card %)) clients-and-credit-cards))

(defn add-ref-purchases [credit-card-id purchases]
  (map #(assoc % :purchase/credit-card [:credit-card/id credit-card-id]) purchases))

(s/defn save-purchases!
  [conn purchases :- [cs/Purchase] credit-card-id :- s/Uuid]
  (->> purchases
       (add-ref-purchases credit-card-id)
       (d/transact conn)))

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

(defn purchases-by-client [db]
  (d/q '[:find (pull ?client [*]) (count ?purchase)
         :keys client purchases
         :where [?purchase :purchase/credit-card ?credit-card]
         [?credit-card :credit-card/client ?client]] db))

;(defn client-who-bought-the-most [db]
;  (->> db
;       purchases-by-client
;       (apply max-key :purchases)))

(defn client-who-bought-the-most [db]
  (d/q '[:find [(max ?count) (pull ?client [*])]
         :where [(q '[:find (count ?purchase) ?client
                      :where [?purchase :purchase/credit-card ?credit-card]
                      [?credit-card :credit-card/client ?client]]
                    $) [[?count ?client]]]] db))

(defn client-who-spent-more [db]
  (d/q '[:find (pull ?client [*]) ?max-value
         :keys client max-value
         :where [(q '[:find (max ?value)
                      :where [_ :purchase/value ?value]]
                    $) [[?max-value]]]
         [?purchase :purchase/value ?value]
         [(= ?value ?max-value)]
         [?purchase :purchase/credit-card ?credit-card]
         [?credit-card :credit-card/client ?client]] db))

(defn clients-who-never-bought [db]
  (d/q '[:find (pull ?client [*])
         :where [?credit-card :credit-card/client ?client]
         (not [_ :purchase/credit-card ?credit-card])] db))