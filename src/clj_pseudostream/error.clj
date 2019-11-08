(ns clj-pseudostream.error
  (:require [slingshot.slingshot :refer [throw+]]))


(defn throw [message from data]
  (throw+ {:error message :from from :data data}))
