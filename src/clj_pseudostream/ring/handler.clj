(ns clj-pseudostream.ring.handler
  (:require [clj-pseudostream.utils :as u]
            [clj-pseudostream.core :as stream]
            [ring.util.http-response :as r]))

(defn stream [{:keys [access-fn source-fn] :as request}]
  (let [resp (stream/response request)]
    (cond
      (error? resp) (r/error  (with-out-str (clojure.pprint/pprint resp)))
      (contains? resp ::stream/from) (r/unauthorized)
      :else resp)))
