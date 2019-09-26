(ns clj-pseudostream.file.route.spec
  (:require [clojure.spec.alpha :as s]))

(create-ns 'clj-pseudostream.file.route)
(alias 'route 'clj-pseudostream.file.route)

(s/def ::route/fs-route string?)
(s/def ::route/req-segment string?)
(s/def ::route/regex any?) ; what type is a regex?

(s/def ::route/mapping (s/keys :req [::route/fs-route ::route/req-segment ::route/regex]))
