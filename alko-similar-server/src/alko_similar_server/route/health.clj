(ns alko-similar-server.route.health)

(def route
  ["/health" {:summary "Is the server running?"
              :get {:handler (fn [_] {:status 200, :body "ok"})}}])