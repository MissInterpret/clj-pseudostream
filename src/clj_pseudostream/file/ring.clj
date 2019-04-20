(ns clj-pseudostream.file.ring
  "Provides efficient access to file resources compatible with ring requests."
  (:require [clj-pseudostream.file.core :as f]
            [clojure.java.io :as io]
            [ring.util.io :as ring-io]))

(defn handle [request range]
  "Returns a map containing:
     :range? - Does the range contain start and length?
     :input  - Either a ring-compatible structure or a File if there is no range"
  (let [handle (f/handle request range)]
    (if (:range? handle)
      (assoc handle :input (ring-io/piped-input-stream
                             (fn [ostream]
                               (io/copy (:input handle) ostream))))
      handle)))
 
    



