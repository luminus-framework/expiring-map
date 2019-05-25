(defproject expiring-map "0.1.9"
  :description "a thread-safe map that expires entries"
  :url "https://github.com/yogthos/expiring-map"
  :license {:name "Apache 2.0 License"
            :url "https://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [net.jodah/expiringmap "0.5.9"]]

  :test-selectors {:default   (complement :benchmark)
                   :benchmark :benchmark
                   :all       (constantly true)}

  :profiles
  {:dev
   {:jvm-opts ["-XX:-TieredCompilation"]
    :global-vars {*warn-on-reflection* true}
    :dependencies [[criterium "0.4.3" :scope "test"]
                   [org.clojure/core.cache "0.6.4"]]}})
