(ns clj-pseudostream.file.core
  (:require [clojure.java.io :as io]
            [clj-pseudostream.request :as r]
            [clj-pseudostream.range :refer [range?]]))

; Caching strategy for re-use of file handles?
;
;   Weak/Phantom references as a way to leverage 
;   garbage collection that doesn't require 
;   a cleanup thread...?
;

(defn bounded-input-stream [path {:keys [start length] :as range}])

;(defn bounded-output-stream
;"Wraps an OutputStream with a check thats throws a RuntimeException
;when more than capacity bytes have been written"
;[^java.io.OutputStream os capacity]
;(let [n (volatile! 0)
      ;check! #(when (> (vswap! n + %) capacity)
                ;(throw (RuntimeException. "over capacity"))
  ;(proxy [java.io.OutputStream] []
    ;(flush [] (.flush os))
;    (close [] (.close os))
;    (write
;      ([bs]
;       (if (instance? Integer bs)
;         (do (check! 1)
;             (.write os ^Integer bs)
;         (do (check! (alength ^bytes bs))
;             (.write os ^bytes bs)
;      ([bs off len]
;       (check! len)
;       (.write os bs off len))



(defn handler [request route range]
  "Returns a handler"
  (let [range? (range? range) 
        request-route (r/request-route request)
        path (r/file-path request-route route)
        input (if range?
                (bounded-input-stream path range) 
                (io/file path))]
   {:range? range?
    :input input}))
