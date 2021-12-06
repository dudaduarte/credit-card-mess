(ns credit-card-mess.hashmap-logic-test
  (:require [clojure.test :refer :all]
            [credit_card_mess.hashmap-logic :refer :all]
            [java-time :as j]
            [schema.core :as s]
            [credit-card-mess.schemas :as cs])
  (:import clojure.lang.ExceptionInfo))

(s/set-fn-validation! true)

(def purchases {:1a
                {:id             :1a,
                 :date           (j/local-date-time 2015 10 12 12 12 12),
                 :value          50,
                 :store          "Duda Store",
                 :category       :clothes,
                 :credit-card-id :5c},
                :2a
                {:id             :2a,
                 :date           (j/local-date-time 2015 10 12 12 12 12),
                 :value          100,
                 :store          "Davi Store",
                 :category       :games,
                 :credit-card-id :5c},
                :3a
                {:id             :3a,
                 :date           (j/local-date-time 2015 10 12 12 12 12),
                 :value          180,
                 :store          "Pandora Store",
                 :category       :clothes,
                 :credit-card-id :5c},
                :4a
                {:id             :4a,
                 :date           (j/plus (j/local-date-time 2015 10 12 12 12 12) (j/months 1)),
                 :value          76,
                 :store          "Burger King",
                 :category       :food,
                 :credit-card-id :5c}})

(def purchases-grouped-by-category {:clothes
                                    [[:1a {:id             :1a,
                                           :date           (j/local-date-time 2015 10 12 12 12 12),
                                           :value          50,
                                           :store          "Duda Store",
                                           :category       :clothes,
                                           :credit-card-id :5c}],
                                     [:3a {:id             :3a,
                                           :date           (j/local-date-time 2015 10 12 12 12 12),
                                           :value          180,
                                           :store          "Pandora Store",
                                           :category       :clothes,
                                           :credit-card-id :5c}]],
                                    :games
                                    [[:2a {:id             :2a,
                                           :date           (j/local-date-time 2015 10 12 12 12 12),
                                           :value          100,
                                           :store          "Davi Store",
                                           :category       :games,
                                           :credit-card-id :5c}]],
                                    :food
                                    [[:4a {:id             :4a,
                                           :date           (j/plus (j/local-date-time 2015 10 12 12 12 12) (j/months 1)),
                                           :value          76,
                                           :store          "Burger King",
                                           :category       :food,
                                           :credit-card-id :5c}]]})

(def purchases-grouped-by-month {10 [[:1a {:id             :1a,
                                           :date           (j/local-date-time 2015 10 12 12 12 12),
                                           :value          50,
                                           :store          "Duda Store",
                                           :category       :clothes,
                                           :credit-card-id :5c}],
                                     [:2a {:id             :2a,
                                           :date           (j/local-date-time 2015 10 12 12 12 12),
                                           :value          100,
                                           :store          "Davi Store",
                                           :category       :games,
                                           :credit-card-id :5c}],
                                     [:3a {:id             :3a,
                                           :date           (j/local-date-time 2015 10 12 12 12 12),
                                           :value          180,
                                           :store          "Pandora Store",
                                           :category       :clothes,
                                           :credit-card-id :5c}]],
                                 11 [[:4a {:id             :4a,
                                           :date           (j/plus (j/local-date-time 2015 10 12 12 12 12) (j/months 1)),
                                           :value          76,
                                           :store          "Burger King",
                                           :category       :food,
                                           :credit-card-id :5c}]]})

