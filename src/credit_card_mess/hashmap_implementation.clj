(ns credit_card_mess.hashmap-implementation
  (:use clojure.pprint)
  (:require [java-time :as j]
            [credit_card_mess.hashmap-logic :as l]
            [credit_card_mess.models :as m]
            [credit-card-mess.db :as db]
            [schema.core :as s]))

(s/set-fn-validation! true)

(println "------------------------------")
(println "HASHMAP IMPLEMENTATION")
(println "------------------------------")

(def conn (db/open-connection!))
(db/create-schema! conn)

;(db/delete-connection!)

(db/save-clients! conn [(m/client "Duda" 54638723678 "duda@duda.com")])
(def clients (db/get-clients (db/db conn)))
(pprint clients)

(db/save-credit-cards! conn [(m/credit-card 1234567890 213 (j/sql-timestamp (j/plus (j/local-date-time) (j/years 2))) 1000M (l/first-client-id clients))])
(def credit-cards (db/get-credit-cards (db/db conn)))
(pprint credit-cards)

(let [purchases [(m/purchase (j/sql-timestamp (j/local-date-time)) 50M "Duda Store" :clothes (l/first-card-id credit-cards))
                 (m/purchase (j/sql-timestamp (j/local-date-time)) 100M "Davi Store" :games (l/first-card-id credit-cards))
                 (m/purchase (j/sql-timestamp (j/local-date-time)) 180M "Pandora Store" :clothes (l/first-card-id credit-cards))
                 (m/purchase (j/sql-timestamp (j/plus (j/local-date-time) (j/months 1))) 76M "Burger King" :food (l/first-card-id credit-cards))]]

  (pprint (db/save-purchases! conn purchases)))
(def purchases (db/get-purchases (db/db conn)))
(pprint purchases)

(println "GROUP BY CATEGORY:")
(pprint (group-by l/purchase-category purchases))

(println "GROUP BY MONTH")
(pprint (group-by l/purchase-month purchases))

(println "MONTH INVOICE")
(pprint (l/month-invoice (group-by l/purchase-month purchases) 12))

(println "SEARCH PURCHASES BY VALUE")
(pprint (l/search-purchases purchases :purchase/value >= 150))

(println "SEARCH PURCHASES BY STORE")
(pprint (l/search-purchases-by-store purchases "Duda Store"))

(println "INVOICES BY MONTH:")
(pprint (l/invoices-by-group (group-by l/purchase-month purchases)))

(println "INVOICES BY CATEGORY:")
(pprint (l/invoices-by-group (group-by l/purchase-category purchases)))

(println "GET PURCHASES FROM DB BY VALUE")
(pprint (db/get-purchases-by (db/db conn) :purchase/value 100M))

(println "GET PURCHASES FROM DB BY STORE")
(pprint (db/get-purchases-by (db/db conn) :purchase/store "Duda Store"))

(println "GET PURCHASES FROM DB BY CATEGORY")
(pprint (db/get-purchases-by (db/db conn) :purchase/category :food))

; TODO: fix tests
