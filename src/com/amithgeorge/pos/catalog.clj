(ns com.amithgeorge.pos.catalog)

(def product-id->price
  {"product-1" "USD 15.50"
   "product 2" "USD 37.19"})

(defn price [product-id]
  (get product-id->price product-id))
