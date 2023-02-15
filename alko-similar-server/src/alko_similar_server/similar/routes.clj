(ns alko-similar-server.similar.routes
  (:require [alko-similar-server.similar.product :as product]
            [clojure.spec.alpha :as s]))

(s/def ::filter-by string?)

(defn routes
  [_env]
  ["/product/:product-id" {:summary "Get a product details with id"
                           :get     {:parameters {:path {:product-id string?}
                                                  :query (s/keys :opt-un [::filter-by])}
                                     :handler    product/get-details}}])