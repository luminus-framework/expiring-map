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

(em/assoc! cache :foo "foo" :bar "bar")
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

### Criterium Benchmarks

The benchmarks below test the assoc/dissoc cycle
for the expiring-map, regular atom, and core.cache
atom.

### atom
```
WARNING: Final GC required 409.9942905040538 % of runtime
Evaluation count : 4756014 in 6 samples of 792669 calls.
             Execution time mean : 120.809576 ns
    Execution time std-deviation : 2.684070 ns
   Execution time lower quantile : 118.923613 ns ( 2.5%)
   Execution time upper quantile : 124.374281 ns (97.5%)
                   Overhead used : 1.988494 ns
```

#### expiring-map

```
WARNING: Final GC required 481.8947027815336 % of runtime
Evaluation count : 1893510 in 6 samples of 315585 calls.
             Execution time mean : 322.901037 ns
    Execution time std-deviation : 11.538941 ns
   Execution time lower quantile : 313.718453 ns ( 2.5%)
   Execution time upper quantile : 340.737836 ns (97.5%)
                   Overhead used : 1.988494 ns

Found 1 outliers in 6 samples (16.6667 %)
	low-severe	 1 (16.6667 %)
 Variance from outliers : 13.8889 % Variance is moderately inflated by outliers
```

#### core.cache

```
WARNING: Final GC required 398.9148954873636 % of runtime
Evaluation count : 925734 in 6 samples of 154289 calls.
             Execution time mean : 645.750787 ns
    Execution time std-deviation : 19.287433 ns
   Execution time lower quantile : 625.196819 ns ( 2.5%)
   Execution time upper quantile : 666.538213 ns (97.5%)
                   Overhead used : 1.988494 ns
```

## License

Copyright © 2015 Dmitri Sotnikov

Distributed under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html)
