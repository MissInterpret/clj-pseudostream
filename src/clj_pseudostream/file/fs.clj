(ns clj-pseudostream.file.fs
  (:require [me.raynes.fs :as fs]
            [clojure.string :as s]
            [ring.util.request :as req]
            [clj-pseudostream.anomolies :as anomolies]))

(defn file-anomoly
  ([message route & throwable]
   (file-anomoly message route nil throwable))
  ([message {:keys [fs-root req-segment] :as route} file-path & throwable]
   (cond-> (anomolies/create ::fs message {} throwable)
     route (assoc-in
             [::anomolies/data :file-info]
             {:fs-root fs-root
              :req-segment req-segment})
     file-path (assoc-in [::anomolies/data :file-into :path] file-path))))


;; Path ----------------------------------------------------
;;

(defn path [{:keys [fs-root req-segment] :as route} request]
  (let [url (req/request-url request)
        idx (s/index-of url req-segment)]
    (if (> idx 0)
      (->> (subs url idx)
           (str fs-root "/") ; / or not?
           )
      (file-anomoly ::cant-find-segment route))))



;; File ----------------------------------------------------
;;


(defn file [route request]
  "Returns a java.io.File by translating the fs-root and req-segment
   into a file path or an anomoly."
  (try
    (let [path (file-path route request)
          file (fs/file file-path)]
      (if (fs/exists? file)
        file
        (file-anomoly ::does-not-exist route file-path)))
    (catch Throwable throwable
      (file-anomoly ::read-exception route file-route throwable))))
