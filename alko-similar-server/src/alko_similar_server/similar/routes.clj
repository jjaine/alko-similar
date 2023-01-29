(ns alko-similar-server.similar.routes
  (:require [clojure.spec.alpha :as s]
            [ring.util.response :as rr]
            [alko-similar-server.similar.product :as product]))

(defn routes
  [env]
  ["/product/:product-id" {:summary "Get a product details with id"
                :get {:parameters {:path {:product-id string?}}
                      :handler product/get-details}}])