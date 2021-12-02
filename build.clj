(ns build
  (:refer-clojure :exclude [test])
  (:require [clojure.tools.build.api :as b] ; for b/git-count-revs
            [org.corfield.build :as bb]))

(def version (format "1.0.%s" (b/git-count-revs nil)))
(def main 'com.amithgeorge.pos)

(defn uberjar
  "Create the uberjar"
  [opts]
  (println "Creating the uberjar")
  (-> opts
      (assoc :version version :main main)
      (bb/clean)
      (bb/uber)))
