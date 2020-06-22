 (defproject halo-api "0.1.0-SNAPSHOT"
   :description "FIXME: write description"
   :dependencies [[org.clojure/clojure "1.10.0"]
                  [metosin/compojure-api "2.0.0-alpha30"]
                  [clj-http "3.10.1"]
                  [org.clojure/data.json "1.0.0"]
                  [com.novemberain/monger "3.1.0"]
                  [buddy "2.0.0"]
                  [ring "1.4.0"]]
   :ring {:handler halo-api.handler/app
          :host "0.0.0.0"
          :port 80}
   :uberjar-name "server.jar"
   :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]
                   :plugins [[lein-ring "0.12.5"]]}})
