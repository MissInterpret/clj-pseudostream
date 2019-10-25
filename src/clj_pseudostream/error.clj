(ns clj-pseudostream.error
  (:require [slingshot.slingshot :only [throw+]]))


(defn throw [message from data]
  (throw+ {::error message ::from from ::data data}))
