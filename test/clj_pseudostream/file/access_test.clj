(ns clj-pseudostream.file.access-test
  (:require [clojure.test :refer [is deftest testing]]
            [clojure.java.io :as io]
            [clj-pseudostream.file.fs :as fs]
            [slingshot.slingshot :refer [try+]]
            [clj-pseudostream.file.route :as route]))


(def stream-routes [(route/new-route
                      (str (.getAbsolutePath (io/file "")) "/test/resources/")
                      #"^/")])
