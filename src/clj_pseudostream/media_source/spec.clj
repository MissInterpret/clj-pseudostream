(ns clj-pseudostream.media-source.spec
  (:require [clojure.alpha.spec :as s]
            [clojure.spec.gen.alpha :as gen]
            [clj-pseudostream.media-source :as src]))

(create-ns 'clj-pseudostream.media-source)
(alias 'media 'clj-pseudostream.media-source)

;; Media Source --------------------------------------------
;;
;; Once a request is being handled a MediaSource is responsable
;; for returning the media and its metadata used in the response.
;;

; The MediaSource protocol
(s/def ::media/source
  (s/spec (partial satisfies? src/MediaSource)
          :gen #(gen/return (src/null-media-source))))
