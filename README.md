# expiring-map
A Clojure wrapper for the [expiringmap](https://github.com/jhalterman/expiringmap).

## Usage

[![Clojars Project](http://clojars.org/expiring-map/latest-version.svg)](http://clojars.org/expiring-map)

```clojure
(require '[expiring-map.core :as em])

(def cache (em/expiring-map 10))

(em/put! cache :foo "bar")

(em/get cache :bar)
(em/get cache :foo)
(em/get cache :bar "defualt")

(em/count cache)
(em/vals cache)
(em/keys cache)
(em/empty? cache)
(em/dissoc cache :foo)
```

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
(expiring-map 10 :minutes)
```

## License

Copyright © 2015 Dmitri Sotnikov

Distributed under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html)
