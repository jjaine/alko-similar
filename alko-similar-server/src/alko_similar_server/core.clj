(ns alko-similar-server.core
  (:require alko-similar-server.server
            ; load mount state
            [mount.core :as mount]
            [taoensso.timbre :refer [info]])
  (:gen-class))

(defn -main
  "The entry point for uberjar."
  [& _args]
  (info "Starting server...")
  (mount/start)
  (deref (promise)))

;(-main)