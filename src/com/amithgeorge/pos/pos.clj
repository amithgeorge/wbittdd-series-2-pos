(ns com.amithgeorge.pos.pos)

(defn ends-with-newline? [input]
  (or (.endsWith input "\n")
      (.endsWith input "\r\n")))

(defn trim-newline [input]
  (.replaceFirst input "\r\n|\n", ""))

(defn scan
  [display input]
  (if (not (ends-with-newline? input))
    (display "Invalid code!")
    (let [input (trim-newline input)]
      (case input
        "product-1" (display "USD 15.50")
        "product 2" (display "USD 37.19")
        (display "Not found!")))))

