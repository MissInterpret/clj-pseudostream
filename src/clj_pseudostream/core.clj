(ns clj-pseudostream.core
  "Internal namespace providing affordances that return file data in
   a format that is compatible with ring and pedestal requests."
  (:require [cognitect.anomalies :as anomalies]
            [clj-pseudostream.utils :as u]
            [clj-pseudostream.media.protocol :as p]
            [clj-pseudostream.request :as req]))

(defn didnt-match? [stream-response]
  (and (contains? stream-response ::from)
       (not (get-in stream-resp [::access :matches?]))))

(defn error? [stream-response]
  (and (u/anomaly? stream-response)
       (not (contains? stream-response ::from))))


(defn initial-response [source request])


(defn response [{:keys [access-fn source-fn] :as request}]
  "Handles range request or returns an anomaly when:

   o Request is not allowed
   o The source is unable to be loaded

   The following keys are required:

   :access-fn  - Access control
   :source-fn  - Provides a MediaSource appropriate for this request"
  (let [access (access-fn request)]
    (cond
      (u/anomaly? access)            access

      (and (:matches? access)
           (not (:allowed? access))) {::anomalies/category ::anomalies/forbidden
                                      ::anomalies/message "Request Unauthorized"
                                      ::from ::response
                                      ::access access}

      (and (:matches? access)
           (:allowed? access))       (let [source (source-fn request)]
                                       (cond
                                         (u/anomaly? source)  source
                                         (req/range? request) (p/input-stream source request)
                                         :else (initial-response source request))))))
