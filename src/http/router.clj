(ns http.router
  (:require [clojure.string :as string]))

; route object
; {:method :get :uri "/test" :handler (fn)}

(defn split-request-uri
  "Splits a request uri; requires a map with a :uri key"
  [request]
  (drop 1 (string/split (get request :uri) #"/")))
  
(defn uris-equal?
  [pattern req]
  (if
    (= (count pattern) (count req))
    (if (nil?
      (some false?
        (map
          (fn
            [p r]
            (if
              (and
                (not (nil? p))
                (string/starts-with? p ":")
                (not (nil? r)))
              true
              (= p r)))
          pattern
          (concat req (repeat nil)))))
      true
      false)
    false))

(defn get-params
  [pattern req]
  (into
    {}
    (map
      (fn
        [p r]
        (if
          (string/starts-with? p ":")
          {(keyword (subs p 1)) r}
          {}
        ))
      pattern
      req)))

(defn find-route
  [routes request]
  (first (filter
    (fn [x]
      (if
        (and
          (= (get x :method) (get request :request-method))
          (uris-equal? (split-request-uri x) (split-request-uri request)))
        true
        false))
    routes)))

(defn not-found
  [request]
  {
    :status 404
    :headers {"Content-Type" "text/html"}
    :body (str "No handler for uri " (get request :uri) " and method " (name (get request :request-method)))
  })

(defn router
  [routes]
  (fn [request]
    (let [r (find-route routes request)]
      (if (nil? r)
        (not-found request)
        ((get r :handler)
          (into
            request
            {:uri-params (get-params (split-request-uri r) (split-request-uri request))}))))))
