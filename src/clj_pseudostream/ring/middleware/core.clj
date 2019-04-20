(ns clj-pseudostream.ring.middleware.core
  (:require [clj-pseudostream.file.ring :refer [handle]]
            [clj-pseudostream.core :refer [stream]])
  (:import java.io.File))
   
(defn wrap-files [app config]
  "Wrap an app such that files that match the configuration are served via 
   progressive download, otherwise proxing the request to the wrapped app."
  (let [allowed? (:allowed-fn config)]
    (fn [request]
      (if (allowed? request config)
        (let [handle (handle request)]
          (stream request handle))
        (app request)))))
