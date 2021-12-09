(ns credit_card_mess.models
  (:require [credit-card-mess.schemas :as cs]
            [schema.core :as s])
  (:import java.util.Date))

(defn uuid [] (java.util.UUID/randomUUID))

(s/defn client :- cs/Client
  [name :- s/Str
   cpf :- s/Num
   email :- s/Str]
  {:client/id    (uuid)
   :client/name  name
   :client/cpf   cpf
   :client/email email})

(s/defn credit-card :- cs/CreditCard
  [number :- s/Num
   cvv :- s/Num
   due-date :- Date
   limit :- s/Num
   client-id :- s/Uuid]
  {:credit-card/id        (uuid)
   :credit-card/number    number
   :credit-card/cvv       cvv
   :credit-card/due-date  due-date
   :credit-card/limit     limit
   :credit-card/client-id client-id})

(s/defn purchase :- cs/Purchase
  [date :- Date
   value :- s/Num
   store :- s/Str
   category :- cs/Categories
   credit-card-id :- s/Uuid]
  {:purchase/id             (uuid)
   :purchase/date           date
   :purchase/value          value
   :purchase/store          store
   :purchase/category       category
   :purchase/credit-card-id credit-card-id})

(def schema [{:db/ident       :client/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :client/name
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "Nome do cliente"}
             {:db/ident       :client/cpf
              :db/valueType   :db.type/long
              :db/cardinality :db.cardinality/one
              :db/doc         "CPF do cliente"}
             {:db/ident       :client/email
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "Email do cliente"}


             {:db/ident       :credit-card/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :credit-card/number
              :db/valueType   :db.type/long
              :db/cardinality :db.cardinality/one
              :db/doc         "Numero do cartão de crédito"}
             {:db/ident       :credit-card/cvv
              :db/valueType   :db.type/long
              :db/cardinality :db.cardinality/one
              :db/doc         "CVV do cartão de crédito"}
             {:db/ident       :credit-card/due-date
              :db/valueType   :db.type/instant
              :db/cardinality :db.cardinality/one
              :db/doc         "Data de vencimento do cartão de crédito"}
             {:db/ident       :credit-card/limit
              :db/valueType   :db.type/bigdec
              :db/cardinality :db.cardinality/one
              :db/doc         "Limite do cartão de crédito"}
             {:db/ident       :credit-card/client-id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/doc         "ID do usuário dono do cartão de crédito"}


             {:db/ident       :purchase/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :purchase/date
              :db/valueType   :db.type/instant
              :db/cardinality :db.cardinality/one
              :db/doc         "Data da compra"}
             {:db/ident       :purchase/value
              :db/valueType   :db.type/bigdec
              :db/cardinality :db.cardinality/one
              :db/doc         "Valor da compra"}
             {:db/ident       :purchase/store
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "Loja em que foi realizada a compra"}
             {:db/ident       :purchase/category
              :db/valueType   :db.type/keyword
              :db/cardinality :db.cardinality/one
              :db/doc         "Categoria da loja em que foi realizada a compra"}
             {:db/ident       :purchase/credit-card-id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/doc         "ID do cartão de crédito utilizado na compra"}])