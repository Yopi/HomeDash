(ns homedash.core
  (:require [ring.adapter.jetty :as ring]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [homedash.views.layout :as layout]
            [homedash.controllers.apartments :as apartments-controller]
            [homedash.models.migration :as migration])
  (:gen-class))

(defroutes routes
  (GET "/" [] (apartments-controller/index))
  (POST "/apartments/update" [] (apartments-controller/update-apartments))
  (route/resources "/")
  (route/not-found (layout/four-oh-four)))

(def reloadable-app
  (wrap-reload #'routes))

(def application (wrap-defaults routes api-defaults))

(defn start [port]
    (ring/run-jetty application {:port port
                                   :join? false}))

(defn -main []
    (migration/migrate) ;; Migrate on boot
    (let [port (Integer. (or (System/getenv "PORT") "8080"))]
        (start port)))
