(ns user
  (:require [integrant.repl :as ig-repl]
            [integrant.core :as ig]
            [integrant.repl.state :as ig-state]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [clj-time.core :as t]
            [clj-time.coerce :as tc]
            [alko-similar-server.server]))

(ig-repl/set-prep!
 (fn [] 
   (-> "dev/resources/config.edn"
       slurp
       ig/read-string
       ig/prep)))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)

(def app (-> ig-state/system :alko-similar-server/app))
(def db (-> ig-state/system :db/postgres))

(comment
  (app {:request-method :get :uri "/api/product/" :path-params {:product-id "915083"}})
  (app {:request-method :get :uri "/api/product/"})
  (app {:request-method :get :uri "/"})
  (app {:request-method :get :uri "/docs/swagger.json"})
  (app {:request-method :get :uri "/api/health"})
  (app {:request-method :get :uri "/api/scrape"})
  (app {:request-method :get :uri "/api/products"})
  (go)
  (halt)
  (reset)
  (reset-all)

  (with-open [conn (jdbc/get-connection db)]
    (let [products (sql/find-by-keys conn :product :all)]
      {:products products}))

  (let [product {:product/id "test" :product/look_count 2 :product/look_date (tc/to-sql-time (t/now))}]
    (-> (sql/update! db :product product (select-keys product [:product/id]))
        :next.jdbc/update-count
        (pos?))) 
  )
  
