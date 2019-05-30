(ns clj-pseudostream.file.core
  (:require [clojure.java.io :as io]
            [me.raynes.fs :as fs]
            [clj-pseudostream.request :as req]))

; InputStream interface:
;
; https://docs.oracle.com/javase/9/docs/api/java/io/InputStream.html
; 
; * Start with the naive thing and allocate a buffer that matches the request
;   length. Eventually performance tuning may require a managed buffer pool.
;
; * Its unclear how much of the interface is needed and which accessors 
;   ring/pedestal is going to use => Need to write ring and pedestal tests 
;   here that just push a big file over a GET request.  
;   
;   Note that progressive download includes a range request and will 
;   also bound the overall size. 
;

 ; Take an InputStream instead of a file so that 
 ; someone that implementes an object representation of the 
 ; data
(defn bounded-input-stream [input-stream? {:keys [start end] :as range}]
  "Returns an InputStream that conforms to the desired range in bytes. 
   Throws a RuntimeException if the range exceeds the underlying file capacity."
  (let [sz (fs/size file)
        length (- end start)
        capacity (- sz start)]
    (when (> length capacity)
      (throw (RuntimeException. (str "Range exceeds capacity: start=" start " length=" length " size=" sz))))
    (let [buf ()]
      (proxy [java.io.InputStream] []))))

(defn time-to-byte [request route time]
  "Translates a time index into a file offset in bytes.
   This passes-through without any timebase change"
  time)

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

(defn load-input [request route]
  "Returns a data structure that matches the required input 
   for the phase of a request in the psuedo-streaming protocol."
  (let [range (req/range request) 
    ; Maybe load the underlying input-stream or other primative
    ; that is more specific than a file.
    ;
    (bounded-input-stream data-uri range) 


(defn new-source [request route])
  ; Use the data-uri-fn to construct a File that is the 
  ; main param of the implementation