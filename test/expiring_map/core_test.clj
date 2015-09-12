(ns expiring-map.core-test
  (:require [clojure.test :refer :all]
            [expiring-map.core :as em]))

(deftest assoc!-get-test
  (let [m (em/expiring-map 10)]
    (em/assoc! m :foo "bar")
    (em/assoc! m :bar [:foo :bar 123 "baz" {:foo "bar"}])
    (= m {:foo "bar", :bar [:foo :bar 123 "baz" {:foo "bar"}]})
    (= "bar" (get m :foo))
    (= [:foo :bar 123 "baz" {:foo "bar"}] (get m :bar))
    (= "default" (get m :baz "default"))))
