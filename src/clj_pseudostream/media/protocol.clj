(ns clj-pseudostream.media.protocol)

(defprotocol MediaSource
  (duration [this])
  (input-stream [this request])
  (time-to-byte [this time]))