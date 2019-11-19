(ns clj-pseudostream.file.route.spec
  (:require [clojure.spec.alpha :as s]))

(create-ns 'clj-pseudostream.file.route)
(alias 'route 'clj-pseudostream.file.route)

(s/def ::route/fs-route string?)
(s/def ::route/segment-regex regex?)

(s/def ::route/mapping (s/keys :req [::route/fs-route ::route/segment-regex]))
