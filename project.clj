(defproject ring-app "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [ring "1.6.3"]
                 [metosin/ring-http-response "0.9.0"]
                 [metosin/muuntaja "0.6.3"]
                 [metosin/reitit "0.1.3"]]
  :repl-options {:init-ns ring-app.core}
  :plugins [[cider/cider-nrepl "0.22.0-beta4"]]
  :main ring-app.core)
