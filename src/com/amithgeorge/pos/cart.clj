(ns com.amithgeorge.pos.cart)

(defn new []
  (atom {:total 0M}))

(defn update-total
  [cart product-price]
  (swap! cart update :total + product-price))

(defn empty?
  [cart]
  (zero? (get @cart :total)))

(defn total
  [cart]
  (get @cart :total))

