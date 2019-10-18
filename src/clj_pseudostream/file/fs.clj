(ns clj-pseudostream.file.fs
  (:require [me.raynes.fs :as fs]
            [clojure.string :as s]
            [ring.util.request :as req]
            [clj-pseudostream.anomolies :as anomolies]))

;; TODO: Convert this to throw+ version
(defn anomoly
  ([message route & throwable]
   (file-anomoly message route nil throwable))
  ([message {:keys [fs-root req-segment] :as route} file-path & throwable]
   (cond-> (anomolies/create ::fs message {} throwable)
     route (assoc-in
             [::anomolies/data :file-info]
             {:fs-root fs-root
              :req-segment req-segment})
     file-path (assoc-in [::anomolies/data :file-info :path] file-path))))


;; Path ----------------------------------------------------
;;

(defn file-path
  "Constructs an absolute file path from the route using
   the `fs-root` and the `req-segment`."
  [{:keys [fs-root req-segment] :as route} req-url]
  (let [idx (s/index-of req-url req-segment)]
    (if (> idx 0)
      (->> (subs req-url idx)
           (str fs-root "/"))
      (anomoly ::cant-find-segment route))))



;; File ----------------------------------------------------
;;


(defn file
  "Returns a java.io.File by translating the `route` using the `request`."
  [route req-url]
  (let [path (file-path route request)
        file (fs/file file-path)]
    (cond
      (not (fs/exists? file)) (anomoly ::does-not-exist route file-path)
      (not (.canRead file))   (anomoly ::unreadable route file-path)
      :else file)))
