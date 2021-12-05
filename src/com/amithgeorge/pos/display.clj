(ns com.amithgeorge.pos.display)

(defn- format-price
  [price]
  (format "USD %s" price))

(defn show
  [device type & args]
  (case type
    :price (let [{:keys [price]} (first args)]
             (device (format-price price)))
    :total (let [{:keys [total]} (first args)]
             (device (format "Total: %s" (format-price total))))
    :invalid (device "Invalid code!")
    :not-found (device "Not found!")
    :pass-through (apply device args)))

(defn price
  ([display product-price]
   (display :price {:price product-price}))
  ([display-device display product-price]
   (show display-device :price {:price product-price})))

(defn total
  ([display total]
   (display :total {:total total}))
  ([display-device display total]
   (show display-device :total {:total total})))

(defn not-found-message
  [display]
  (display :not-found))

(defn code-invalid-message
  [display]
  (display :invalid))

(defn nothing-scanned-message
  ([display]
   (display :pass-through "No products scanned yet. Please scan a product."))
  ([display-device display]
   (show display-device :pass-through "No products scanned yet. Please scan a product.")))

