# expiring-map

A Clojure wrapper for the [expiringmap](https://github.com/jhalterman/expiringmap).
The expiring-map is a thread-safe mutable map that supports expiry for elements.

## Usage

[![Clojars Project](http://clojars.org/expiring-map/latest-version.svg)](http://clojars.org/expiring-map)

### Basic Usage

The expiry map responds to the standard collection functions such as
`get`, `count`, `keys`, `vals`, and `empty?`.

In addition, the map provides the following functions for mutating its values:

* `assoc!` - associates a key value pair with the map
* `dissoc!` - dissociates a key from the map
* `clear!` - removes all key/value pairs from the map
* `reset-expiration!` - resets the expirating timeout for the given key

```clojure
(require '[expiring-map.core :as em])

(def cache (em/expiring-map 10))

(em/assoc! cache :foo "bar")

(get cache :bar)
(get cache :foo)
(get cache :bar "defualt")

(count cache)
(vals cache)
(keys cache)
(empty? cache)

(em/dissoc! cache :foo)

(em/assoc! cache :foo "foo")
(em/assoc! cache :bar "bar")
(em/reset-expiration! cache :foo)
(em/clear! cache)
```

### Time Units

The time unit defaults to `:seconds`, the following time units are supported:

* `:nanoseconds`
* `:microseconds`
* `:milliseconds`
* `:seconds`
* `:minutes`
* `:hours`
* `:day`

e.g:

```clojure
(em/expiring-map 10 {:time-unit :minutes})
```

### Expiration Policy

The expiration policy can either be set to creation time or last access, defaults to
creation time.

```clojure
(def cache (em/expiring-map
            10
            {:expiration-policy :access
             :time-unit :minutes}))
```

### Listeners

The map allows setting expiry listeners, a listener must be a function that
accepts the key and the value that expires:

```clojure
(def cache (em/expiring-map
            10
            {:listeners [(fn [k v] (println "Expired:" k v))]}))
```
## License

Copyright © 2015 Dmitri Sotnikov

Distributed under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html)
