(ns http.router-test)
(use 'clojure.test)
(use 'http.router)

(deftest split-uri-basic
  (testing "split-request-uri basic"
    (is (= (split-request-uri {:uri "/some/random/uri"}) ["some" "random" "uri"]))))