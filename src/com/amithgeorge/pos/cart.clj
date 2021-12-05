(ns com.amithgeorge.pos.cart)

(defn new []
  (atom {:total-str "" :total 0M}))
