(ns credit_card_mess.hashmap-implementation
  (:use clojure.pprint)
  (:require [java-time :as j]
            [credit_card_mess.hashmap-logic :as c.logic]
            [credit_card_mess.models :as c.models]
            [schema.core :as s]))

(s/set-fn-validation! true)

(println "------------------------------")
(println "HASHMAP IMPLEMENTATION")
(println "------------------------------")
(defn teste-hashmap []
  (let [clients (atom {})
        purchases (atom {})
        credit-cards (atom {})]
    (pprint @clients)
    (pprint @purchases)
    (pprint @credit-cards)
    (c.logic/add-to-collection! clients (c.models/client "Duda" 54638723678 "duda@duda.com"))
    (c.logic/add-to-collection! credit-cards (c.models/credit-card 1234567890 213 (j/plus (j/local-date-time) (j/years 2)) 1000 (c.logic/first-client-id @clients)))
    (c.logic/add-to-collection! purchases (c.models/purchase (j/local-date-time) 50 "Duda Store" :clothes (c.logic/first-card-id @credit-cards)))
    (c.logic/add-to-collection! purchases (c.models/purchase (j/local-date-time) 100 "Davi Store" :games (c.logic/first-card-id @credit-cards)))
    (c.logic/add-to-collection! purchases (c.models/purchase (j/local-date-time) 180 "Pandora Store" :clothes (c.logic/first-card-id @credit-cards)))
    (c.logic/add-to-collection! purchases (c.models/purchase (j/plus (j/local-date-time) (j/months 1)) 76 "Burger King" :food (c.logic/first-card-id @credit-cards)))
    (pprint @clients)
    (pprint @purchases)
    (pprint @credit-cards)

    (println "GROUP BY CATEGORY:")
    (pprint (group-by c.logic/group-by-category @purchases))

    (println "GROUP BY MONTH")
    (pprint (group-by c.logic/group-by-month @purchases))

    (println "MONTH INVOICE")
    (pprint (c.logic/month-invoice (group-by c.logic/group-by-month @purchases) 11))

    (println "SEARCH PURCHASES BY VALUE")
    (pprint (c.logic/search-purchases @purchases :value >= 76))

    (println "SEARCH PURCHASES BY STORE")
    (pprint (c.logic/search-purchases-by-store @purchases "Duda Store"))

    (println "INVOICES BY MONTH:")
    (pprint (c.logic/invoices-by-group (group-by c.logic/group-by-month @purchases)))

    (println "INVOICES BY CATEGORY:")
    (pprint (c.logic/invoices-by-group (group-by c.logic/group-by-category @purchases)))))

(teste-hashmap)