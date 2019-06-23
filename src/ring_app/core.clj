(ns ring-app.core
  (:require [reitit.ring :as reitit]
            [ring.adapter.jetty :as jetty]
            [ring.util.http-response :as response]
            [ring.middleware.reload :refer [wrap-reload]]
            [muuntaja.middleware :refer [wrap-format]]))

(defn json-handler [request]
  ;;   $ curl -H "Content-Type: application/json" -H "Accept: application/json" -X POST -d '{"id": 1}' localhost:3000/json
  ;; {"result":1}
  (response/ok
   {:result (-> request :body-params :id)}))

(defn root-handler [request]
  ;; $ curl -X GET localhost:3000
  ;; <html><body>your IP is: 0:0:0:0:0:0:0:1</body></html>
  (response/ok
   (str "<html><body>your IP is: "
        (:remote-addr request)
        "</body></html>")))

(defn hello-handler [request]
  ;; $ curl -X GET localhost:3000/hello/coco
  ;; <html><body>hello coco</body></html>
  (response/ok
   (str "<html><body>hello "
        (get-in request [:path-params :value])
        "</body></html>")))

(defn api-multiply-handler [{params :body-params}]
  (let [{:keys [a b]} params]
    (response/ok {:result (* a b)})))

(def routes
  [["/" {:get root-handler}]
   ["/json" {:post json-handler}]
   ["/api" {:middleware [wrap-format]}
    ["/multiply" {:post api-multiply-handler}]]
   ["/hello/:value" {:get hello-handler}]])

(def handler
  (reitit/ring-handler
   (reitit/router routes)))

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
   (-> #'handler wrap-nocache wrap-reload)
   {:port 3000
    :join? false}))
