(ns clj-pseudostream.access)

(defprotocol Access
  (ignore? [this])
  (forbid? [this])
  (media [this]))

(defn null-access []
  (reify Access
    (ignore? [this])
    (forbid? [this])
    (media [this])))
