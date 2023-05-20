(ns workman.core
  (:require
   [ring.adapter.jetty :as jetty]
   [http.router :as http]))

(defn handler [request]
  {
    :status 200
    :headers {"Content-Type" "text/html"}
    :body "Hello World"
  })

(defn id-handler [request]
  {
    :status 200
    :headers {"Content-Type" "text/html"}
    :body (str "Hello user " (get (get request :uri-params) :id))
  })

(def routes [
                 (http/->Route :get "/" handler)
                 (http/->Route :get "/users/:id" id-handler)
])

(defn -main
    []
    (jetty/run-jetty (http/router routes) {
        :port 3000
    }))