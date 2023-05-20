(ns http.router)

(use 'taoensso.timbre)

; route object
; {:method :get :path "/test" :handler (fn)}

(defn find-route
    [routes request]
    (first (filter
      (fn [x]
        (if
          (and
            (= (get x :method) (get request :request-method))
            (= (get x :uri) (get request :uri)))
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
        ((get r :handler) request))
      ))
  )
