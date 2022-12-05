(defproject alko-similar-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [dk.ative/docjure "1.19.0"]
                 [clj-time "0.15.2"]
                 [metosin/reitit "0.5.4"]
                 [com.taoensso/timbre "5.1.2"]
                 [mount "0.1.16"]
                 [tolitius/mount-up "0.1.3"]
                 [ring/ring-jetty-adapter "1.9.4"]
                 [aero "1.1.6"]]
  :main ^:skip-aot alko-similar-server.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
