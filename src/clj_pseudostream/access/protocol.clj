(ns clj-pseudostream.access.protocol)

(defprotocol Access
  (ignore? [this])
  (forbid? [this])
  (media [this]))

(defn null-access []
  (reify Access
    (ignore? [this])
    (forbid? [this])
    (media [this])))
