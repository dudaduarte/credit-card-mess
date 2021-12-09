(ns credit-card-mess.schemas
  (:require [schema.core :as s])
  (:import java.util.Date))

(def Categories (s/enum :food :clothes :games :health))
(def PosInt (s/constrained s/Num pos?))

(def Client
  {(s/optional-key :db/id) s/Num
   :client/id    s/Uuid
   :client/name  s/Str
   :client/cpf   PosInt
   :client/email s/Str})

(def CreditCard
  {(s/optional-key :db/id) s/Num
   :credit-card/id        s/Uuid
   :credit-card/number    PosInt
   :credit-card/cvv       PosInt
   :credit-card/due-date  Date
   :credit-card/limit     PosInt
   :credit-card/client-id s/Uuid})

(def Purchase
  {(s/optional-key :db/id) s/Num
   :purchase/id             s/Uuid
   :purchase/date           Date
   :purchase/value          PosInt
   :purchase/store          s/Str
   :purchase/category       Categories
   :purchase/credit-card-id s/Uuid})

(def Purchases [[Purchase]])
(def CreditCards [[CreditCard]])
(def Clients [[Client]])
(def GroupedValues {(s/cond-pre s/Keyword s/Num s/Str) s/Num})
(def GroupedPurchases {(s/cond-pre s/Keyword s/Num s/Str) Purchases})
(def GroupedByMonthPurchases {s/Num Purchases})
(def Funcs (s/enum < <= > >= =))
