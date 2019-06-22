(ns clj-pseudostream.ring.middleware.core
  (:require [clj-pseudostream.core :as stream]))

(defn wrap-stream [app config]
  "Wrap an app such that files that match the configuration are served via
   progressive download, otherwise proxing the request to the wrapped app."
  (fn [request]
    (let [resp (stream/response (merge request config))]
      (cond
        (stream/error? resp)       (r/error (with-out-str (clojure.pprint/pprint resp)))
        (stream/didnt-match? resp) (app request)
        :else                      resp))))
