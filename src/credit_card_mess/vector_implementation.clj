(ns credit_card_mess.vector-implementation
  (:use clojure.pprint)
  (:require [java-time :as j]
            [credit_card_mess.vector-logic :as c.logic]
            [credit_card_mess.models :as c.models]
            [schema.core :as s]))

(s/set-fn-validation! true)

(println "------------------------------")
(println "VECTOR IMPLEMENTATION")
(println "------------------------------")

(defn teste-vector []
  (let [clients (atom [])
        purchases (atom [])
        credit-cards (atom [])]
    (pprint @clients)
    (pprint @purchases)
    (pprint @credit-cards)
    (c.logic/add-to-vector! clients (c.models/client "Duda" 54638723678 "duda@duda.com"))
    (c.logic/add-to-vector! credit-cards (c.models/credit-card 1234567890 233 (j/plus (j/local-date-time) (j/years 2)) 1000 (c.logic/first-client-id-vec @clients)))
    (c.logic/add-to-vector! purchases (c.models/purchase (j/local-date-time) 50 "Duda Store" :clothes (c.logic/first-card-id-vec @credit-cards)))
    (c.logic/add-to-vector! purchases (c.models/purchase (j/local-date-time) 100 "Davi Store" :games (c.logic/first-card-id-vec @credit-cards)))
    (c.logic/add-to-vector! purchases (c.models/purchase (j/local-date-time) 180 "Pandora Store" :clothes (c.logic/first-card-id-vec @credit-cards)))
    (c.logic/add-to-vector! purchases (c.models/purchase (j/plus (j/local-date-time) (j/months 1)) 76 "Burger King" :food (c.logic/first-card-id-vec @credit-cards)))
    (pprint @clients)
    (pprint @purchases)
    (pprint @credit-cards)

    (println "GROUP BY:")
    (pprint (group-by :category @purchases))

    (println "MONTHLY INVOICE")
    (pprint (c.logic/month-invoice-vec (group-by c.logic/purchase-month-vec @purchases) 11))

    (println "SEARCH PURCHASES BY VALUE")
    (pprint (c.logic/search-purchases @purchases :value >= 76))

    (println "SEARCH PURCHASES BY STORE")
    (pprint (c.logic/search-purchases-by-store @purchases "Duda Store"))

    (println "INVOICES BY MONTH:")
    (pprint (c.logic/invoices-by-group (group-by c.logic/purchase-month-vec @purchases)))

    (println "INVOICES BY CATEGORY:")
    (pprint (c.logic/invoices-by-group (group-by :category @purchases)))))

(teste-vector)