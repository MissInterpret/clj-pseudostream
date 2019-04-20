(ns clj-pseudostream.core
  "Internal namespace providing affordances that return file data in 
   a format that is compatible with ring and pedestal requests."
  (:require [me.raynes.fs :as fs]
            [clj-pseudostream.file.core :as f]))

(defn contained? [path root]
  (let [mapped (map #(fs/child-of? % path) root)]
    (every? true? mapped)))

(defn matches? [path {:keys [matches root]}]
  ; Does the path match the regex?
  (let [contained? (contained? path root)
        match? ()]
    (and contained? match?)))

(defn ext? [path exts]
  (contains? exts (fs/extension path)))

(defn allowed? [request config]
  "Does the path of the request match the regex expression and exist?"
  (let [path (f/path request)]
    (and (matches? request config)
         (ext? path (:extensions config))
         (fs/exists? path))))

(defn stream [request {:keys [range? input]}]
  "Handles the progressive download protocol for this file request")

