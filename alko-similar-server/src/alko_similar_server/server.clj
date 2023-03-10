(ns alko-similar-server.server
  (:require [ring.adapter.jetty :as jetty]
            [integrant.core :as ig]
            [environ.core :refer [env]]
            [alko-similar-server.router :as router]
            [alko-similar-server.scraper :as scraper]
            [next.jdbc :as jdbc]
            [next.jdbc.connection :as njc])
  (:import (com.zaxxer.hikari HikariDataSource)))

(defn app
  [env]
  (router/routes env))

(defmethod ig/prep-key :server/jetty
  [_ config]
  (if-let [env-port (env :port)]
    (merge config {:port (Integer/parseInt env-port)})
    config))

(defmethod ig/prep-key :db/postgres
  [_ config]
  (if-let [jdbc-url (env :jdbc-database-url)]
    (merge config {:jdbc-url jdbc-url})
    config))

(defmethod ig/init-key :server/jetty
  [_ {:keys [handler port]}]
  (println (str "Server started on port " port))
  (jetty/run-jetty handler {:port port :join? false}))

(defmethod ig/init-key :alko-similar-server/app
  [_ config]
  (println "Initializing app")
  (app config))

(defmethod ig/init-key :db/postgres
  [_ {:keys [jdbc-url]}]
  (jdbc/with-options
    (njc/->pool HikariDataSource {:jdbcUrl jdbc-url})
    jdbc/snake-kebab-opts))

(defmethod ig/halt-key! :db/postgres
  [_ config]
  (println "Closing db connection")
  (.close ^HikariDataSource (:connectable config)))

(defmethod ig/halt-key! :server/jetty
  [_ server]
  (println "Stopping server " server)
  (.stop server))

(defn -main
  [config-file]
  (scraper/scrape-data)
  (let [config (-> config-file
                   slurp
                   ig/read-string)]
    (-> config
        ig/prep
        ig/init)))

(comment
  (-main "resources/config.edn") 
  )