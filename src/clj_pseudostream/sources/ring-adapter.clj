(ns clj-pseudostream.file.ring-adapter
  "Provides efficient access to file resources compatible with ring requests."
  (:require [clj-pseudostream.file.core :as f]
            [clj-pseudostream.request :as req]
            [clojure.java.io :as io]
            [ring.util.io :as ring-io]))

(defn input-stream [request route]
  "Returns a map containing:
     :range? - Does the range contain start and length?
     :target  - Either a ring-compatible input stream or a File if there is no range"
  (let [input (f/load-input request route)]
    (if (:range? input)
      (assoc input :target (ring-io/piped-input-stream
                             (fn [ostream]
                               (io/copy (:target input) ostream))))
      input)))
    
(defn time-to-byte [request route time]
  (f/time-to-byte request route time))


(defn new-source [source request route])