(deftest purchase-category-test
  (testing "Success to group by categories - purchase-category as group-by function"
    (is (= purchases-grouped-by-category (group-by purchase-category purchases))))

  (testing "Success returning category"
    (is (= :clothes (purchase-category [:1a (:1a purchases)])))
    (is (= :games (purchase-category [:2a (:2a purchases)])))
    (is (= :clothes (purchase-category [:3a (:3a purchases)])))
    (is (= :food (purchase-category [:4a (:4a purchases)]))))

  (testing "Error returning non keywords categories"
    (is (thrown? ExceptionInfo (purchase-category [:4a {:id             :4a,
                                                        :date           (j/local-date-time 2015 10 12 12 12 12),
                                                        :value          76,
                                                        :store          "Burger King",
                                                        :category       "food",
                                                        :credit-card-id :5c}])))
    (is (thrown? ExceptionInfo (purchase-category [:4a {:id             :4a,
                                                        :date           (j/local-date-time 2015 10 12 12 12 12),
                                                        :value          76,
                                                        :store          "Burger King",
                                                        :category       [:food],
                                                        :credit-card-id :5c}])))
    (is (thrown? ExceptionInfo (purchase-category [:4a {:id             :4a,
                                                        :date           (j/local-date-time 2015 10 12 12 12 12),
                                                        :value          76,
                                                        :store          "Burger King",
                                                        :category       1,
                                                        :credit-card-id :5c}])))
    (is (thrown? ExceptionInfo (purchase-category [:4a {:id             :4a,
                                                        :date           (j/local-date-time 2015 10 12 12 12 12),
                                                        :value          76,
                                                        :store          "Burger King",
                                                        :credit-card-id :5c}])))))

(deftest purchase-value-test
  (testing "Success returning category"
    (is (= 50 (purchase-value [:1a (:1a purchases)])))
    (is (= 100 (purchase-value [:2a (:2a purchases)])))
    (is (= 180 (purchase-value [:3a (:3a purchases)])))
    (is (= 76 (purchase-value [:4a (:4a purchases)]))))

  (testing "Error returning invalid values"
    (is (thrown? ExceptionInfo (purchase-value [:1a {:id             :1a,
                                                     :date           (j/local-date-time 2015 10 12 12 12 12),
                                                     :value          -50,
                                                     :store          "Duda Store",
                                                     :category       :clothes,
                                                     :credit-card-id :5c}])))
    (is (thrown? ExceptionInfo (purchase-value [:1a {:id             :1a,
                                                     :date           (j/local-date-time 2015 10 12 12 12 12),
                                                     :value          0,
                                                     :store          "Duda Store",
                                                     :category       :clothes,
                                                     :credit-card-id :5c}])))
    (is (thrown? ExceptionInfo (purchase-value [:1a {:id             :1a,
                                                     :date           (j/local-date-time 2015 10 12 12 12 12),
                                                     :value          "12",
                                                     :store          "Duda Store",
                                                     :category       :clothes,
                                                     :credit-card-id :5c}])))))

(deftest purchase-month-test
  (testing "Success to group by month - purchase-month as group-by function"
    (is (= purchases-grouped-by-month (group-by purchase-month purchases))))

  (testing "Success returning months from purchases"
    (is (= 10 (purchase-month [:1a (:1a purchases)])))
    (is (= 10 (purchase-month [:2a (:2a purchases)])))
    (is (= 10 (purchase-month [:3a (:3a purchases)])))
    (is (= 11 (purchase-month [:4a (:4a purchases)]))))

  (testing "Error receiving invalid date format"
    (is (thrown? ExceptionInfo (purchase-month [:1a {:id             :2a,
                                                     :date           (java.util.Date.),
                                                     :value          100,
                                                     :store          "Davi Store",
                                                     :category       :games,
                                                     :credit-card-id :5c}])))
    (is (thrown? ExceptionInfo (purchase-month [:1a {:id             :2a,
                                                     :date           "2021-10-12",
                                                     :value          100,
                                                     :store          "Davi Store",
                                                     :category       :games,
                                                     :credit-card-id :5c}])))))

(deftest month-invoice-test
  (testing "Success on getting a month invoice"
    (is (= 330 (month-invoice purchases-grouped-by-month 10)))
    (is (= 76 (month-invoice purchases-grouped-by-month 11))))

  (testing "Error receiving wrong type parameters"
    (is (thrown? ExceptionInfo (month-invoice purchases-grouped-by-category 11)))
    (is (thrown? ExceptionInfo (month-invoice purchases 10)))
    (is (thrown? ExceptionInfo (month-invoice purchases-grouped-by-month "10")))))

(deftest check-search-conditions-test
  (testing "Success on validating parameters"
    (is (= true (check-search-conditions :store =)))
    (is (= true (check-search-conditions :category =)))
    (is (= true (check-search-conditions :credit-card-id =)))
    (is (= true (check-search-conditions :id =))))

  (testing "Error on validation wrong parameters"
    (is (thrown? ExceptionInfo (check-search-conditions :store >)))
    (is (thrown? ExceptionInfo (check-search-conditions :category >=)))
    (is (thrown? ExceptionInfo (check-search-conditions :credit-card-id <)))
    (is (thrown? ExceptionInfo (check-search-conditions :id <=)))))

