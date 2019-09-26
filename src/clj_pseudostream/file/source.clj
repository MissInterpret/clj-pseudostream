(ns clj-pseudostream.file.source
  (:require [clj-pseudostream.file.route :as route]
            [clj-pseudostream.anomolies :as anomolies]
            [me.raynes.fs :as fs])
  (:import [java.io RandomAccessFile InputStream OutputStream]))


(defn anomoly [message file range & throwable]
  (anomolies/create ::input-stream message {:file file :range range} throwable))

(defn random-access-file [file {:keys [playhead duration] :as range}]
  (let [raf (RandomAccessFile. path "r")
        length (.length raf)]
    (cond
      (> duration length) (anomoly ::duration-exceeds-file-length file range)
      (< playhead 0) (anomoly ::playhead-negative file range)
      :else (try
              (.seek playhead)
              (catch Throwable t (anomoly ::seek-failed file range t))))))

(defrecord FileSource [file]
  "A MediaSource that is backed by a file on a file system. This treats
   time as bytes in the file.

   i.e. duration is the total bytes, etc."
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
        (anomolies/create ::input-stream ::range-exceeds-file-size)
        (try
          (let [raf (random-access-file)]
            (if (anomolies/anomoly? raf)
              raf
              (proxy [java.io.InputStream] []

                (available     [] size)
                (close         [] (.close raf))
                (markSupported [] false)
                (read          [] (.read raf (byte-array duration)))
                (readAllBytes  [] (.read raf (byte-array duration)))

                (readNBytes [^bytes bytes off len]
                  (cond
                    (> len duration)       (throw (IllegalArgumentException. ::read-past-range))
                    (not (= off playhead)) (throw (IllegalArgumentException. ::offset-not-playhead))
                    :else                  (.read raf bytes)))

                (transferTo [^OutputStream out]
                  (.write out (.read raf (byte-array duration))))

                ; Unimplemented
                ;
                (mark [readlimit] (throw (UnsupportedOperationException. ::mark)))
                (reset [] (throw (UnsupportedOperationException. ::reset)))
                (skip [n] (throw (UnsupportedOperationException. ::skip)))
                ))))
        (catch Throwable t (anomoly ::exception-reading-file file range t)))))

  (time-to-byte [this time]
    time))

(defn create-file-source [file]
   (map->FileSource {::file file}))

(defn source [stream-routes request]
  "Implements the :clj-pseudostream.handler/source spec.

   Supplies a FileSource which operates on a java.io.File.
   It uses the :clj-pseudostream.file/stream-paths spec
   to map a route to a file path on an assumed file system."
  (let [route (route/parse-route stream-routes request)]
    (cond
      (anomolies/anomoly? route) route
      :else
      (create-file-source (route/file request)))))
