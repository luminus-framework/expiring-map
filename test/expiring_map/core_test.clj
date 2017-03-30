(ns expiring-map.core-test
  (:require [clojure.test :refer :all]
            [expiring-map.core :as em]))

(deftest assoc!-get-test
  (let [m (em/expiring-map 10)]
    (em/assoc! m :foo "bar")
    (em/assoc! m :bar [:foo :bar 123 "baz" {:foo "bar"}])
    (is (= m {:foo "bar", :bar [:foo :bar 123 "baz" {:foo "bar"}]}))
    (is (= "bar" (get m :foo)))
    (is (= [:foo :bar 123 "baz" {:foo "bar"}] (get m :bar)))
    (is (= "default" (get m :baz "default")))))

(deftest max-size-test
  (let [m (em/expiring-map 10 {:max-size 1})]
    (em/assoc! m :foo "bar")
    (em/assoc! m :moo "mar")
    (is (= m {:moo "mar"}))))
