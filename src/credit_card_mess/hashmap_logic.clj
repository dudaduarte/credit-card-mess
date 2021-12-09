(ns credit_card_mess.hashmap-logic
  (:require [java-time :as j]
            [schema.core :as s]
            [credit-card-mess.schemas :as cs]))

(s/defn purchase-category :- s/Keyword
  [purchase :- [cs/Purchase]]
  (-> purchase
      first
      :purchase/category))

(s/defn purchase-value :- s/Num
  [purchase :- [cs/Purchase]]
  (-> purchase
      first
      :purchase/value))

(s/defn purchase-month :- s/Num
  [purchase :- [cs/Purchase]]
  (-> purchase
      first
      :purchase/date
      j/local-date-time
      (j/property :month-of-year)
      j/value))

(s/defn month-invoice :- s/Num
  [grouped-purchases :- cs/GroupedByMonthPurchases
   month :- s/Num]
  (->> month
       grouped-purchases
       (map purchase-value)
       (reduce +)))

(s/defn check-search-conditions
  [param-key :- s/Keyword
   func :- cs/Funcs]
  (if (and
        (not= func =)
        (or (= param-key :purchase/store)
            (= param-key :purchase/category)
            (= param-key :purchase/credit-card-id)
            (= param-key :purchase/id)))
    (throw (ex-info "Wrong parameters. Functions different than = can only be used on key :purchase/value."
                    {:parameter-key param-key
                     :function func}))
    true))

(s/defn search-purchases :- cs/Purchases
  [purchases :- cs/Purchases
   param-key :- s/Keyword
   func :- cs/Funcs
   param-value :- (s/cond-pre cs/Categories s/Str s/Num)]
  {:pre [(check-search-conditions param-key func)]}
  (filter #(func (get-in % [0 param-key]) param-value) purchases))

(s/defn search-purchases-by-store :- cs/Purchases
  [purchases :- cs/Purchases store :- s/Str]
  (search-purchases purchases :purchase/store = store))

(s/defn first-card-id :- s/Uuid
  [credit-cards :- cs/CreditCards]
  (->> credit-cards
       ffirst
       :credit-card/id))

(s/defn first-client-id :- s/Uuid
  [clients :- cs/Clients]
  (->> clients
       ffirst
       :client/id))

(s/defn sum-purchases-values :- s/Num
  [val :- cs/Purchases]
  (reduce + (map purchase-value val)))

(s/defn invoices-by-group :- cs/GroupedValues
  ([pending-elements :- cs/GroupedPurchases]
   (invoices-by-group {} pending-elements))
  ([coll :- cs/GroupedValues
    pending-elements :- cs/GroupedPurchases]
   (if (seq pending-elements)
     (let [key (first (keys pending-elements))
           new-value (sum-purchases-values (first (vals pending-elements)))]
       (recur (assoc coll key new-value) (dissoc pending-elements key)))
     coll)))