(ns clj-pseudostream.file.configure
  (:require [clj-pseudostream.file.access :as a]
            [clj-pseudostream.file.source :as s]))

(defn add-route [stream-path {:keys [root base regex] :as args}]
  (let [route (route/new-route root base regex)]
    (conj stream-path route)))

(defn new-access [stream-routes]
  (partial stream-routes a/access))
