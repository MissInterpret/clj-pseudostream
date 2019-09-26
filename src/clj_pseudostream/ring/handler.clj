(ns clj-pseudostream.ring.handler
  (:require [clj-pseudostream.utils :as u]
            [clj-pseudostream.handler :as stream]
            [clj-pseudostream..anomalies :as anomalies]
            [ring.util.http-response :as r]))

(defn stream [{:keys [access source] :as request}]
  (let [resp (stream/response request)]
    (cond
      (anomolies/anomoly? resp)
      (r/error  (with-out-str (clojure.pprint/pprint resp)))
      :else resp)))
