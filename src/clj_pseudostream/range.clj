(ns clj-pseudostream.range)

(defn range? [{:keys [start length] :as range}]
  (and (some? start) (some? length)))

(defn from-request [request])