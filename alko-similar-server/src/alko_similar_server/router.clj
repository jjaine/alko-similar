(ns alko-similar-server.router
  (:require [reitit.ring :as ring]
            [alko-similar-server.similar.route :as similar]
            [alko-similar-server.scraper :as scraper]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja]))

(def swagger-docs
  ["/swagger.json"
   {:get {:no-doc true
          :swagger {:basePath "/"
                    :info {:title "Alko Similar API"
                           :description "API for the alko similar project"
                           :version "1.0.0"}}
          :handler (swagger/create-swagger-handler)}}])

(def router-config
  {:data {:muuntaja m/instance
          :middleware [swagger/swagger-feature
                       muuntaja/format-middleware]}})

(defn routes
  [env]
  (ring/ring-handler
   (ring/router
    [swagger-docs
     ["/api" (similar/routes env)]
     ["/scrape" {:summary "Scrape the alko website"
                 :get {:handler (fn [_] (let [res (scraper/scrape-data)]
                                          res))}}]
     ["/health" {:summary "Is the server running?"
                 :get {:handler (fn [_] {:status 200, :body "ok"})}}]]
    router-config)
   (ring/routes
    (swagger-ui/create-swagger-ui-handler {:path "/"}))))
