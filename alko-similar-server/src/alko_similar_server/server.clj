(ns alko-similar-server.server
  (:require
   [ring.adapter.jetty :as jetty]
   [integrant.core :as ig]
   [environ.core :refer [env]]
   [alko-similar-server.router :as router]))

(defn app
  [env]
  (router/routes env))

(defmethod ig/prep-key :server/jetty
  [_ config]
  (if-let [env-port (env :port)]
    (merge config {:port (Integer/parseInt env-port)})
    config))

(defmethod ig/init-key :server/jetty
  [_ {:keys [handler port]}]
  (println (str "Server started on port " port))
  (jetty/run-jetty handler {:port port :join? false}))

(defmethod ig/init-key :alko-similar-server/app
  [_ config]
  (println "Initializing app")
  (app config))

(defmethod ig/halt-key! :server/jetty
  [_ server]
  (println "Stopping server " server)
  (.stop server))

(defn -main
  [config-file]
  (let [config (-> config-file
                   slurp
                   ig/read-string)]
    (-> config
        ig/prep
        ig/init)))

(comment
  (-main "resources/config.edn"))