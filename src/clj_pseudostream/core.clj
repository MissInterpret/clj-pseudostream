(ns clj-pseudostream.core
  "Internal namespace providing affordances that return file data in 
   a format that is compatible with ring and pedestal requests."
  (:require [me.raynes.fs :as fs]
            [ring.util.codec :as codec]
            [clj-pseudostream.route :as r]
            [clj-pseudostream.file.core :as f]))

(defn allowed? [request-route matches-fn route]
  (let [file (file request-route route)]
     (and
       (matches-fn request-route route)
       (fs/exists? file))))
                        
(defn format-handler [request config]
  "Returns the format handler that best matches the route of the 
   request. The most specific route is returned if there is more 
   than one route that matches the request route.
   
   Additionally, if a handler that matches the format of the file
   exists it is preferred."
 (let [allowed-fn (:allowed-fn config)
       matches-fn (:matches-fn config)
       request-route (r/request-route request)
       route (last (filter 
                      true? 
                      (map 
                        #(allowed-fn request-route matches-fn %)
                        (:routes config))))
       handlers (:handlers route)
       ext (r/extension request-route)
       kw-ext (keyword ext)]
  (if (contains? handlers kw-ext)
    (get handlers kw-ext)
    (:default handlers))))

(defn stream [request {:keys [range? input] :as handler}]
  "Handles the progressive download protocol for this file request")

