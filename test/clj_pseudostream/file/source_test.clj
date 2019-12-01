(ns clj-pseudostream.file.source-test
  (:require [clojure.test :refer [is deftest testing]]
            [clojure.java.io :as io]
            [clj-pseudostream.file.source :as src]
            [slingshot.slingshot :refer [try+]]
            [me.raynes.fs :as rfs]))

(def video (io/as-file (str (.getAbsolutePath (io/file "")) "/test/resources/video.mp4")))


(deftest raf
  (testing "Range - duration g.t. length"
    (try+
      (src/random-access-file video {:playhead 0
                                     :duration (+ (.length video) 10)})
      (catch [:error ::src/duration-exceeds-file-length] info
        (is true))
      (catch Object _
        (is false))))
  (testing "Range - Playhead negative"
    (try+
      (src/random-access-file video {:playhead -1
                                     :duration (- (.length video) 10)})
      (catch [:error ::src/playhead-negative] info
        (is true))
      (catch Object _
        (is false))))
  (testing "Range - playhead g.t. length"
    (try+
      (src/random-access-file video {:duration 0
                                     :playhead (+ (.length video) 10)})
      (catch [:error ::src/playhead-exceeds-file-length] info
        (is true))
      (catch Object _
        (is false))))
  (testing "Range - duration negative"
    (try+
      (src/random-access-file video {:duration -1
                                     :playhead (- (.length video) 10)})
      (catch [:error ::src/duration-negative] info
        (is true))
      (catch Object _
        (is false))))

  (testing "Seek success"
    (let [raf (src/random-access-file video {:duration 100
                                             :playhead 100})]
      (is (= 100 (.getFilePointer raf))))))


(deftest file-source
  (let []
    (testing "Duration"
      (is false)
      )
    (testing "Time to Byte"
      (is false)
      )
    (testing "InputStream"
      (is false)
      )
    ))
