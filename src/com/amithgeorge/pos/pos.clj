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
       (let [product-price-str (if (string? product-price) product-price (format "USD %s" product-price))]
         (do (display :price {:price product-price-str})
             (swap! cart update :total (fnil + 0M) product-price)))
       (display :not-found))
     (display :invalid))))

(defn total
  [display cart]
  (if (zero? (get @cart :total))
    (display :pass-through "No products scanned yet. Please scan a product.")
    (display :total {:total (get @cart :total)})))