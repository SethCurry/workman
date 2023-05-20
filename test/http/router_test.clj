(ns http.router-test
  (:require
   [http.router :as http]
   [clojure.test :as test]))

(test/deftest split-uri-basic
  (test/testing "split-request-uri basic"
    (test/is (= (http/split-request-uri {:uri "/some/random/uri"}) ["some" "random" "uri"]))))