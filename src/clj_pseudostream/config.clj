(ns clj-pseudostream.config
  (:require [clj-pseudostream.core :refer [allowed?]]))

(defn new [paths exts regex & allowed-fn]
  ; validate fn 
  ; 
  ; paths and exts  - vector
  ; regex a valid regex expression 
  ; and allowed-fn is a fn 
  ;
  {:matches regex
   :root (into #{} paths)
   :extensions (into #{} exts)
   :allowed-fn (if (some? allowed-fn) allowed-fn allowed?)})

