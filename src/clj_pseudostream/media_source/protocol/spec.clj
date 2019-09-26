(ns clj-pseudostream.media.protocol.spec
  (:require [clojure.alpha.spec :as s]
            [clojure.spec.gen.alpha :as gen]
            [clj-pseudostream.media.protocol :as p]))

(create-ns 'clj-pseudostream.media.protocol)
(alias 'protocol 'clj-pseudostream.media.protocol)

;; Media Source --------------------------------------------
;;
;; Once a request is being handled a MediaSource is responsable
;; for returning the media and its metadata used in the response.
;;

; The MediaSource protocol
(s/def ::protocol/media-source
  (s/spec (partial satisfies? p/MediaSource)
          :gen #(gen/return (p/null-media-source))))
