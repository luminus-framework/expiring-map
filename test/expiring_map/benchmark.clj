(ns expiring-map.benchmark
  (:require [clojure.test :refer :all]
            [criterium.core :as criterium]
            [clojure.core.cache :refer [ttl-cache-factory]]
            [expiring-map.core :as em]))

(deftest ^:benchmark bench-expiring-map-assoc []
  #_(println "++++ expiring-map assoc!")
  #_(let [cache (em/expiring-map 1 {:time-unit :hours})
          value {:bar "baz" :baz [1 2 3 {:foo "bar"}]}]
      (criterium/quick-bench (em/assoc! cache :foo value)))

  #_(println "++++ atom assoc")
  #_(let [cache (atom {})
          value {:bar "baz" :baz [1 2 3 {:foo "bar"}]}]
      (criterium/quick-bench (swap! cache assoc :foo value)))

  (println "++++ expiring-map assoc!/dissoc!")
  (let [cache (em/expiring-map 1 {:time-unit :hours})
        value {:bar "baz" :baz [1 2 3 {:foo "bar"}]}]
    (criterium/quick-bench
     (do
       (em/assoc! cache :foo value)
       (em/dissoc! cache :foo))))

  (println "++++ core.cache assoc/dissoc")
  (let [cache (atom (ttl-cache-factory {} :ttl (* 1000 1000)))
        value {:bar "baz" :baz [1 2 3 {:foo "bar"}]}]
    (criterium/quick-bench
     (do
       (swap! cache assoc :foo value)
       (swap! cache dissoc :foo value))))

  (println "++++ atom assoc/dissoc")
  (let [cache (atom {})
        value {:bar "baz" :baz [1 2 3 {:foo "bar"}]}]
    (criterium/quick-bench
     (do
       (swap! cache assoc :foo value)
       (swap! cache dissoc :foo)))))
