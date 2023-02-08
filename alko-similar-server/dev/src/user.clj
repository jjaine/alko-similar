(ns user
  (:require [integrant.repl :as ig-repl]
            [integrant.core :as ig]
            [integrant.repl.state :as ig-state]
            [alko-similar-server.server]))

(ig-repl/set-prep!
 (fn [] (-> "resources/config.edn"
            slurp
            ig/read-string)))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)

(def app (-> ig-state/system :alko-similar-server/app))

(comment
  (app {:request-method :get :uri "/api/product" :query-params {"id" "915083"}})
  (app {:request-method :get :uri "/"})
  (app {:request-method :get :uri "/swagger.json"})
  (app {:request-method :get :uri "/health"})
  (app {:request-method :get :uri "/scrape"})
  (go)
  (halt)
  (reset)
  (reset-all)
  )
  