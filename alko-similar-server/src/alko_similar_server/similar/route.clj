(ns alko-similar-server.similar.route)

(defn routes
  [env]
  ["/products" {:get {:handler (fn [req] {:status 200
                                           :body "This is a list of products"})}}])