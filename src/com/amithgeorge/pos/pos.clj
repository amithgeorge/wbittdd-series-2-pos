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
  [price display input]
  (if-let [product-id (parse-product-id input)]
    (if-let [product-price (price product-id)]
      (display :price {:price product-price})
      (display :not-found))
    (display :invalid)))

(defn total
  [display cart]
  (when (str/blank? (cart :total-str))
    (display :pass-through "No products scanned yet. Please scan a product.")))