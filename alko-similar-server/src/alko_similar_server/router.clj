(ns alko-similar-server.router
  (:require [reitit.ring :as ring]
            [alko-similar-server.similar.routes :as similar]
            [alko-similar-server.scraper :as scraper]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [ring.util.response :as rr]
            [reitit.coercion.spec :as coercion-spec]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.parameters :as parameters]
            [ring.middleware.cors :refer [wrap-cors]]))

(def swagger-docs
  ["/swagger.json"
   {:get {:no-doc true
          :swagger {:basePath "/"
                    :info {:title "Alko Similar API"
                           :description "API for the alko similar project"
                           :version "1.0.0"}}
          :handler (swagger/create-swagger-handler)}}])

(def router-config
  {:data {:coercion coercion-spec/coercion
          :muuntaja m/instance
          :middleware [parameters/parameters-middleware
                       swagger/swagger-feature
                       muuntaja/format-middleware
                       exception/exception-middleware
                       coercion/coerce-request-middleware
                       [wrap-cors
                        :access-control-allow-origin [#"http://.*"]
                        :access-control-allow-methods [:get]]]}})

(defn routes
  [env]
  (ring/ring-handler
   (ring/router
    [swagger-docs
     ["/api" (similar/routes env)]
     ["/api/product/" {:get {:handler (fn [_] (rr/not-found ["No product id given"]))}}]
     ["/scrape" {:summary "Scrape the alko website"
                 :get     {:handler (fn [_] (let [res (scraper/scrape-data)]
                                              res))}}]
     ["/health" {:summary "Is the server running?"
                 :get     {:handler (fn [_] (rr/response nil))}}]]
    router-config)
   (ring/routes
    (swagger-ui/create-swagger-ui-handler {:path "/"}))))
