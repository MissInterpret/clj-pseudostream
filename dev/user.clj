(ns user
  (:require [clojure.java.io :as io]
            [clj-pseudostream.file.route :as r]))


(defn write-byte-blocks [num-blocks bytes-per-block path]
  (let [array (byte-array (* num-blocks bytes-per-block))
        file (io/as-file path)]
    (when (.exists file)
      (.delete file))
    (.createNewFile file)
    (doseq [block (range num-blocks)]
      (let [start (* block bytes-per-block)
            end (- (* (+ block 1) bytes-per-block) 1)]
        (java.util.Arrays/fill array start end (byte block))))
    (with-open [ofile (java.io.FileOutputStream. file)]
      (println "ofile=" ofile)
      (.write ofile array)
      )
    )
  )

(defn check-byte-blocks [num-blocks byte-per-block path]


  )
