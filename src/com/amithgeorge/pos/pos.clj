(ns com.amithgeorge.pos.pos
  (:require [clojure.string :as str]))

(defn ends-with-newline? [input]
  (or (.endsWith input "\n")
      (.endsWith input "\r\n")))

(defn parse-product-id [input]
  (if (or (str/blank? input)
          (not (ends-with-newline? input)))
    nil
    (let [parts (str/split-lines input)
          code (first parts)]
      (if (or (not= 1 (count parts))
              (str/blank? code))
        nil
        code))))

(defn scan
  ([price display cart input]
   (if-let [product-id (parse-product-id input)]
     (if-let [product-price (price product-id)]
       (do (display :price {:price product-price})
           (swap! cart assoc :total-str product-price))
       (display :not-found))
     (display :invalid))))

(defn total
  [display cart]
  (if (str/blank? (get @cart :total-str))
    (display :pass-through "No products scanned yet. Please scan a product.")
    (display :total {:total (get @cart :total-str)})))