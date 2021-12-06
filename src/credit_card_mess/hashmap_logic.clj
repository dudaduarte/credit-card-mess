(ns credit_card_mess.hashmap-logic
  (:require [java-time :as j]
            [schema.core :as s]
            [credit-card-mess.schemas :as cs]))

(s/defn add-to-collection! :- cs/AnyPredefColls
  [coll :- cs/AnyPredefCollsAtom
   item :- (s/either cs/Purchase cs/CreditCard cs/Client)]
  (swap! coll assoc (:id item) item))

(s/defn purchase-category :- s/Keyword
  [[_ purchase] :- cs/MapPurchases]
  (:category purchase))

(s/defn purchase-value :- s/Num
  [[_ purchase] :- cs/MapPurchases]
  (:value purchase))

(s/defn purchase-month :- s/Num
  [[_ purchase] :- cs/MapPurchases]
  (-> purchase
      :date
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
        (or (= param-key :store)
            (= param-key :category)
            (= param-key :credit-card-id)
            (= param-key :id)))
    (throw (ex-info "Wrong parameters. Functions different than = can only be used on keys :value or :date."
                    {:parameter-key param-key
                     :function func}))
    true))

(s/defn search-purchases :- [cs/MapPurchases]
  [purchases :- cs/Purchases
   param-key :- s/Keyword
   func :- cs/Funcs
   param-value :- (s/cond-pre cs/Categories s/Str s/Num)]
  {:pre [(check-search-conditions param-key func)]}
  (filter #(func (get-in % [1 param-key]) param-value) purchases))

(s/defn search-purchases-by-store :- [cs/MapPurchases]
  [purchases :- cs/Purchases store :- s/Str]
  (search-purchases purchases :store = store))

(s/defn first-card-id :- s/Keyword
  [credit-cards :- cs/CreditCards]
  (->> credit-cards
       keys
       first))

(s/defn first-client-id :- s/Keyword
  [clients :- cs/Clients]
  (->> clients
       keys
       first))

(s/defn sum-purchases-values :- s/Num
  [val :- [cs/MapPurchases]]
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