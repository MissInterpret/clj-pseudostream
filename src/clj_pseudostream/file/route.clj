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

(defn parse-anomoly [message & data]
  (anomolies/create ::route ::anomolies/unhandled message data))

(defn parse-route [stream-routes request]
  "Creates a route from the incoming request using the stream-routes mapping."
  (let [url (req/request-url request)
        route (filter
                (fn [route]
                  (s/starts-with? url (::req-segment route)))
                stream-routes)]
    (cond
      (= 0 (count route)) (parse-anomoly ::route-not-found)
      (> 1 (count route)) (parse-anomoly ::too-many-routes route)
      :else (first route))))



;; Access Value ------------------------------------------------
;;

(defn access-value [route request]
  "Returns an clj-pseudostream/access data structure when the file exists and is readable or an anomoly.

   NOTE: handle? is always true."
  (let [file (file route request)
        readable? (.canRead file)]
    (access/new-access true readable?)))
