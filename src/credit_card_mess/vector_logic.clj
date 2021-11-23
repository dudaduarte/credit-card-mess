(ns credit_card_mess.vector-logic
  (:require [java-time :as j]))

(defn add-to-vector! [vec item]
  (swap! vec conj item))

(defn group-by-month-vec [item]
  (-> item
      :date
      (j/property :month-of-year)
      j/value))

(defn month-invoice-vec [purchases month]
  (let [get-value #(get % :value)]
    (->> month
         purchases
         (map get-value)
         (reduce +))))

(defn search-purchases [purchases param-key func param-value]
  (filter #(func param-value (get % param-key)) purchases))

(defn search-purchases-by-store [purchases store]
  (search-purchases purchases :store = store))

(defn first-card-id-vec [credit-cards]
  (->> credit-cards
       first
       :id))

(defn first-client-id-vec [clients]
  (->> clients
       first
       :id))

(defn sum-purchases-values [val]
  (reduce + (map #(get % :value) val)))

(defn invoices-by-group
  ([pending-elements] (invoices-by-group {} pending-elements))
  ([coll pending-elements]
   (if (seq pending-elements)
     (let [key (first (keys pending-elements))
           new-value (sum-purchases-values (first (vals pending-elements)))]
       (recur (assoc coll key new-value) (dissoc pending-elements key)))
     coll)))