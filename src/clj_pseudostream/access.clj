(ns clj-pseudostream.access
  (:require [clj-pseudostream.access.spec :as access]))



(defn ignore? [{:keys [handle?] :as access}]
  (not handle?))

(defn forbid? [{:keys [handle? allow?] :as access}]
  (and handle? (not allow?)))

(defn new-value [handle? allow?]
  {:post (s/valid? ::access/value %)}
  {::handle? handle?
   ::allow? allow?})
