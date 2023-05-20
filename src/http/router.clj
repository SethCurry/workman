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

(defn- is-uri-keyword [s] (string/starts-with? s ":"))

(defn- parse-uri-keyword [s] ((keyword (subs s 1))))

(defn- parse-uri-param [k v]
  (if (is-uri-keyword k)
    {(parse-uri-keyword k) v}
    {}))

(defn get-params
  [pattern req]
  (into
    {}
    (map
     parse-uri-param
     pattern
     req)))

(defn- method-matches?
  [route request]
  (= (get route :method) (get request :request-method)))

(defn- route-matches?
  [request]
  (fn [x]
    (if (and
         (method-matches? x request)
         (uris-equal? (split-request-uri x) (split-request-uri request)))
         true
         false)))

(defn find-route
  [routes request]
  (first (filter
          (route-matches? request)
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
