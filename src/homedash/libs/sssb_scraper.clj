(ns homedash.libs.sssb-scraper
  (:require [homedash.models.apartment :as model]
            [cheshire.core :refer :all]
            [clojure.string :as str]))

;; Scraper stuff
(def sssb-index
    "https://www.sssb.se/soka-bostad/sok-ledigt/lediga-bostader/lediga-lagenheter-lista/?omraden=&objektTyper=BOASL&hyraMax=&actionId=&paginationantal=all")

(def sssb-list
    "https://www.sssb.se/widgets/?omraden=&objektTyper=BOASL&hyraMax=&actionId=&paginationantal=all&callback=j&widgets%5B%5D=objektlista%40lagenheter")

(def sssb-list-all
    "https://www.sssb.se/widgets/?omraden=&objektTyper=&hyraMax=&actionId=&paginationantal=all&callback=j&widgets%5B%5D=objektlista%40lagenheter")

(def good-areas
  ["Frösunda" "Kungshamra" "Pax" "Strix" "Apeln"
    "Domus" "Forum" "Fyrtalet" "Hugin & Munin"
    "Idun" "Jerum" "Kurland" "Lucidor" "Mjölner"
    "Nyponet" "Roslagstull" "Vätan" "Marieberg" "Tanto" ])

(defn cut-jquery-callback
    "Cuts away the jQuery callback function from JSON response"
    [json]
    (subs json 2 (- (count json) 2)))

(defn scrape-sssb
    "Fetches and returns the current list of apartments out for applying"
    []
    (get
        (get
            (get (parse-string (cut-jquery-callback (slurp sssb-list)) true) :data)
        (keyword "objektlista@lagenheter"))
    :all))

(defn convert-keys
    "Convert keys from JSON to the apartment model"
    [apartment]
    {
        :id (get apartment :objektNr)
        :address (get apartment :adress)
        :area (get apartment :omrade)
        :rent (Integer.(get apartment :hyra))
        :type (get apartment :typ)
        :surface (get apartment :yta)
        :move_in_date (Integer.(get apartment :inflyttningDatum))
        :elevator (if (= (get apartment :hiss) "Ja") true false)
        :floor (Integer.(if(or (= (get apartment :vaning) "BV") (= (get apartment :vaning) "SU")) 0 (get apartment :vaning)))
        :queue_days (Integer.(first (re-seq #"\d+" (get apartment :antalIntresse))))
        :interested (Integer.(last (re-seq #"\d+" (get apartment :antalIntresse))))
        :url (get apartment :detaljUrl)
        :map_url (get apartment :kartURL)
    })

(defn get-apartments
    "Get list of current apartments"
    []
    (map convert-keys (scrape-sssb)))

(defn update-apartments
    "Updates list of apartments in database"
    []
    (filter (fn [x] (= (type x) clojure.lang.LazySeq))
      (map
        (fn [apt] (model/update-or-insert! apt ["id = ?" (get apt :id)]))
        (filter (fn [x] (some #(= (get x :area) %) good-areas)) (get-apartments)))))
