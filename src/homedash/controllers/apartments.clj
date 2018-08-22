(ns homedash.controllers.apartments
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [homedash.views.apartments :as view]
            [homedash.models.apartment :as apartment]
            [homedash.models.user :as user]
            [homedash.libs.sssb-scraper :as scraper]
            [homedash.libs.email-notifications :as notifications]))

(defn index []
  (view/index (apartment/all)))

(defn update-apartments []
  (let [apartments (scraper/update-apartments)]
    (notifications/send-notifications apartments (user/all))
    (ring/response (str "Updated " (count apartments) " apartments\r\n"))))
