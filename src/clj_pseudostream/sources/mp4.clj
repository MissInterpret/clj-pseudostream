(ns clj-pseudostream.media.mp4
  (:require [clj-pseudostream.media.file :as file]))

(defrecord Mp4Source [source]
  p/MediaSource
  (duration [this])
  (input-stream [this])
  (time-to-byte [this time]))

(defn new-source [request route]
  (let [])
  ; use data-uri-fn here to generate a file source
  ; 
  ())

  ; can you reify and over-ride ?`


