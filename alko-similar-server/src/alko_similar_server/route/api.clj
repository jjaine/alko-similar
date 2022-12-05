(ns alko-similar-server.route.api
  (:require [clojure.spec.alpha :as s]
            [alko-similar-server.scraper :as scraper]
            [alko-similar-server.similar :as similar]
            ;[taoensso.timbre :refer [infof]]
            ))

(s/def ::id string?)

(s/def ::similar-request (s/keys :req-un [::id]))

(def route
  ["/api"
   ["/get-similar"
    {:get
     {:parameters {:query ::similar-request}
      :handler    (fn [{{{:keys [id]} :query} :parameters}]
                    ;(infof "Received: %s %d %d" id x y)
                    (scraper/scrape-data)
                    (similar/get-similar id)
                    {:status 200
                     :body {:received [id]}})}}]])