(ns clj-pseudostream.handler.spec
  (:require [clojure.spec.alpha :as s]
            [clj-pseudostream.anomalies.spec :as anomalies]))

(create-ns 'clj-pseudostream.handler)
(alias 'handler 'clj-pseudostream.handler)

;; Access Control --------------------------------------------
;;
;; An implementation can control if the given request
;; should be handled and, if so,  if it is allowed.
;;
;; Returns a map with access states
;;


(s/def ::handler/access-fn
  (s/fspec
    :args map?
    :ret (s/or ::handler/anomoly ::handler/access-value)))


;; Media Source ----------------------------------------------
;;
;; Once a request is being handled a MediaSource is responsable
;; for returning the media's data and metadata used in the response.
;;

(create-ns 'clj-pseudostream.media-source.protocol)
(alias 'protocol 'clj-pseudostream.media-source.protocol)

; Returns an anomoly or an implementation of MediaSource
(s/def ::handler/source-fn
  (s/fspec
    :args map?
    :ret (s/or ::handler/anomoly ::protocol/media-source)))


;; Handler ------------------------------------------------------
;;

(s/def ::handler/access
  (s/fspec
    :args map?
    :ret (s/or ::anomolies/anomoly ::handler/access-fn)))

(s/def ::handler/source
  (s/fspec
    :args map?
    :ret (s/or ::anomolies/anomoly ::handler/source-fn)))

(s/def ::handler/request :req [::handler/access ::handler/source])
