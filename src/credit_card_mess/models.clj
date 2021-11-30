(ns credit_card_mess.models
  (:require [credit-card-mess.schemas :as cs]
            [schema.core :as s])
  (:import java.time.LocalDateTime))

(defn uuid [] (.toString (java.util.UUID/randomUUID)))

(s/defn client :- cs/Client
  [name :- s/Str
   cpf :- s/Num
   email :- s/Str]
  {:id    (keyword (uuid))
   :name  name
   :cpf   cpf
   :email email})

(s/defn credit-card :- cs/CreditCard
  [number :- s/Num
   cvv :- s/Num
   due-date :- LocalDateTime
   limit :- s/Num
   client-id :- s/Keyword]
  {:id        (keyword (uuid))
   :number    number
   :cvv       cvv
   :due-date  due-date
   :limit     limit
   :client-id client-id})

(s/defn purchase :- cs/Purchase
  [date :- LocalDateTime
   value :- s/Num
   store :- s/Str
   category :- cs/Categories
   credit-card-id :- s/Keyword]
  {:id             (keyword (uuid))
   :date           date
   :value          value
   :store          store
   :category       category
   :credit-card-id credit-card-id})