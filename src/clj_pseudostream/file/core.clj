(ns clj-pseudostream.file.core
  (:require [clojure.java.io :as io]
            [ring.util.codec :as codec]
            [clj-pseudostream.range :refer [range?]]))

(defn path [request]
  (.substring ^String (codec/url-decode (:uri request)) 1))

; Caching strategy for re-use of file handles?
;
;   Weak/Phantom references as a way to leverage 
;   garbage collection that doesn't require 
;   a cleanup thread...?
;

(defn bounded-input-stream [path {:keys [start length] :as range}])

(defn handle [request range]
  (let [range? (range? range) 
        path (path request)
        input (if range?
                (bounded-input-stream path range) 
                (io/file path))]
    {:range? range?
     :input input}))
