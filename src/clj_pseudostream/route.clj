(ns clj-pseudostream.route
  (:require [clj-pseudostream.file.core :as f]
            [clj-pseudostream.file.ring :as r]))

(defn new [server route root]
  "Creates a path map that contains the route, the root file 
   path and the vector of file extension handlers."
  (let [handlers (condp = server
                    :ring {:default r/handler}
                    {:default f/handler})]
    {:route route
     :root root
     :handlers handlers}))
