(ns clj-pseudostream.file.fs-test
  (:require [clojure.test :refer [is deftest testing]]
            [clojure.java.io :as io]
            [clj-pseudostream.file.fs :as fs]
            [slingshot.slingshot :refer [try+]]
            [me.raynes.fs :as rfs]))


(def segment "/")

(def route-map {:fs-root (str (.getAbsolutePath (io/file "")) "/test/resources/")
})

(deftest load-video-file
  (testing "missing file"
    (try+
      (fs/file route-map (str segment "missing-file.mp4"))
      (catch [:error ::fs/does-not-exist] info
        (is true))
      (catch Object _
        (is false))))
  (testing "can't read"
    (let [path (io/as-file (str (:fs-root route-map) "unreadable.mp4"))]
      (try+
        (rfs/chmod "-r" path)
        (fs/file route-map (str segment "unreadable.mp4"))
        (is false)
        (catch [:error ::fs/unreadable] info
          (rfs/chmod "+r" path)
          (is true))
        (catch Object _
          (rfs/chmod "+r" path)
          (is false)))))
  (testing "loaded"
    (let [f (fs/file route-map (str segment "video.mp4"))]
      (is (instance? java.io.File f)))))
