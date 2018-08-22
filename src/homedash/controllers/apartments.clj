(ns homedash.controllers.apartments
  (:require [clojure.string :as str]
            [ring.util.response :as ring]
            [homedash.views.apartments :as view]
            [homedash.models.apartment :as model]
            [homedash.libs.sssb-scraper :as scraper]))

(defn index []
  (view/index (model/all)))

(defn update-apartments []
  (let [apartments (scraper/update-apartments)]
    (ring/response (str "Updated " (count apartments) " apartments\r\n"))))