(deftest search-purchases-test
  (testing "Success on searching valid purchases equal values"
    (is (= [[:1a (:1a purchases)]] (search-purchases purchases :value = 50)))
    (is (= [[:2a (:2a purchases)]] (search-purchases purchases :store = "Davi Store")))
    (is (= [[:1a (:1a purchases)] [:3a (:3a purchases)]] (search-purchases purchases :category = :clothes))))

  (testing "Error receiving wrong parameter types"
    (is (thrown? ExceptionInfo (search-purchases purchases :store >= "Duda Store")))
    (is (thrown? ExceptionInfo (search-purchases purchases :category <= :food)))
    (is (thrown? ExceptionInfo (search-purchases purchases :credit-card-id > :1a)))
    (is (thrown? ExceptionInfo (search-purchases purchases :id < :2b)))))

(deftest search-purchases-by-store-test
  (testing "Success on searching valid purchases stores"
    (is (= [[:1a (:1a purchases)]] (search-purchases-by-store purchases "Duda Store")))
    (is (= [[:2a (:2a purchases)]] (search-purchases-by-store purchases "Davi Store")))
    (is (= [[:3a (:3a purchases)]] (search-purchases-by-store purchases "Pandora Store")))
    (is (= [[:4a (:4a purchases)]] (search-purchases-by-store purchases "Burger King"))))

  (testing "Error receiving wrong parameters"
    (is (thrown? ExceptionInfo (search-purchases-by-store purchases :lojinha)))
    (is (thrown? ExceptionInfo (search-purchases-by-store purchases-grouped-by-category "Duda Store")))))

(deftest first-card-id-test
  (testing "Success on getting first card id"
    (let [credit-cards {:5c {:id        :5c,
                             :number    1234567890,
                             :cvv       213,
                             :due-date  (j/local-date-time 2015 10 12 12 12 12),
                             :limit     1000,
                             :client-id :9d},
                        :6c {:id        :6c,
                             :number    1234567891,
                             :cvv       214,
                             :due-date  (j/local-date-time 2015 10 12 12 12 12),
                             :limit     1000,
                             :client-id :10d}}
          credit-cards2 {:7c {:id        :7c,
                              :number    1234567892,
                              :cvv       215,
                              :due-date  (j/local-date-time 2015 10 12 12 12 12),
                              :limit     1000,
                              :client-id :11d},
                         :8c {:id        :8c,
                              :number    1234567893,
                              :cvv       216,
                              :due-date  (j/local-date-time 2015 10 12 12 12 12),
                              :limit     1000,
                              :client-id :12d}}]
      (is (= :5c (first-card-id credit-cards)))
      (is (= :7c (first-card-id credit-cards2))))))

(deftest first-client-id-test
  (testing "Success on getting first card id"
    (let [clients {:10d {:id    :10d,
                         :name  "Duda",
                         :cpf   54638723678,
                         :email "duda@duda.com"},
                   :11d {:id    :11d,
                         :name  "Pandora",
                         :cpf   22900800702,
                         :email "pandora@duda.com"}}
          clients2 {:12d {:id    :12d,
                          :name  "Ramona",
                          :cpf   33200304590,
                          :email "ramona@duda.com"}
                    :13d {:id    :13d,
                          :name  "Romeu",
                          :cpf   44200304590,
                          :email "romeu@duda.com"}}]
      (is (= :10d (first-client-id clients)))
      (is (= :12d (first-client-id clients2))))))

