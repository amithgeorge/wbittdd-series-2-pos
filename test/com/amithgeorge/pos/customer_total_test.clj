(ns com.amithgeorge.pos.customer-total-test
  (:require [clojure.test :refer [deftest testing is]]
            [com.amithgeorge.pos.cart :as cart]
            [com.amithgeorge.pos.catalog :as catalog]
            [com.amithgeorge.pos.display :as display]
            [com.amithgeorge.pos.pos :as sut]))

(deftest no-products-scanned
  (testing "no products scanned yet, then display should show none-scanned message"
    (let [mock-device (atom "NOTHING_DISPLAYED_YET")
          device-fn (fn [message] (reset! mock-device message))
          display-fn (partial display/show device-fn)
          display-text mock-device
          cart (cart/new)]
      (sut/total display-fn cart)
      (is (= "No products scanned yet. Please scan a product."
             @display-text)))))

(deftest one-product-scanned
  (testing "one product scanned, then display should show its price as total"
    (let [mock-device (atom "NOTHING_DISPLAYED_YET")
          device-fn (fn [message]
                      (reset! mock-device message))
          display-fn (partial display/show device-fn)
          display-text mock-device
          price-fn catalog/price
          cart (cart/new)]
      (sut/scan price-fn display-fn cart "product-1\n")
      (sut/total display-fn cart)
      (is (= "Total: USD 15.50" @display-text)))))

(deftest two-products-scanned
  (testing "two products scanned, then display sum of their prices as total"
    (let [mock-device (atom "NOTHING_DISPLAYED_YET")
          device-fn (fn [message]
                      (reset! mock-device message))
          display-fn (partial display/show device-fn)
          display-text mock-device
          price-fn catalog/price
          cart (cart/new)]
      (sut/scan price-fn display-fn cart "product-1\n")
      (sut/scan price-fn display-fn cart "product 2\n")
      (sut/total display-fn cart)
      (is (= "Total: USD 52.69" @display-text)))))

(deftest any-scanning-error-doesnt-update-total
  (testing "one product scanned, next product not found, display previous total"
    (let [mock-device (atom "NOTHING_DISPLAYED_YET")
          device-fn (fn [message]
                      (reset! mock-device message))
          display-fn (partial display/show device-fn)
          display-text mock-device
          price-fn catalog/price
          cart (cart/new)]
      (sut/scan price-fn display-fn cart "product-1\n")
      (sut/scan price-fn display-fn cart "DUMMY PRODUCT\n")
      (sut/total display-fn cart)
      (is (= "Total: USD 15.50" @display-text))))

  (testing "three products scanned, 2nd one not found, display total as sum of 1st and 3rd products"
    (let [mock-device (atom "NOTHING_DISPLAYED_YET")
          device-fn (fn [message]
                      (reset! mock-device message))
          display-fn (partial display/show device-fn)
          display-text mock-device
          price-fn catalog/price
          cart (cart/new)]
      (sut/scan price-fn display-fn cart "product-1\n")
      (sut/scan price-fn display-fn cart "DUMMY PRODUCT\n")
      (sut/scan price-fn display-fn cart "product 2\n")
      (sut/total display-fn cart)
      (is (= "Total: USD 52.69" @display-text))))

  (testing "one product scanned, next product not found, display previous total"
    (let [mock-device (atom "NOTHING_DISPLAYED_YET")
          device-fn (fn [message]
                      (reset! mock-device message))
          display-fn (partial display/show device-fn)
          display-text mock-device
          price-fn catalog/price
          cart (cart/new)]
      (sut/scan price-fn display-fn cart "product 2\n")
      (sut/scan price-fn display-fn cart "INVALID\n\n\nINPUT")
      (sut/total display-fn cart)
      (is (= "Total: USD 37.19" @display-text)))))