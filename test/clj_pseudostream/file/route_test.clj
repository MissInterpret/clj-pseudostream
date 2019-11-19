(ns clj-pseudostream.file.route-test
  (:require [clojure.test :refer [is deftest testing]]
            [clj-pseudostream.file.route :as route]))


(deftest extension
  (let [rel-path "/some/relative/path/movie.mp4"
        ext (route/media-extension rel-path)]
    (is (= :mp4 ext))))


(deftest find-route
  (let [rel-path "/some/relative/path/test.mp4"
        stream-routes [(route/new-route "/mnt/files" #"^/some")
                       (route/new-route "/mnt/files2" #"^/other")]]
    (testing "no match"
      (is (nil? (route/find-route stream-routes "/no/match"))))

    (testing "regex1"
      (let [r (route/find-route stream-routes "/some/path")]
        (is (some? r))
        (is (= "/mnt/files" (::route/fs-root r)))))

    (testing "regex2"
      (let [r (route/find-route stream-routes "/other/path")]
        (is (some? r))
        (is (= "/mnt/files2" (::route/fs-root r)))))))
