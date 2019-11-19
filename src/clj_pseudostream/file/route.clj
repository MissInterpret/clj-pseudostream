(ns clj-pseudostream.file.route
  "Provides a mapping between a request route
   and a file on a presumed file system."
  (:require [clojure.string :as s]
            ))


(defn new-route [root regex]
  {::fs-root root
   ::segment-regex regex})


(defn media-extension [rel-path]
  (let [idx (s/last-index-of rel-path ".")]
    (if (nil? idx)
      nil
      (keyword (subs rel-path (+ idx 1))))))


(defn find-route
  "Returns the appropriate route for the incoming request using the stream-routes
  mapping."
  [stream-routes rel-path]
  ;; Wouldn't loop-recur that short-circuits on first match
  ;; be better?
  (let [route (filter
                (fn [route]
                  (not (nil? (re-find (::segment-regex route) rel-path))))
                stream-routes)]
    (last route)))
