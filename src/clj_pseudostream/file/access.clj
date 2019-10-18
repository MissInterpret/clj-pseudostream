(ns clj-pseudostream.file.access
  (:require [clj-pseudostream.anomolies :as anomolies]
            [clj-pseudostream.access.protocol :as p]
            [clj-pseudostream.file.fs]
            [clj-pseudostream.file.route :as route]
            [clj-pseudostream.file.source :as src]))


(defrecord RouteAccess
  [route req-url]
  p/Access
  (ignore? [this]
    (nil? (re-matches (:regex route) req-url)))
  (forbid? [this]
    false)
  (media [this]
    (src/new-media-source (fs/file route req-url))))


(defn new-access
  "Implements the :clj-pseudostream.handler/access-fn spec
   returning a function that maps a request url onto a
   file on the local file system.

   `stream-routes` is a set of routes that conform to the
  :clj-pseudostream.file.access.spec/stream-routes spec."
  [stream-routes]
  {:pre (s/valid? ::stream-routes stream-routes)}
  (fn [request]
    (let [route (route/parse-route stream-routes request)]
      (cond
        (anomalies/anomaly? route) route
        :else
        (RouteAccess. route (req/request-url request))))))
