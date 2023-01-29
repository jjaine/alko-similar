(ns alko-similar-server.similar.product
  (:require [alko-similar-server.scraper :as scraper]
            [clojure.pprint :refer [pprint]]
            [ring.util.response :as rr]))

(defn get-details 
  [request]
  (pprint request)
  (let [id (-> request
               :path-params
               :product-id)
        selected (->> (filter (comp #{id} :id) @scraper/data)
                      first)
        response (rr/response selected)]
    (pprint response)
    response))

(comment
  (get-details {:path-params {:product-id "915083"}})
  )