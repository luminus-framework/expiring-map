(ns expiring-map.core
  (:refer-clojure :exclude [assoc! dissoc!])
  (:import[net.jodah.expiringmap
           ExpirationPolicy
           ExpiringMap
           ExpiringMap$Builder
           ExpirationListener]
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

(defn- expiration-policy [^ExpiringMap$Builder builder {:keys [expiration-policy]}]
  (if-let [policy (expiration-policies expiration-policy)]
   (.expirationPolicy builder policy)
    builder))

(defn- set-timeout [^ExpiringMap$Builder builder timeout {:keys [time-unit]}]
  (.expiration builder timeout (clojure.core/get time-units time-unit TimeUnit/SECONDS)))

(defn- set-max-size [^ExpiringMap$Builder builder {:keys [max-size]}]
  (if max-size
    (.maxSize builder max-size)
    builder))

(defn- listeners [^ExpiringMap$Builder builder {:keys [listeners]}]
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
      (set-max-size opts)
      (set-timeout timeout opts)
      (listeners opts)
      (.build)))

(defn assoc!
  ([^ExpiringMap m k v]
   (.put m k v)
   m)
  ([^ExpiringMap m k v & kvs]
   (.put m k v)
   (doseq [[k v] (partition 2 kvs)]
     (.put m k v))
   m))

(defn dissoc!
  ([^ExpiringMap m k] (.remove m k) m)
  ([^ExpiringMap m k & ks]
   (.remove m k)
   (doseq [k ks]
     (.remove m k))
   m))

(defn clear! [^ExpiringMap m]
  (.clear m) m)

(defn reset-expiration! [^ExpiringMap m k]
  (.resetExpiration m k) m)
