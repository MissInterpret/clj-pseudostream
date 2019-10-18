(ns clj-pseudostream.media.protocol)

(defprotocol MediaSource
  (duration [this])
  (input-stream [this access request])
  (time-to-byte [this time]))

(defn null-media-source []
  (reify MediaSource
    (duration [this])
    (input-stream [this request])
    (time-to-byte [this])))
