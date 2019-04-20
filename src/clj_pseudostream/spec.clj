(ns clj-pseudostream.spec)

; Spec range here 
(def range {:start 1 :length 100})

; config
(def spec {:matches #{} 
           :root-paths #{} 
           :extensions #{} 
           :allowed-fn :allowed?})
