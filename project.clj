(defproject workman "0.0.1-SNAPSHOT"
    :description "A task manager"
    :dependencies [
        [org.clojure/clojure "1.8.0"]
        [ring "1.10.0"]
        [com.taoensso/timbre "6.1.0"]
        [org.clojure/data.json "2.4.0"]
        [org.xerial/sqlite-jdbc "3.41.2.2"]
        [org.clojure/java.jdbc "0.7.12"]
        [honeysql "1.0.461"]
    ]
    :main ^:skip-aot workman.core
    :target-path "target/%s"
    :profiles {:uberjar {:aot :all}})
