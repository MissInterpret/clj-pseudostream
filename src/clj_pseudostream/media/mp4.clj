(ns clj-pseudostream.media.mp4
  (:require [clj-pseudostream.media.protocol :as p]
   [clj-pseudostream.media.file :as file]))

(defrecord Mp4Source [source]
  p/MediaSource
  (duration [this])
  (input-stream [this])
  (time-to-byte [this time]))
