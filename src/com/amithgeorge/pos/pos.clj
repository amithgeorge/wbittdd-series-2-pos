(ns com.amithgeorge.pos.pos
  (:require [clojure.string :as str]))

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

;; Cart functions

(defn- update-cart-total
  [cart product-price]
  (swap! cart update :total (fnil + 0M) product-price))

(defn- cart-empty?
  [cart]
  (zero? (get @cart :total)))

(defn- cart-total
  [cart]
  (get @cart :total))

;; Display functions

(defn- display-price
  [display product-price]
  (display :price {:price product-price}))

(defn- display-not-found-message
  [display]
  (display :not-found))

(defn- display-code-invalid-message
  [display]
  (display :invalid))

(defn- display-nothing-scanned-message
  [display]
  (display :pass-through "No products scanned yet. Please scan a product."))

(defn- display-total
  [display total]
  (display :total {:total total}))

(defn scan
  ([price display cart input]
   (if-let [product-id (parse-product-id input)]
     (if-let [product-price (price product-id)]
       (do (display-price display product-price)
           (update-cart-total cart product-price))
       (display-not-found-message display))
     (display-code-invalid-message display))))

(defn total
  [display cart]
  (if (cart-empty? cart)
    (display-nothing-scanned-message display)
    (display-total display (cart-total cart))))