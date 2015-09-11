(ns expiring-map.core-test
  (:require [clojure.test :refer :all]
            [expiring-map.core :as em]))

(deftest assoc!-get-test
  (let [m (em/expiring-map 10)]
    (em/assoc! m :foo "bar")
    (em/assoc! m :bar [:foo :bar 123 "baz" {:foo "bar"}])
    (= m {:foo "bar", :bar [:foo :bar 123 "baz" {:foo "bar"}]})
    (= "bar" (em/get m :foo))
    (= [:foo :bar 123 "baz" {:foo "bar"}] (em/get m :bar))
    (= "default" (em/get m :baz "default"))))
