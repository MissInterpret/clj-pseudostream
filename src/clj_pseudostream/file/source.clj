(ns clj-pseudostream.file.source
  (:require [clj-pseudostream.error :as err]
            [clj-pseudostream.file.route :as route]
            [clj-pseudostream.media-source :as media])
  (:import [java.io RandomAccessFile InputStream OutputStream]))


(defn error [from message file range]
  (err/throw message from {:file file :range range}))


(defn random-access-file [file {:keys [playhead duration] :as range}]
  (let [raf (RandomAccessFile. file "r")
        length (.length raf)]
    (cond
      (> duration length) (error ::raf ::duration-exceeds-file-length file range)
      (> playhead length) (error ::raf ::playhead-exceeds-file-length file range)
      (< duration 0)      (error ::raf ::duration-negative file range)
      (< playhead 0)      (error ::raf ::playhead-negative file range)
      :else (try
              (.seek raf playhead)
              raf
              (catch Exception e
                (let [msg {::seek-failed (.getMessage e)}]
                  (error raf msg file range)))))))


(defrecord FileSource [file]

  media/MediaSource

  (duration [this]
    (.length file))

  ; InputStream interface:
  ;
  ; https://docs.oracle.com/javase/9/docs/api/java/io/InputStream.html
  ;
  (input-stream [this {:keys [playhead duration] :as range}]

    (let [size (.length file)
          range-length (+ playhead duration)]

      (if (> range-length size)

        (error ::input-stream ::range-exceeds-file-size file range)

        (try
          (let [raf (random-access-file file range)]

            #_(proxy [java.io.Inputstream] []

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

              )

            )
          (catch Exception e
            (error ::input-stream
                   {:type ::exception-reading-file
                    :ex (Throwable->map e)}
                   file range))
          )

        )

     ))

  (time-to-byte [this time]
    ;; TODO -- map proportion of file given duration
    time))

(comment

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
        (catch Throwable t (anomoly ::input-stream ::exception-reading-file file range t)))
  )

(defn new-media-source [file]
  (FileSource. file))
