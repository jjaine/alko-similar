(ns alko-similar-server.router
  (:require [reitit.ring :as ring]
            [alko-similar-server.product :as product]
            [alko-similar-server.scraper :as scraper]
            [alko-similar-server.handlers :as handlers]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [ring.util.response :as rr]
            [reitit.coercion.spec :as coercion-spec]
            [reitit.ring.coercion :as coercion]
            [clojure.spec.alpha :as s]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.parameters :as parameters]
            [ring.middleware.cors :refer [wrap-cors]]))

(def swagger-docs
  ["/docs/swagger.json"
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
                        :access-control-allow-methods [:get :post]]]}})

(s/def ::filter-by string?)
(s/def ::min-price float?)
(s/def ::max-price float?)

(defn routes
  [env]
  (let [db (:jbdc-url env)]
    (ring/ring-handler
     (ring/router
      [swagger-docs
       ["/api/products/popular" {:summary "Get popular products"
                                 :get     {:handler (handlers/list-popular-products db)}}]
       ["/api/products/recent" {:summary "Get recently searched products"
                                :get     {:handler (handlers/list-recent-products db)}}]
       ["/api/products" {:summary "Add new product to db or update existing"
                         :post {:handler (handlers/update-product! db)
                                :parameters {:body {:id string?}}}}]
       ["/api/product/:product-id" {:summary "Get product details with id"
                                    :get     {:parameters {:path {:product-id string?}}
                                              :handler    product/get-details}}]
       ["/api/similar/:product-id" {:summary "Get similar products with id"
                                    :get     {:parameters {:path {:product-id string?}
                                                           :query (s/keys :opt-un [::filter-by ::min-price ::max-price])}
                                              :handler    product/get-similars}}]
       ["/api/product/" {:get {:handler (fn [_] (rr/not-found ["Anna tuotenumero!"]))}}]
       ["/api/similar/" {:get {:handler (fn [_] (rr/not-found ["Anna tuotenumero!"]))}}]
       ["/api/scrape" {:summary "Scrape the alko website"
                       :get     {:handler (fn [_] (let [res (scraper/scrape-data)]
                                                    res))}}]
       ["/api/health" {:summary "Is the server running?"
                       :get     {:handler (fn [_] (rr/response nil))}}]]
      router-config)
     (ring/routes
      (swagger-ui/create-swagger-ui-handler {:path "/docs"
                                             :url "/docs/swagger.json"})
      (ring/create-default-handler)))))