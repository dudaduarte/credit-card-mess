(ns credit-card-mess.schemas
  (:require [schema.core :as s])
  (:import java.time.LocalDateTime))

(def Categories (s/enum :food :clothes :games :health))
(def PosInt (s/constrained s/Num pos?))

(def Client
  {:id    s/Keyword
   :name  s/Str
   :cpf   PosInt
   :email s/Str})

(def CreditCard
  {:id        s/Keyword
   :number    PosInt
   :cvv       PosInt
   :due-date  LocalDateTime
   :limit     PosInt
   :client-id s/Keyword})

(def Purchase
  {:id             s/Keyword
   :date           LocalDateTime
   :value          PosInt
   :store          s/Str
   :category       Categories
   :credit-card-id s/Keyword})

(def MapPurchases [(s/cond-pre s/Keyword Purchase)])
(def GroupedValues {(s/cond-pre s/Keyword s/Num s/Str) s/Num})
(def GroupedPurchases {(s/cond-pre s/Keyword s/Num s/Str) [MapPurchases]})
(def GroupedByMonthPurchases {s/Num [MapPurchases]})
(def Funcs (s/enum < <= > >= =))
(def Purchases {s/Keyword Purchase})
(def CreditCards {s/Keyword CreditCard})
(def Clients {s/Keyword Client})
(def AnyPredefColls (s/either Clients CreditCards Purchases))
(def AnyPredefCollsAtom (s/->Atomic (s/cond-pre AnyPredefColls {})))

;(s/conditional #(= (:type %) :bird) {:type (s/eq :bird) :chirping s/Bool}
;               #(= (:type %) :fish) {:type (s/eq :fish) :swimming s/Bool}
;               ...
;               :default  {:type (s/eq :animal) :existing s/Bool})