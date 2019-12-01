(ns clj-pseudostream.media-source)

(defprotocol MediaSource
  (duration [this])
  (input-stream [this range])
  (time-to-byte [this time]))

(defn null-media-source []
  (reify MediaSource
    (duration [this])
    (input-stream [this range])
    (time-to-byte [this time])))
