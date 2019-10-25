(ns clj-psuedostream.access.spec
  (:require [clojure.spec.alpha :as s]
            [clj-pseudostream.access :as a]))

(create-ns 'clj-psuedostream.access)
(alias 'access 'clj-pseudostream.access)

;; Access ----------------------------------------------------
;;
;; Provides access control for a request.
;;

(s/def ::access/access
  (s/spec (partial satisfies? a/Access)
          :gen #(gen/return (a/null-access))))
