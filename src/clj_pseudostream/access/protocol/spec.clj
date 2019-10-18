(ns clj-psuedostream.access.spec
  (:require [clojure.spec.alpha :as s]))

(create-ns 'clj-psuedostream.access)
(alias 'access 'clj-pseudostream.access)

(s/def ::access/handle? s/boolean?)
(s/def ::access/allow? s/boolean?)
(s/def ::access/value (s/keys :req [::handler/handle? ::handler/allow?]))
