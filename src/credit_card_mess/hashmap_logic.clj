(ns credit_card_mess.hashmap-logic
  (:require [java-time :as j]))

(defn add-to-collection! [coll item]
  (swap! coll assoc (:id item) item))

(defn group-by-category [[_ item]]
  (:category item))

(defn purchase-value [purchase]
  (get-in purchase [1 :value]))

(defn group-by-month [[_ item]]
  (-> item
      :date
      (j/property :month-of-year)
      j/value))

(defn month-invoice [purchases month]
  (let [get-value #(purchase-value %)]
    (->> month
         purchases
         (map get-value)
         (reduce +))))

(defn search-purchases [purchases param-key func param-value]
  (filter #(func (get-in % [1 param-key]) param-value) purchases))

(defn search-purchases-by-store [purchases store]
  (search-purchases purchases :store = store))

(defn first-card-id [credit-cards]
  (->> credit-cards
       keys
       first))

(defn first-client-id [clients]
  (->> clients
       keys
       first))

(defn sum-purchases-values [val]
  (reduce + (map #(purchase-value %) val)))

(defn invoices-by-group
  ([pending-elements] (invoices-by-group {} pending-elements))
  ([coll pending-elements]
   (if (seq pending-elements)
     (let [key (first (keys pending-elements))
           new-value (sum-purchases-values (first (vals pending-elements)))]
       (recur (assoc coll key new-value) (dissoc pending-elements key)))
     coll)))