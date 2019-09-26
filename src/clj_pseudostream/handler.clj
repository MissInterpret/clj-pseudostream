(ns clj-pseudostream.handler
  "Internal namespace providing affordances that return file data in
   a format that is compatible with ring requests."
  (:require [clj-pseudostream..anomalies :as anomalies]
            [clj-pseudostream.access :as access]
            [clj-pseudostream.handler.spec]
            ))

;; Range Response Implementation -----------------------------------
;;

(defn range? [request]
  false)

(defn initial-response [source request]
  ; return a ring response or an anomoly
  )

(defn stream-response [source request]
  ; return a ring response or an anomoly
  )


;; Request Handler ------x-------------------------------------------
;;

(defn unhandled? [request]
  (and (anomolies/anomaly? request)
       (= (::anomolies/category request) ::anomolies/unhandled)))

(defn handle [{:keys [access source] :as request}]
  {:pre (s/valid? ::handler/request)}
  "Handles range request or returns an anomaly when:

   o Request is not handled
   o Request is not allowed
   o The source is unable to be loaded
   o Implementation-specific anomalies

   The following keys are required:

   :clj-psuedostream.core/access  - A fn that matches the ::handler/access-fn spec
   :clj-psuedostream.core/source  - A fn that matches the ::handler/source-fn spec"
  (let [a (access request)]
    (cond
      (anomolies/anomaly? a) a
      (access/ignore? a)     (anomolies/create ::handler ::anomolies/unhandled)
      (access/forbid? a)     (anomolies/create ::handler ::anomolies/forbidden)
      :else (let [src (source request)]
              (cond
                (anomolies/anomaly? src) src
                (req/range? request)     (stream-response src request)
                :else                    (initial-response src request))))))
