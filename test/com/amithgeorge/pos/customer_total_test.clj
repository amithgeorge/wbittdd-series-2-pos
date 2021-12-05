(ns com.amithgeorge.pos.customer-total-test
  (:require [clojure.test :refer [deftest testing is]]
            [com.amithgeorge.pos.cart :as cart]
            [com.amithgeorge.pos.catalog :as catalog]
            [com.amithgeorge.pos.pos :as sut]))

(defn- setup
  []
  (let [mock-device (atom "NOTHING_DISPLAYED_YET")
        device-fn (fn [message]
                    (reset! mock-device message))
        price-fn catalog/price
        sut-scan (partial sut/scan price-fn device-fn)
        sut-total (partial sut/total device-fn)]
    {:display-text mock-device
     :sut-scan sut-scan
     :sut-total sut-total}))

(deftest no-products-scanned
  (testing "no products scanned yet, then display should show none-scanned message"
    (let [cart (cart/new)
          {:keys [sut-total display-text]} (setup)]
      (sut-total cart)
      (is (= "No products scanned yet. Please scan a product."
             @display-text)))))

(deftest one-product-scanned
  (testing "one product scanned, then display should show its price as total"
    (let [cart (cart/new)
          {:keys [sut-scan sut-total display-text]} (setup)]
      (sut-scan cart "product-1\n")
      (sut-total cart)
      (is (= "Total: USD 15.50" @display-text)))))

(deftest two-products-scanned
  (testing "two products scanned, then display sum of their prices as total"
    (let [cart (cart/new)
          {:keys [sut-scan sut-total display-text]} (setup)]
      (sut-scan cart "product-1\n")
      (sut-scan cart "product 2\n")
      (sut-total cart)
      (is (= "Total: USD 52.69" @display-text)))))

(deftest any-scanning-error-doesnt-update-total
  (testing "one product scanned, next product not found, display previous total"
    (let [cart (cart/new)
          {:keys [sut-scan sut-total display-text]} (setup)]
      (sut-scan cart "product-1\n")
      (sut-scan cart "DUMMY PRODUCT\n")
      (sut-total cart)
      (is (= "Total: USD 15.50" @display-text))))

  (testing "three products scanned, 2nd one not found, display total as sum of 1st and 3rd products"
    (let [cart (cart/new)
          {:keys [sut-scan sut-total display-text]} (setup)]
      (sut-scan cart "product-1\n")
      (sut-scan cart "DUMMY PRODUCT\n")
      (sut-scan cart "product 2\n")
      (sut-total cart)
      (is (= "Total: USD 52.69" @display-text))))

  (testing "one product scanned, next product not found, display previous total"
    (let [cart (cart/new)
          {:keys [sut-scan sut-total display-text]} (setup)]
      (sut-scan cart "product 2\n")
      (sut-scan cart "INVALID\n\n\nINPUT")
      (sut-total cart)
      (is (= "Total: USD 37.19" @display-text)))))