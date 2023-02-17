(defproject alko-similar-server "0.1.0-SNAPSHOT"
  :description "API for finding similar Alko products"
  :url ""
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring "1.9.6"]
                 [integrant "0.8.0"]
                 [environ "1.2.0"]
                 [metosin/reitit "0.5.18"]
                 [clj-time "0.15.2"]
                 [dk.ative/docjure "1.19.0"]
                 [ring-cors "0.1.13"]]
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :dev {:source-paths ["dev/src"]
                   :resource-paths ["dev/resources"]
                   :dependencies [[ring/ring-mock "0.4.0"]
                                  [integrant/repl "0.3.2"]]}}
  :uberjar-name "alko-similar-server.jar"
  :main alko-similar-server.server)
