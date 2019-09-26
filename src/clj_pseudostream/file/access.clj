(ns clj-pseudostream.file.access
  (:require [clj-pseudostream.anomolies :as anomolies]
            [clj-pseudostream.access.spec :as access]
            [clj-pseudostream.file.spec]
            [clj-pseudostream.file.route :as route]))

(defn ignore? [route request]
  (nil? (re-matches (::route/regex route) (req/request-url request))))

(defn access [stream-routes request]
  {:pre (s/valid? ::stream-routes stream-routes)}
  "Implements the :clj-pseudostream.handler/access spec
   returning a value described by the  :clj-pseudostream.handler/access-value spec.

   Presumes a filesystem local to the execution context.

   The value of the :clj-pseudostream.file/stream-routes parameter
   is a set of routes that conform to the :clj-pseudostream.file.access.spec/stream-routes spec."
  (let [route (route/parse-route stream-routes request)]
    (cond
      (anomolies/anomaly? route) route
      (ignore? route request)    (anomolies/create ::access
                                                   ::anomolies/unhandled
                                                   ::unmatched-regex)
      :else (route/access-value route request))))


(defn new-stream-routes [route-mappings]
  {::stream-routes (into #{} route-mappings)})
