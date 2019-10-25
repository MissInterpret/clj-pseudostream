(ns clj-pseudostream.file.route
  "Provides a mapping between a request route
   and a file on a presumed file system."
  (:require [clojure.string :as s]
            [ring.util.request :as req]
            ;[clj-pseudostream.anomolies :as anomolies]
            ))


(defn new-route [fs-root req-segment regex]
  {::fs-root fs-root
   ::req-segment req-segment
   ::regex regex})


(defn media-extension [rel-path]
  (let [idx (s/last-index-of rel-path ".")]
    (if (nil? idx)
      nil
      (keyword (subs rel-path (+ idx 1))))))


(defn find-route
  "Creates a route from the incoming request using the stream-routes
  mapping. If multiple routes are matched the last one in returned."
  [stream-routes rel-path]
  (let [route (filter
                (fn [route]
                  (s/starts-with? rel-path (::req-segment route)))
                stream-routes)]
    (last route)))
