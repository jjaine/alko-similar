(defproject alko-similar-server "0.1.0-SNAPSHOT"
  :description "API for finding similar Alko products"
  :url "https://alko-similar.dy.fi/docs"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring "1.9.6"]
                 [integrant "0.8.0"]
                 [environ "1.2.0"]
                 [metosin/reitit "0.5.18"]
                 [clj-time "0.15.2"]
                 [dk.ative/docjure "1.19.0"]
                 [ring-cors "0.1.13"]
                 [org.postgresql/postgresql "42.5.4"]
                 [com.github.seancorfield/next.jdbc "1.3.858"]
                 [camel-snake-kebab "0.4.1"]
                 [com.zaxxer/HikariCP "3.4.5"]
                 [clj-http "3.12.3"]]
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :dev {:source-paths ["dev/src"]
                   :resource-paths ["dev/resources"]
                   :dependencies [[ring/ring-mock "0.4.0"]
                                  [integrant/repl "0.3.2"]
                                  [nrepl "1.0.0"]]
                   :plugins [[cider/cider-nrepl "0.28.5"]]}}
  :uberjar-name "alko-similar-server.jar"
  :main alko-similar-server.server)
