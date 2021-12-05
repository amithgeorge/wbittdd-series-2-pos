(ns com.amithgeorge.pos.display)

(defn- format-price
  [price]
  (format "USD %s" price))

(defn- show
  [device type & args]
  (case type
    :price (let [{:keys [price]} (first args)]
             (device (format-price price)))
    :total (let [{:keys [total]} (first args)]
             (device (format "Total: %s" (format-price total))))
    :invalid (device "Invalid code!")
    :not-found (device "Not found!")
    :nothing-scanned (device "No products scanned yet. Please scan a product.")))

(defn price
  ([display-device product-price]
   (show display-device :price {:price product-price})))

(defn total
  ([display-device total]
   (show display-device :total {:total total})))

(defn not-found-message
  ([display-device]
   (show display-device :not-found)))

(defn code-invalid-message
  ([display-device]
   (show display-device :invalid)))

(defn nothing-scanned-message
  ([display-device]
   (show display-device :nothing-scanned)))

