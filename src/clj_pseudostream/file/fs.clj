(ns clj-pseudostream.file.fs
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [clj-pseudostream.error :as err]))

(defn error
  [message route file]
  (err/throw message ::file {:route route :file (str file)}))

(defn join-paths [fs-root req-url]
  (let [root (if (s/ends-with? fs-root "/")
               fs-root
               (str fs-root "/"))]
    (if (s/starts-with? req-url "/")
      (str fs-root (subs req-url 1))
      (str fs-root req-url))))

(defn file
  "Returns a java.io.File by translating the `route` using the `request`."
  [{:keys [fs-root] :as route} req-url]
  (let [path (join-paths fs-root req-url)
        file (io/as-file path)]
    (cond
      (not (.exists file))  (error ::does-not-exist route file)
      (not (.canRead file)) (error ::unreadable route file)
      :else file)))
