(ns homedash.views.apartments
  (:require [homedash.views.layout :as layout]
            [hiccup.core :refer [h]]
            [hiccup.form :as form]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.local :as l]))

(def good-areas
  ["Frösunda" "Kungshamra" "Lappkärrsberget"
    "Pax" "Strix" "Apeln" "Domus" "Forum"
    "Fyrtalet" "Hugin & Munin" "Idun" "Jerum"
    "Kurland" "Lucidor" "Mjölner" "Nyponet"
    "Roslagstull" "Vätan" "Marieberg" "Tanto" ])

(defn by-floor-move-in-queue-days [x y]
    (compare [(:move_in_date y) (:floor y) (:queue_days x)] ; Descending move in date + floor, ascending queue days
             [(:move_in_date x) (:floor x) (:queue_days y)]))

(defn apartment [apt]
  [:tr {:class
    (if (t/before? (f/parse (f/formatters :basic-date)
        (str "20" (get apt :move_in_date))) (l/local-now)) "negative" "")}
    [:td [:a {:href (get apt :url)} (get apt :address)]]
    [:td (get apt :type)]
    [:td (get apt :surface)]
    [:td (get apt :floor)]
    [:td (get apt :rent)]
    [:td (get apt :queue_days)]
    [:td (get apt :interested)]
    [:td (f/unparse (f/formatters :date)
      (f/parse (f/formatters :basic-date)
        (str "20" (get apt :move_in_date))))]])

(defn display-apartments [apartments]
  [:div {:class "apartments"}
    [:table {:class "ui definition celled right aligned striped selectable table"}
      [:thead
        [:tr
          [:th ]
          [:th "Typ"]
          [:th "Area"]
          [:th "Våning"]
          [:th "Hyra"]
          [:th "Ködagar"]
          [:th "Intressenter"]
          [:th "Inflyttningsdatum"]]
      [:tbody
        (map
          (fn [apt]
            (apartment apt))
            (sort by-floor-move-in-queue-days apartments))]]]])

(defn display-areas [apartments]
  (for [kv (group-by :area apartments)]
    (if (some #(= (first kv) %) good-areas)
      [:div {:class "area"}
        [:div {:class "ui horizontal divider"} [:h2 (first kv)]]
        (display-apartments (last kv))]
      nil)))

(defn index [apartments]
  (layout/common "SSSB"
    (display-areas apartments)))
