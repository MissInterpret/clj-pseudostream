(ns clj-pseudostream.request
  (:require [clj-pseudostream.media.protocol :as p]))

; Ensure this doesn't include the domain
; Careful to make this compatible with both ring and pedestal
;
(defn route [request])

(defn sub-route [request route]
  "Returns the sub-route from the request if it
   starts with the base route.

   Returns nil if the request is not a sub-route
   of the route."
  (let [request-route (route request)
        base (:base route)
        stripped ()]
    nil))

(defn filename [request]
  (let [r (route request)]))
    ; pull off the last item pulled out by /

(defn extension [request]
  (let [filename (filename request)]
    ; pull off the file extension
   (clojure.string/lower-case)))

(defn range [request route source]
  "Returns and range information found either in headers or in params"
  ; Always return bytes, use fn to do timebase translation
  (let [start ()
        end ()]
    {:start  (p/time-to-byte source start)
     :end (p/time-to-byte source end)}))
