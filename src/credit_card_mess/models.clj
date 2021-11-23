(ns credit_card_mess.models)

(defn uuid [] (.toString (java.util.UUID/randomUUID)))

(defn client [name cpf email]
  {:id    (keyword (uuid))
   :name  name
   :cpf   cpf
   :email email})

(defn credit-card [number cvv due-date limit client-id]
  {:id        (keyword (uuid))
   :number    number
   :cvv       cvv
   :due-date  due-date
   :limit     limit
   :client-id client-id})

(defn purchase [date value store category credit-card-id]
  {:id             (keyword (uuid))
   :date           date
   :value          value
   :store          store
   :category       category
   :credit-card-id credit-card-id})