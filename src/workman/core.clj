(ns workman.core)

(use 'ring.adapter.jetty)
(use 'http.router)

(defn handler [request]
    {
     :status 200
     :headers {"Content-Type" "text/html"}
     :body "Hello World"
    })

(def routes [
  {
    :method :get
    :uri "/"
    :handler handler
  }
])

(defn -main
    []
    (run-jetty (router routes) {
        :port 3000
    }))