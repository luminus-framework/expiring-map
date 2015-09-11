# expiring-map
A Clojure wrapper for the [expiringmap](https://github.com/jhalterman/expiringmap).

## Usage

[]

```clojure
(use 'expiring-map.core)

(def cache (expiring-map 10))

(put! cache :foo "bar")

(get cache :bar)
(get cache :foo)
(get cache :bar "defualt")

(count cache)
(vals cache)
(keys cache)
(empty? cache)
(dissoc cache :foo)
```

The time unit defaults to seconds, the following time units are supported:

* `:nanoseconds`
* `:microseconds`
* `:milliseconds`
* `:seconds`
* `:minutes`
* `:hours`
* `:day`

e.g:

```clojure
(expiring-map 10 :seconds)
```

## License

Copyright © 2015 Dmitri Sotnikov

Distributed under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html)
