(ns clj-pseudostream.file.access.spec
  (:require [clojure.spec.alpha :as s]
            [clj-pseudostream.file.route.spec :as route]))

(create-ns 'clj-pseudostream.file.access)
(alias 'access 'clj-pseudostream.file.access)

(s/def ::access/stream-routes (s/coll-of? ::route/mapping))
