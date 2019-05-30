(ns clj-pseudostream.config
  (:require [clj-pseudostream.defaults :as defaults]
            [clj-pseudostream.route :as route]))

(defn routes [mappings]
 (if-not (and (some? mappings) (even? (count mappings)))
   (throw (IllegalArgumentException. "mappings does not contain route-root pairs.")))
 (loop [pairs mappings 
        result []]
   (if (empty? pairs)
     result
     (let [base (first pairs)
           fs (next pairs) 
           data-uri-fn (defaults/new-path fs)
           route (route/new base data-uri-fn)]
       (recur (drop 2 pairs) (conj result route))))))

(defn handlers [server]
  {:default (case server
              :ring 'clj-pseudostream.file.ring
              :pedestal 'clj-pseudostream.file.core)})

(defn new-config 
  [server routes & {:keys [regex allowed-fn] :as opts}]
  (let [rx (if (some? regex)
              regex
              defaults/regex)
        allowed (if (some? allowed-fn) 
                  allowed-fn
                  (defaults/new-allowed rx))]
   {:allowed-fn allowed 
    :routes (new-routes routes)
    :
    :sources (new-handlers server)})) 
     
