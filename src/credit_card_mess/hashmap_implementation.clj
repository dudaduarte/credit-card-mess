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

(db/delete-connection!)
(def conn (db/open-connection!))
(db/create-schema! conn)

(def client (m/client "Duda" (l/rand-num 11) "duda@duda.com"))
(def credit-card (m/credit-card (l/rand-num 16) (l/rand-num 3) (j/sql-timestamp (j/plus (j/local-date-time) (j/years 2))) 1000M))

(def client2 (m/client "Pandora" (l/rand-num 11) "pandora@duda.com"))
(def credit-card2 (m/credit-card (l/rand-num 16) (l/rand-num 3) (j/sql-timestamp (j/plus (j/local-date-time) (j/years 2))) 1000M))

(def client3 (m/client "Ramona" (l/rand-num 11) "ramona@duda.com"))
(def credit-card3 (m/credit-card (l/rand-num 16) (l/rand-num 3) (j/sql-timestamp (j/plus (j/local-date-time) (j/years 2))) 1000M))

(def client4 (m/client "Romeu" (l/rand-num 11) "romeu@duda.com"))
(def credit-card4 (m/credit-card (l/rand-num 16) (l/rand-num 3) (j/sql-timestamp (j/plus (j/local-date-time) (j/years 2))) 1000M))

(db/save-clients-and-credit-cards! conn [{:client client
                                          :credit-card credit-card}
                                         {:client client2
                                          :credit-card credit-card2}
                                         {:client client3
                                          :credit-card credit-card3}
                                         {:client client4
                                          :credit-card credit-card4}])

(def clients (db/get-clients (db/db conn)))
(pprint clients)

(def credit-cards (db/get-credit-cards (db/db conn)))
(pprint credit-cards)

(let [purchases [(m/purchase (j/sql-timestamp) 20.4M "Duda Store" :clothes)
                 (m/purchase (j/sql-timestamp) 120M "Davi Store" :games)
                 (m/purchase (j/sql-timestamp (j/plus (j/local-date-time) (j/months 1))) 26M "Burger King" :food)]]

  (db/save-purchases! conn purchases (:credit-card/id credit-card2)))

(let [purchases [(m/purchase (j/sql-timestamp) 50M "Duda Store" :clothes)
                 (m/purchase (j/sql-timestamp) 120M "Davi Store" :games)
                 (m/purchase (j/sql-timestamp) 180M "Pandora Store" :clothes)]]

  (db/save-purchases! conn purchases (:credit-card/id credit-card)))

(let [purchases [(m/purchase (j/sql-timestamp) 230M "Duda Store" :clothes)
                 (m/purchase (j/sql-timestamp (j/plus (j/local-date-time) (j/months 1))) 16M "Burger King" :food)]]

  (db/save-purchases! conn purchases (:credit-card/id credit-card3)))

(let [purchases [(m/purchase (j/sql-timestamp) 70.88M "Xiruleibe" :food)
                 (m/purchase (j/sql-timestamp (j/plus (j/local-date-time) (j/months 1))) 76M "Burger King" :food)
                 (m/purchase (j/sql-timestamp (j/plus (j/local-date-time) (j/months 1))) 199.92M "Kebab King" :food)]]

  (db/save-purchases! conn purchases (:credit-card/id credit-card)))

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

(println "PURCHASE REPORT")
(pprint (db/client-who-bought-the-most (db/db conn)))
(pprint (db/client-who-spent-more (db/db conn)))
(pprint (db/clients-who-never-bought (db/db conn)))

(pprint (db/purchases-by-client (db/db conn)))

; TODO: fix tests
