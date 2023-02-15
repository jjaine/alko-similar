(ns alko-similar-server.similar.routes
  (:require [alko-similar-server.similar.product :as product]))

(defn routes
  [_env]
  ["/product/:product-id" {:summary "Get a product details with id"
                           :get     {:parameters {:path {:product-id string?}
                                                  :query {:filter-by string?}}
                                     :handler    product/get-details}}])