(ns expiring-map.core
  (:import net.jodah.expiringmap.ExpiringMap
           java.util.concurrent.TimeUnit))

(def time-unit
  {:seconds TimeUnit/SECONDS
   :minutes TimeUnit/MINUTES
   :hours   TimeUnit/HOURS})

(defn expiring-map []
  (-> (ExpiringMap/builder)
      (.expiration 30 TimeUnit/MINUTES)
      (.build)))

(defprotocol Cache
  (put! [this k v])
  (get [this k]))

(extend-protocol Cache
  ExpiringMap
  (put! [this k v] (.put this k v))
  (get [this k] (.get this k)))



