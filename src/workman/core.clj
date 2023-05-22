(ns workman.core
  (:require
   [ring.adapter.jetty :as jetty]
   [http.router :as http]
   [models.tasks :as tasks]))

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

(defn task-id-handler
  [request]
  (let [t (tasks/get-by-id (get (get request :uri-params) :id))]
    {
     :status 200
     :body (get t :name)
    }))

(def routes [
                 (http/->Route :get "/" handler)
                 (http/->Route :get "/users/:id" id-handler)
                 (http/->Route :get "/tasks/:id" task-id-handler)
])

(defn -main
    []
    (do
      (tasks/init)
      (print (get :id (tasks/create "name" "body" "status")))
      (print (tasks/get-by-id 1))
      (jetty/run-jetty (http/router routes) {
        :port 3000
      })
    ))
