(ns com.amithgeorge.pos.customer-total-test
  (:require [clojure.test :refer [deftest testing is]]
            [com.amithgeorge.pos.catalog :as catalog]
            [com.amithgeorge.pos.display :as display]
            [com.amithgeorge.pos.pos :as sut]))

(deftest no-products-scanned
  (testing "no products scanned yet, then display should show none-scanned message"
    (let [mock-device (atom "NOTHING_DISPLAYED_YET")
          device-fn (fn [message] (reset! mock-device message))
          display-fn (partial display/show device-fn)
          display-text mock-device]
      (sut/total display-fn)
      (is (= "No products scanned yet. Please scan a product."
             @display-text)))))

(deftest ^:kaocha/skip one-product-scanned
  (testing "one product scanned, then display should show its price as total"
    (let [mock-device (atom "NOTHING_DISPLAYED_YET")
          device-fn (fn [message]
                      (println "Received message:" message)
                      (reset! mock-device message))
          display-fn (partial display/show device-fn)
          display-text mock-device
          price-fn catalog/price]
      (sut/scan price-fn display-fn "product-1\n")
      (sut/total display-fn)
      (is (= "Total: USD 15.50" @display-text)))))