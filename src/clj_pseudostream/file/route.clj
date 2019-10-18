(ns clj-pseudostream.file.route
  "Provides a mapping between a request route
   and a file on a presumed file system."
  (:require [clojure.string :as s]
            [ring.util.request :as req]
            [clj-pseudostream.fs :as fs]
            [clj-pseudostream.access :as access]
            [clj-pseudostream.anomolies :as anomolies]))


(defn new-route [fs-root req-segment regex]
  {::fs-root fs-root
   ::req-segment req-segment
   ::regex regex})


;; Parsing -----------------------------------------------------
;;

(defn anomoly [message & data] ;; TODO: Convert to throw+
  (anomolies/create ::route ::anomolies/unhandled message data))


(defn parse-route
  "Creates a route from the incoming request using the stream-routes mapping."
  [stream-routes request]
  (let [url (req/request-url request)
        route (filter
                (fn [route]
                  (s/starts-with? url (::req-segment route)))
                stream-routes)]
    (cond
      (= 0 (count route)) (anomoly ::route-not-found)
      (> 1 (count route)) (anomoly ::too-many-routes route)
      :else (first route))))
