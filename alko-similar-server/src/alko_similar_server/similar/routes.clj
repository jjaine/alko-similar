(ns alko-similar-server.similar.routes
  (:require [alko-similar-server.similar.product :as product]
            [clojure.spec.alpha :as s]))

(s/def ::filter-by string?)
(s/def ::min-price float?)
(s/def ::max-price float?)

(defn routes
  [_env]
  ["/product/:product-id" {:summary "Get a product details with id"
                           :get     {:parameters {:path {:product-id string?}
                                                  :query (s/keys :opt-un [::filter-by ::min-price ::max-price])}
                                     :handler    product/get-details}}])