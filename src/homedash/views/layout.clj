(ns homedash.views.layout
  (:require [hiccup.page :as h]
            [clojure.string :as str]))

(defn common [title & body]
  (h/html5
   [:head
    [:meta {:charset "utf-8"}]
    [:link {:rel "stylesheet" :href "https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.3.3/semantic.min.css" :integrity "sha256-ncjDAd2/rm/vaNTqp7Kk96MfSeHACtbiDU9NWKqNuCI" :crossorigin "anonymous" }]
    [:title (str "HomeDash | " title)]
    (h/include-css "/stylesheets/base.css")
   [:body
    [:div {:class "ui container"}
      [:h1 {:class "dividing header"} "HomeDash | SSSB"]
      body]]]))

(defn four-oh-four []
  (common "Page Not Found"
          [:div {:id "four-oh-four"}
           "The page you requested could not be found"]))
