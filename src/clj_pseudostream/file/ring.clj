(ns clj-pseudostream.file.ring
  "Provides efficient access to file resources compatible with ring requests."
  (:require [clj-pseudostream.file.core :as f]
            [clj-pseudostream.request :as r]
            [clojure.java.io :as io]
            [ring.util.io :as ring-io]))

(defn handler [request route range]
  "Returns a map containing:
     :range? - Does the range contain start and length?
     :input  - Either a ring-compatible structure or a File if there is no range"
  (let [request-route (r/request-route request)
        handler (f/handler request-route route range)]
    (if (:range? handler)
      (assoc handler :input (ring-io/piped-input-stream
                             (fn [ostream]
                               (io/copy (:input handler) ostream))))
      handler)))
 
    



