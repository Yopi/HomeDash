(defproject homedash "0.5"
    :description "Scrapes and serves up SSSB apartments"
    :url "http://example.com/FIXME"
    :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
    :dependencies [[org.clojure/clojure "1.8.0"]
                    [org.clojure/java.jdbc "0.6.1"]
                    [org.postgresql/postgresql "9.4-1201-jdbc41"]
                    [cheshire "5.8.0"]
                    [ring/ring-core "1.4.0"]
                    [ring/ring-defaults "0.1.2"]
                    [ring/ring-devel "1.4.0"]
                    [ring/ring-jetty-adapter "1.4.0"]
                    [compojure "1.6.1"]
                    [hiccup "1.0.5"]
                    [clj-time "0.14.4"]]
    :uberjar-name "homedash-standalone.jar"
    :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring-mock "0.1.5"]]}
                :uberjar {:aot :all}}
    :main ^:skip-aot homedash.core)
