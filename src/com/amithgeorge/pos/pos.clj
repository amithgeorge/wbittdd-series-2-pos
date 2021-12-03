(ns com.amithgeorge.pos.pos
  (:require [clojure.string :as str]))

(defn ends-with-newline? [input]
  (or (.endsWith input "\n")
      (.endsWith input "\r\n")))

(defn trim-newline [input]
  (.replaceFirst input "\r\n|\n", ""))

(defn valid-input? [input]
  (if (or (nil? input)
          (not (ends-with-newline? input)))
    false
    (let [parts (str/split-lines input)]
      (if (not= 1 (count parts))
        false
        (not (str/blank? (first parts)))))))

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

(def price {"product-1" "USD 15.50"
            "product 2" "USD 37.19"})

(defn scan
  ([display input]
   (if-let [product-id (parse-product-id input)]
     (if-let [product-price (price product-id)]
       (display product-price)
       (display "Not found!"))
     (display "Invalid code!"))))

