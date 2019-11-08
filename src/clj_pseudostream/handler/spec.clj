(ns clj-pseudostream.handler.spec
  (:require [clojure.spec.alpha :as s]
            [clj-psuedostream.access.spec :as access]
            [clj-pseudostream.media-source.spec :as media]))


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
    :ret (s/or keyword? access/access)))


;; Handler ------------------------------------------------------
;;

(create-ns 'clj-pseudostream.handler)
(alias 'handler 'clj-pseudostream.handler)

(s/def ::handler/request (s/keys :req [::handler/access-fn]))
