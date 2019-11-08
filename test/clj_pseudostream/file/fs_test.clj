(ns clj-pseudostream.file.fs-test
  (:require [clojure.test :refer [is deftest testing]]
            [clojure.java.io :as io]
            [clj-pseudostream.file.fs :as fs]
            [slingshot.slingshot :refer [try+]]
            [me.raynes.fs :as rfs]))


(def segment "/some/relative/path/")

(def valid-file "video.mp4")

(def route-map {:fs-root (str (.getAbsolutePath (io/file "")) "/test/resources/")
                :req-segment segment})

(deftest load-video-file
  (testing "missing file"
    (try+
      (fs/file route-map (str segment "missing-file.mp4"))
      (catch [:error ::fs/does-not-exist] info
        (is true))
      (catch Object _
        (is false))))
  (testing "can't read"
    (try+
      (let [path ()])
      (rfs/chmod )
      (fs/file route-map (str segment "unreadable.mp4"))
      (is false)
      (catch [:error ::fs/unreadable] info
        (is true))
      (catch Object _
        (is false))))
  (testing "loaded"
    (let [f (fs/file route-map (str segment "video.mp4"))]
      (is (instance? java.io.File f)))))
