(ns com.amithgeorge.pos.display)

(defn show
  [device type & args]
  (case type
    :price (let [{:keys [price]} (first args)]
             (device price))
    :total (let [{:keys [total]} (first args)]
             (device (format "Total: %s" total)))
    :invalid (device "Invalid code!")
    :not-found (device "Not found!")
    :pass-through (apply device args)))