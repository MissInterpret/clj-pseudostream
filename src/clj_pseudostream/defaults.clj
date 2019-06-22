(ns clj-pseudostream.defaults
  (:require [clojure.string :refer [ends-with?]]
            [clj-pseudostream.request :as req]
            [clj-pseudostream.route :as route]
            [me.raynes.fs :as fs]))

(defn allowed? [matches-fn request route]
  (and
    (matches-fn request route)
    (let [path-fn (:path-fn route)]
      (fs/exists? (path-fn request route)))))

(def regex #"\.(.mp4|.MP4)$")

(defn new-matches [regex]
  "Applies the regex to the request route"
  (fn [request route]
    (let [subs (req/sub-route request route)]
      (and (some? subs) (re-matches regex subs)))))

(defn new-allowed [regex]
  (partial allowed? (new-matches regex)))

(defn new-path [fs-root]
  "Uses the base route as the start of an assumed file system
   that directly maps. i.e.

   request: /some/route/with/subdirs/file.mp4
     route: /some/route
      path: fs-root/with/subdirs/file.mp4 "
  (fn [request route]
    (let [subs (req/sub-route request route)
          mnt (if (ends-with? subs "/")
                fs-root
                (str fs-root "/"))]
      (if (nil? subs)
        nil
        (str mnt subs)))))
