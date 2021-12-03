(ns com.amithgeorge.pos.pos)

(defn scan
  [display input]
  (if (not (.endsWith input "\n"))
    (display "Invalid code!")
    (case input
      "product-1\n" (display "USD 15.50")
      "product 2\n" (display "USD 37.19")
      (display "Not found!"))))

(comment
  (.endsWith "product-1" "\n"))