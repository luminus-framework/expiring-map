(ns expiring-map.core
  (:refer-clojure :exclude [count get dissoc! put! keys vals empty?])
  (:import net.jodah.expiringmap.ExpiringMap
           java.util.concurrent.TimeUnit))

(def ^:private time-unit
  {:nanoseconds  TimeUnit/NANOSECONDS
   :microseconds TimeUnit/MICROSECONDS
   :milliseconds TimeUnit/MILLISECONDS
   :seconds      TimeUnit/SECONDS
   :minutes      TimeUnit/MINUTES
   :hours        TimeUnit/HOURS
   :day          TimeUnit/DAYS})

(defn expiring-map [timeout & [units]]
  (-> (ExpiringMap/builder)
      (.expiration timeout (clojure.core/get time-unit units TimeUnit/SECONDS))
      (.build)))

(defprotocol Cache
  (assoc! [this k v])
  (clear! [this])
  (dissoc! [this k])
  (count [this])
  (empty? [this])
  (get [this k] [this k default])
  (keys [this])
  (vals [this]))

(extend-type ExpiringMap
  Cache
  (assoc! [this k v] (.put this k v))
  (clear! [this] (.clear this))
  (dissoc! [this k] (.remove this k))
  (count [this] (.size this))
  (empty? [this] (.isEmpty this))
  (get
   ([this k] (.get this k))
   ([this k default](or (.get this k) default)))
  (keys [this] (seq (.keySet this)))
  (vals [this] (seq (.values this))))
