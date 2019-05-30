(ns clj-pseudostream.route)

(defn new [base data-uri-fn] 
  {:base base
   :data-uri-fn data-uri-fn})
