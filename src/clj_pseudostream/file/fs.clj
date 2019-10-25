(ns clj-pseudostream.file.fs
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [clj-pseudostream.error :as err]))

(defn error
  [message {:keys [fs-root req-segment] :as route} file]
  (err/throw message ::file {:route route :file (str file)}))


;; File ----------------------------------------------------
;;

(defn file
  "Returns a java.io.File by translating the `route` using the `request`."
  [{:keys [fs-root req-segment]} req-url]
  (let [path (str fs-root (subs req-url (count req-segment)))
        file (io/as-file file-path)]
    (cond
      (not (.exists file))  (error ::does-not-exist route file)
      (not (.canRead file)) (error ::unreadable route file)
      :else file)))
