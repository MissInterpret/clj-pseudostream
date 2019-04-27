(ns clj-pseudostream.request)

; Ensure this doesn't include the domain 
; Careful to make this compatible with both ring and pedestal
;
(defn request-route [request])

(defn filename [request-route])

(defn extension [request-route]
  (let [filename (filename request-route)]
   (clojure.string/lower-case)))

(defn file-path [request-route {:keys [route root]}])

