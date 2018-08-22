(ns homedash.libs.email-notifications
  (:require [postal.core :refer [send-message]]
            [clojure.string :as str]))

(def email (System/getenv "EMAIL_USER"))
(def password (System/getenv "EMAIL_PASSWORD"))

(def conn {:host "smtp.gmail.com"
           :ssl true
           :user email
           :pass password})

(defn send-notifications
    "Sends out notifications to registered users"
    [apartments emails]
    (for [user emails]
        (send-message conn {:from email
                            :to (get user :email)
                            :subject (str "SSSB | " (count apartments) " nya lägenheter")
                            :body (str "Hej " (get user :name) "!\r\n\r\n"
                                        "SSSB har lagt upp nya lägenheter i följande områden:\r\n"
                                        (str/join ", " (map :area apartments)) "\r\n\r\n"
                                        "Läs mer här: https://homedash-sssb.herokuapp.com\r\n") })))
