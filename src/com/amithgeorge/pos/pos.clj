(ns com.amithgeorge.pos.pos
  (:require [clojure.string :as str]
            [com.amithgeorge.pos.display :as display]
            [com.amithgeorge.pos.cart :as cart]))

(defn- ends-with-newline? [input]
  (or (.endsWith input "\n")
      (.endsWith input "\r\n")))

(defn- parse-product-id [input]
  (if (or (str/blank? input)
          (not (ends-with-newline? input)))
    nil
    (let [parts (str/split-lines input)
          code (first parts)]
      (if (or (not= 1 (count parts))
              (str/blank? code))
        nil
        code))))

(defn- update-cart-and-display-price
  [display-device cart product-price]
  (cart/update-total cart product-price)
  (display/price display-device product-price))

(defn scan
  ([price display-device cart input]
   (if-let [product-id (parse-product-id input)]
     (if-let [product-price (price product-id)]
       (update-cart-and-display-price display-device cart product-price)
       (display/not-found-message display-device))
     (display/code-invalid-message display-device))))

(defn total
  ([display-device cart]
   (if (cart/empty? cart)
     (display/nothing-scanned-message display-device)
     (display/total display-device (cart/total cart)))))