(ns clj-pseudostream.media.protocol)

(defprotocol MediaSource
  (duration [this])
  (input-stream [this])
  (time-to-byte [this time]))