(deftest sum-purchases-values-test
  (testing "Success on sum purchases values from list"
    (let [purchases-list [[:5a
                           {:id             :5a,
                            :date           (j/local-date-time 2015 10 12 12 12 12),
                            :value          100,
                            :store          "Davi Store",
                            :category       :games,
                            :credit-card-id :5c}],
                          [:6a
                           {:id             :6a,
                            :date           (j/local-date-time 2015 10 12 12 12 12),
                            :value          30,
                            :store          "Duda Store",
                            :category       :clothes,
                            :credit-card-id :5c}],
                          [:7a
                           {:id             :7a,
                            :date           (j/local-date-time 2015 10 12 12 12 12),
                            :value          99.99,
                            :store          "Ramona Store",
                            :category       :food,
                            :credit-card-id :5c}]]]
      (is (= 229.99 (sum-purchases-values purchases-list)))))

  (testing "Error receiving wrong format parameters"
    (let [wrong-format-list [{:id             :5a,
                              :date           (j/local-date-time 2015 10 12 12 12 12),
                              :value          100,
                              :store          "Davi Store",
                              :category       :games,
                              :credit-card-id :5c},
                             {:id             :6a,
                              :date           (j/local-date-time 2015 10 12 12 12 12),
                              :value          30,
                              :store          "Duda Store",
                              :category       :clothes,
                              :credit-card-id :5c},
                             {:id             :7a,
                              :date           (j/local-date-time 2015 10 12 12 12 12),
                              :value          99.99,
                              :store          "Ramona Store",
                              :category       :food,
                              :credit-card-id :5c}]]
      (is (thrown? ExceptionInfo (sum-purchases-values wrong-format-list)))
      (is (thrown? ExceptionInfo (sum-purchases-values purchases-grouped-by-category)))
      (is (thrown? ExceptionInfo (sum-purchases-values purchases-grouped-by-month))))))

(deftest invoices-by-group-test
  (testing "Success on get invoices by valid purchase groups"
    (is (= {:clothes 230, :games 100, :food 76} (invoices-by-group purchases-grouped-by-category)))
    (is (= {10 330, 11 76} (invoices-by-group purchases-grouped-by-month)))

    (let [grouped-purchases-store {"Duda Store"
                                   [[:1a {:id             :1a,
                                          :date           (j/local-date-time 2015 10 12 12 12 12),
                                          :value          50,
                                          :store          "Duda Store",
                                          :category       :clothes,
                                          :credit-card-id :5c}],
                                    [:2a {:id             :2a,
                                          :date           (j/local-date-time 2015 10 12 12 12 12),
                                          :value          100,
                                          :store          "Duda Store",
                                          :category       :clothes,
                                          :credit-card-id :5c}],
                                    [:4a {:id             :4a,
                                          :date           (j/plus (j/local-date-time 2015 10 12 12 12 12) (j/months 1)),
                                          :value          76,
                                          :store          "Duda Store",
                                          :category       :clothes,
                                          :credit-card-id :5c}]],
                                   "Pandora Store"
                                   [[:3a {:id             :3a,
                                          :date           (j/local-date-time 2015 10 12 12 12 12),
                                          :value          180,
                                          :store          "Pandora Store",
                                          :category       :health,
                                          :credit-card-id :5c}]]}]
      (is (= {"Duda Store" 226 "Pandora Store" 180} (invoices-by-group grouped-purchases-store)))))

  (testing "Error receiving wrong format parameters"
    (is (thrown? ExceptionInfo (invoices-by-group [] purchases-grouped-by-category)))
    (is (thrown? ExceptionInfo (invoices-by-group purchases)))))

(deftest add-to-collection!-test
  (testing "Success on adding new items to each of the collections"
    (let [clients (atom {})
          credit-cards (atom {})
          purchases (atom {})]
      (add-to-collection! clients {:id    :34y234gy23
                                   :name  "Fulano"
                                   :cpf   23454367580
                                   :email "fulano@fulano.com"})
      (add-to-collection! credit-cards {:id        :234iu23hi4
                                        :due-date  (j/local-date-time 2015 10 12 12 12 12)
                                        :number    1234567890
                                        :cvv       324
                                        :limit     2999
                                        :client-id :23h4iu23h4})
      (add-to-collection! purchases {:id             :23i4uh324
                                     :date           (j/local-date-time 2015 10 12 12 12 12)
                                     :value          234
                                     :store          "Outback"
                                     :category       :food
                                     :credit-card-id :234iuh23i4})

      (is (= 1 (count @clients)))
      (is (= 1 (count @credit-cards)))
      (is (= 1 (count @purchases)))

      (is (s/validate cs/Purchase (:23i4uh324 @purchases)))
      (is (s/validate cs/Purchases @purchases))
      (is (s/validate cs/Client (:34y234gy23 @clients)))
      (is (s/validate cs/Clients @clients))
      (is (s/validate cs/CreditCard (:234iu23hi4 @credit-cards)))
      (is (s/validate cs/CreditCards @credit-cards)))))