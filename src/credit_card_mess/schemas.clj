(ns credit-card-mess.schemas
  (:require [schema.core :as s])
  (:import java.time.LocalDateTime))

(def Categories (s/enum :food :clothes :games :health))

(def Client
  {:id    s/Keyword
   :name  s/Str
   :cpf   s/Num
   :email s/Str})

(def CreditCard
  {:id        s/Keyword
   :number    s/Num
   :cvv       s/Num
   :due-date  LocalDateTime
   :limit     s/Num
   :client-id s/Keyword})

(def Purchase
  {:id             s/Keyword
   :date           LocalDateTime
   :value          s/Num
   :store          s/Str
   :category       Categories
   :credit-card-id s/Keyword})