(ns clj-pseudostream.ring.middleware
  (:require [clj-pseudostream.handler :as stream]
            [clj-pseudostream.anomalies :as anomolies]))

(defn wrap-stream [app config]
  "Wrap an app such that files that match the configuration are served via
   progressive download, otherwise proxing the request to the wrapped app."
  (fn [request]
    (let [resp (stream/handle (merge request config))]
      (cond
        (stream/unhandled? resp)  (app request)
        (anomolies/anomoly? resp) (r/error (with-out-str (clojure.pprint/pprint resp)))
        :else                     resp))))
