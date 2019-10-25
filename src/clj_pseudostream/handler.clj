(ns clj-pseudostream.handler
  "Internal namespace providing affordances that return file data in
   a format that is compatible with ring requests."
  (:require [ring.util.response :as resp]
            [clj-pseudostream.access :as access]
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


(defn handle
  "Handles range request or returns:

  :clj-pseudostream.handler/unhandled
  :clj-psuedostream.handler/forbidden

   The following keys are required:

   :access-fn  - A fn that matches the ::handler/access spec"
  [{:keys [access-fn] :as request}]
  (let [a (access-fn request)]
    (cond
      (access/ignore? a) ::unhandled
      (access/forbid? a) ::forbidden
      :else
      (if (range? request)
        (stream-response a request)
        (initial-response a request)))))
