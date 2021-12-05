(ns com.amithgeorge.pos.catalog)

(def product-id->price
  {"product-1" 15.50M
   "product 2" 37.19M})

(defn price [product-id]
  (get product-id->price product-id))
