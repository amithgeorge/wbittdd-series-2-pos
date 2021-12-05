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