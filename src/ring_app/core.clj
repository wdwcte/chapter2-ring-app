(ns ring-app.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.http-response :as response]
            [ring.middleware.reload :refer [wrap-reload]]
            [muuntaja.middleware :refer [wrap-format]]))

(defn handler [request]
  (response/ok
   {:result (-> request :body-params :id)}))

(defn wrap-nocache [handler]
  ;; we return a handler. A handler takes a request, modifies it, and
  ;; return it
  (fn [request]
    (-> request
        handler ;; we call the previous handler
        (assoc-in [:headers "Pragma"] "no-cache") ;; we do our own
                                                  ;; modifications to
                                                  ;; whatever the
                                                  ;; previous handler
                                                  ;; returned
        )))

(defn -main []
  (jetty/run-jetty
   ;; create our handler from our middlewares. Note that we use a
   ;; var (the macro reader #'foo expands to `(var foo)`, but I don't
   ;; really understand why.
   (-> #'handler wrap-nocache wrap-format wrap-reload)
   {:port 3000
    :join? false}))
