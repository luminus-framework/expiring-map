(ns expiring-map.core-test
  (:require [clojure.test :refer :all]
            [expiring-map.core :refer :all]))

(deftest put!-get-test
  (let [m (expiring-map 10)]
    (put! m :foo "bar")
    (put! m :bar [:foo :bar 123 "baz" {:foo "bar"}])
    (= m {:foo "bar", :bar [:foo :bar 123 "baz" {:foo "bar"}]})
    (= "bar" (get m :foo))
    (= [:foo :bar 123 "baz" {:foo "bar"}] (get m :bar))))
