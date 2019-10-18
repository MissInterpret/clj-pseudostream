(ns clj-pseudostream.handler
  "Internal namespace providing affordances that return file data in
   a format that is compatible with ring requests."
  (:require [clj-pseudostream..anomalies :as anomalies]
            [clj-pseudostream.access.protocol :as access]
            [clj-pseudostream.handler.spec]
            ))

;; Range Response Implementation -----------------------------------
;;

(defn range? [request]
  false)

(defn initial-response
  [access request]
  ; return a ring response
  )

(defn stream-response
  [access request]
  ; return a ring response
  )


;; Request Handler --------------------------------------------------
;;

(defn unhandled? [request]
  (and (anomolies/anomaly? request)
       (= (::anomolies/category request) ::anomolies/unhandled)))

(defn handle
  "Handles range request or returns an anomaly when:

   o Request is not handled
   o Request is not allowed
   o error

   The following keys are required:

   :access-fn  - A fn that matches the ::handler/access spec"
  [{:keys [access-fn] :as request}]
  (let [a (access-fn request)]
    (cond
      (anomolies/anomaly? a) a
      (access/ignore? a)     (anomolies/create ::handler ::anomolies/unhandled)
      (access/forbid? a)     (anomolies/create ::handler ::anomolies/forbidden)
      :else
      (if (range? request)
        (stream-response a request)
        (initial-response a request)))))
