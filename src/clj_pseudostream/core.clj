(ns clj-pseudostream.core
  "Internal namespace providing affordances that return file data in
   a format that is compatible with ring and pedestal requests."
  (:require [clj-pseudostream.request :as req]))

(defn stream-input [config request]
  "Returns the input handle that best matches the route of the
   request.

    Once an allowed route is determined,
    the format handler that best matches the extension
    of the requested file is used to load it.

    If the route is not matched then nil is returned."
 (let [allowed-fn (:allowed-fn config)
       route (last (filter
                      true?
                      (map
                        #(allowed-fn request %)
                        (:routes config))))]
   (if (nil? route)
     nil
     (let [ext (keyword (req/extension request))
           sources (:sources config)
           source-ctr (if (contains? sources ext)
                        (get sources ext)
                        (:default sources))
           src (source-ctr request route)
           range? (req/range request route src)]
       {:range? range?
        :source src}))))

(defn stream [request {:keys [range? target] :as handler}]
  "Handles the progressive download protocol for this file request")
; This is mostly blurting back the right headers to indicate support
; for the (psuedo) protocol...
