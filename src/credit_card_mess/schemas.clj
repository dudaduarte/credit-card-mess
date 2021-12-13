(ns credit-card-mess.schemas
  (:require [schema.core :as s])
  (:import java.util.Date))

(def Categories (s/enum :food :clothes :games :health))
(def PosInt (s/constrained s/Num pos?))
(def DbItem {:db/id s/Num})

(def Client
  {(s/optional-key :db/id) s/Num
   :client/name            s/Str
   :client/cpf             PosInt
   :client/email           s/Str
   :client/id              s/Uuid})

(def CreditCard
  {(s/optional-key :db/id)              s/Num
   :credit-card/number                  PosInt
   :credit-card/cvv                     PosInt
   :credit-card/due-date                Date
   :credit-card/limit                   PosInt
   :credit-card/id                      s/Uuid
   (s/optional-key :credit-card/client) DbItem})

(def Purchase
  {(s/optional-key :db/id)                s/Num
   :purchase/date                         Date
   :purchase/value                        PosInt
   :purchase/store                        s/Str
   :purchase/category                     Categories
   :purchase/id                           s/Uuid
   (s/optional-key :purchase/credit-card) DbItem})

(def Purchases [[Purchase]])
(def CreditCards [[CreditCard]])
(def Clients [[Client]])
(def GroupedValues {(s/cond-pre s/Keyword s/Num s/Str) s/Num})
(def GroupedPurchases {(s/cond-pre s/Keyword s/Num s/Str) Purchases})
(def GroupedByMonthPurchases {s/Num Purchases})
(def Funcs (s/enum < <= > >= =))
