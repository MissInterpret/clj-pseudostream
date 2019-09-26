(ns clj-pseudostream.media.ring-adapter
  "Provides efficient access to file resources compatible with ring requests."
  (:require

               [ring.util.io :as ring-io]
            [clj-pseudostream.media.protocol :as p]))


(defrecord RingFileSource [source]
 
  p/MediaSource
  (duration [this])
  (input-stream [this])
  (time-to-byte [this time]))


(defn new-source [])

#_(defn input-stream [request route]
  "Returns a map containing:
     :range? - Does the range contain start and length?
     :target  - Either a ring-compatible input stream or a File if there is no range"
  (let [input (f/load-input request route)]
    (if (:range? input)
      (assoc input :target (ring-io/piped-input-stream
                             (fn [ostream]
                               (io/copy (:target input) ostream))))
      input)))

#_(defn time-to-byte [request route time]
  (f/time-to-byte request route time))


#_(defn new-source [source request route])
