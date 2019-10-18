(ns clj-pseudostream.file.source
  (:require [clj-pseudostream.file.route :as route]
            [clj-pseudostream.anomolies :as anomolies]
            [me.raynes.fs :as fs])
  (:import [java.io RandomAccessFile InputStream OutputStream]))


(defn anomoly [from message file range & throwable]
  (anomolies/throw from message {:file file :range range} throwable))


(defn random-access-file [file {:keys [playhead duration] :as range}]
  (let [raf (RandomAccessFile. path "r")
        length (.length raf)]
    (cond
      (> duration length) (anomoly ::raf ::duration-exceeds-file-length file range)
      (< playhead 0)      (anomoly ::raf ::playhead-negative file range)
      :else (try
              (.seek playhead)
              (catch Throwable t (anomoly raf ::seek-failed file range t))))))


(defrecord FileSource
  "A MediaSource that is backed by a random access file on a file system. This treats
   time as bytes in the file.

   i.e. duration is the total bytes, etc."
  [file]
  p/MediaSource

  (duration [this]
    (fs/size (.getPath file)))

  ; InputStream interface:
  ;
  ; https://docs.oracle.com/javase/9/docs/api/java/io/InputStream.html
  ;
  (input-stream [this {:keys [playhead duration] :as range}]
    (let [path (.getPath file)
          size (fs/size path)
          range-length (+ playhead duration)]
      (if (> range-length size)

        (anomoly ::input-stream ::range-exceeds-file-size file range)

        (try
          (let [raf (random-access-file)]
            (proxy [java.io.InputStream] []

              (available     [] size)
              (close         [] (.close raf))
              (markSupported [] false)
              (read          [] (.read raf (byte-array duration)))
              (readAllBytes  [] (.read raf (byte-array duration)))

              (readNBytes [^bytes bytes off len]
                (cond
                  (> len duration)       (anomaly ::readNBytes ::read-past-range file range)
                  (not (= off playhead)) (anomaly ::readNBytes ::offset-not-playhead file range)
                  :else                  (.read raf bytes)))

              (transferTo [^OutputStream out]
                (.write out (.read raf (byte-array duration))))

              (mark [readlimit] (anomaly ::mark ::unsupported file range))
              (reset []         (anomaly ::reset ::unsupported file range))
              (skip [n]         (anomaly ::skip ::unsupported file range))
              )))
        (catch Throwable t (anomoly ::input-stream ::exception-reading-file file range t)))))

  (time-to-byte [this time]
    ;; TODO -- map proportion of file given duration
    time))


(defn new-media-source [file]
  (FileSource. file))
