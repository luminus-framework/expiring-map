(ns expiring-map.core
  (:refer-clojure :exclude [assoc! dissoc!])
  (:import[net.jodah.expiringmap ExpirationPolicy ExpiringMap ExpirationListener]
           java.util.concurrent.TimeUnit))

(def ^:private time-units
  {:nanoseconds  TimeUnit/NANOSECONDS
   :microseconds TimeUnit/MICROSECONDS
   :milliseconds TimeUnit/MILLISECONDS
   :seconds      TimeUnit/SECONDS
   :minutes      TimeUnit/MINUTES
   :hours        TimeUnit/HOURS
   :day          TimeUnit/DAYS})

(def ^:private expiration-policies
  {:access   ExpirationPolicy/ACCESSED
   :creation ExpirationPolicy/CREATED})

(defn- expiration-policy [builder {:keys [expiration-policy]}]
  (if-let [policy (expiration-policies expiration-policy)]
   (.expirationPolicy builder policy)
    builder))

(defn- set-timeout [builder timeout {:keys [time-unit]}]
  (.expiration builder timeout (clojure.core/get time-units time-unit TimeUnit/SECONDS)))

(defn- listeners [builder {:keys [listeners]}]
  (reduce
   (fn [builder listener]
     (.expirationListener
      builder
      (proxy [ExpirationListener][]
        (expired [k v] (listener k v)))))
   builder listeners))

(defn expiring-map [timeout & [opts]]
  (-> (ExpiringMap/builder)
      (expiration-policy opts)
      (set-timeout timeout opts)
      (listeners opts)
      (.build)))

(defn assoc!
  ([m k v]
   (.put m k v)
   m)
  ([m k v & kvs]
   (.put m k v)
   (doseq [[k v] (partition 2 kvs)]
     (.put m k v))
   m))

(defn dissoc!
  ([m k] (.remove m k) m)
  ([m k & ks]
   (.remove m k)
   (doseq [k ks]
     (.remove m k))
   m))

(defn clear! [m]
  (.clear m) m)

(defn reset-expiration! [m k]
  (.resetExpiration m k) m)
