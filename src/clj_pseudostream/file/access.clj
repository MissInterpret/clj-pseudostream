(ns clj-pseudostream.file.access
  (:require [ring.util.request :as req]
            [clj-pseudostream.anomolies :as anomolies]
            [clj-pseudostream.access.access :as access]
            [clj-pseudostream.file.fs]
            [clj-pseudostream.file.route :as route]))


(defrecord RouteAccess
  [rel-path route source-new]
  access/Access
  (ignore? [this]
    (or
      (nil? source-new)
      (nil? route)))
  (forbid? [this]
    false)
  (media [this]
    (source-new (fs/file route rel-path))))

(defn new-access
  "Implements the :clj-pseudostream.handler/access-fn spec
   returning a function that maps a request url onto a
   file on the local file system.

   `stream-routes` is a set of routes that conform to the
   :clj-pseudostream.file.route/mapping spec.

  `media-sources` is a map of file extensions by keyword
  (i.e. `{:mp4 ...`) with values that are constructors for
  MediaSource's."
  [stream-routes media-sources]
  (let [default-source (:default media-sources)]
    (fn [request]
      (let [rel-path (req/path-info request)
            route (route/find-route stream-routes rel-path)
            ext (route/media-extension request)]
        (RouteAccess. rel-path route (get media-sources ext default-source))))))
