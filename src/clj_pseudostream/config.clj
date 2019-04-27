(ns clj-pseudostream.config
  (:require [clj-pseudostream.core :refer [allowed?]]
            [clj-pseudostream.matches :as matches]
            [clj-pseudostream.route :as route]
            [clj-pseudostream.file.core :as f]
            [clj-pseudostream.file.ring :as r]))
            
;; Server Types ---------------------------------
;;
(def ring :ring)
(def pedestal :pedestal)
         
(defn new-routes 
  ([server root]
   [(route/new server "/" root)])
  ([server route root]
   [(route/new server route root)])
  ([server route root & mappings]
   (if-not (and (some? mappings) (even? (count mappings)))
     (throw (IllegalArgumentException. "mappings does not contain route-root pairs.")))
   (let [path-pairs (if (some? mappings)
                      (into [route root] mappings)
                      [route root])]
     (loop [pairs path-pairs
            result []]
       (if (empty? pairs)
         result
         (let [route (first pairs)
               root (next pairs) 
               path (r/route server route root)]
           (recur (drop 2 pairs) (conj result path))))))))

(defn new-config [routes server & {:keys [matches-fn* allowed-fn*] :as opts}]
  {:routes routes
   :matches-fn (if (some? matches-fn) matches-fn* (matches/new))
   :allowed-fn (if (some? allowed-fn) allowed-fn* allowed?)})

