(ns clj-pseudostream.ring.middleware.core
  (:require [clj-pseudostream.core :refer [stream stream-input]])
  (:import java.io.File))
   
(defn wrap-stream [app config]
  "Wrap an app such that files that match the configuration are served via 
   progressive download, otherwise proxing the request to the wrapped app."
  (fn [request]
    (let [h (stream-input config request)]
      (if (nil? h)
        (app request)
        (stream request h)))))
