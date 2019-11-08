(ns clj-pseudostream.file.route-test
  (:require [clojure.test :refer [is deftest testing]]
            [clj-pseudostream.file.route :as route]))


(deftest extension
  (let [rel-path "/some/relative/path/movie.mp4"
        ext (route/media-extension rel-path)]
    (is (= :mp4 ext))))


(deftest find-route
  (let [rel-path "/some/relative/path/test.mp4"
        stream-routes [(route/new-route "/mnt/files" #"regex1")
                       (route/new-route "/mnt/files2" #"regex2")]]
    (testing "no match"
      (is (nil? (route/find-route stream-routes "/no/match"))))

    (testing "regex1"
      (let [r (route/find-route stream-routes "/first/path")]
        ))

    (testing "regex2"
      (let [r (route/find-route stream-routes "/second/path")]
        (is (some? r))
        (is (= "/mnt/files2" (::route/fs-root r)))))
    ))
