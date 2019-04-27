(ns clj-pseudostream.matches
  (:require [clojure.pprint :refer [pprint]]))


(defn matches? [regex request-route {:keys [route root]}])

(def default-expression "")

(defn new 
  ([])
  ([regex]
   (partial matches? regex)))
