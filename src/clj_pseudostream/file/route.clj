(ns clj-pseudostream.file.route
  "Provides a mapping between a request route
   and a file on a presumed file system."
  (:require [clojure.string :as s]
            ))


(defn new-route [fs-root req-segment]
  {::fs-root fs-root
   ::segment-regex req-segment})


(defn media-extension [rel-path]
  (let [idx (s/last-index-of rel-path ".")]
    (if (nil? idx)
      nil
      (keyword (subs rel-path (+ idx 1))))))


(defn find-route
  "Returns the appropriate route for the incoming request using the stream-routes
  mapping."
  [stream-routes rel-path]
  (let [route (filter
                (fn [route]
                  (re-find (::segment-regex route) rel-path))
                stream-routes)]
    (last route)))
