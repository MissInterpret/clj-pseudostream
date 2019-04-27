(ns clj-pseudostream.ring.middleware.core
  (:require [clj-pseudostream.core :refer [stream format-handler]]
            [clj-pseudostream.matches :refer []])
  (:import java.io.File))
   
(defn wrap-files [app config]
  "Wrap an app such that files that match the configuration are served via 
   progressive download, otherwise proxing the request to the wrapped app."
  (let [allowed? (:allowed-fn config)]
    (fn [request]
      (let [h (format-handler request config)]
        (if (not-nil? h)
          (stream request h)
          (app request))))))
