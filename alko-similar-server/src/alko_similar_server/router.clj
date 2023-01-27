(ns alko-similar-server.router
  (:require [reitit.ring :as ring]
            [alko-similar-server.similar.route :as similar]
            [alko-similar-server.scraper :as scraper]))

(defn routes
  [env]
  (ring/ring-handler
   (ring/router
    [["/scrape" {:summary "Scrape the alko website"
                 :get {:handler (fn [_] ( (scraper/scrape-data)
                                          ))}}]
     ["/health" {:summary "Is the server running?"
                 :get {:handler (fn [_] {:status 200, :body "ok"})}}]
     ["/api" (similar/routes env)]])))