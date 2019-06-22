(ns clj-pseudostream.utils
  (:require [cognitect.anomalies :as anomalies]))


(defn anomaly? [data]
  (and (map? data) (contains? data ::anomalies/category)))
