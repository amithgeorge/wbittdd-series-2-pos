(ns com.amithgeorge.pos.scan-code-test
  (:require [clojure.test :refer [deftest testing is]]
            [com.amithgeorge.pos.cart :as cart]
            [com.amithgeorge.pos.catalog :as catalog]
            [com.amithgeorge.pos.pos :as sut]))

(defn- valid-price-message?
  [message]
  (.startsWith message "USD "))

(defn setup
  []
  (let [mock-device (atom "NOTHING_TO_DISPLAY")
        device-fn (fn [message] (reset! mock-device message))
        cart (cart/new)
        sut-fn (partial sut/scan
                        catalog/price
                        device-fn
                        cart)]
    [sut-fn mock-device]))

(deftest handle-code-for-existing-product
  (testing "Given a valid code for an existing product, display its price"
    (let [[sut-fn display-text] (setup)]
      (sut-fn "product-1\n")
      (is (= "USD 15.50" @display-text))))

  (testing "Given a valid code for another existing product, display its price"
    (let [[sut-fn display-text] (setup)]
      (sut-fn "product 2\n")
      (is (= "USD 37.19" @display-text)))))

(deftest handle-code-for-product-not-found
  (testing "Given a valid code for product whose info we don't have, display not found message"
    (let [[sut-fn display-text] (setup)]
      (sut-fn "product-random-id\n")
      (is (= "Not found!" @display-text)))))

(deftest handle-malformed-code
  (let [args-list [["Invalid code!" nil "null input is invalid"]
                   ["Invalid code!" "" "empty input is invalid"]
                   ["Invalid code!" "\n" "empty input with just newline is invalid"]
                   ["Invalid code!" "   \n" "whitespace input with just newline is invalid"]
                   ["Invalid code!" " ... \n .. \n" "multiple newline is invalid"]
                   ["Invalid code!" "product-1\n  " "whitespace after newline is invalid"]
                   ["Invalid code!" "product-1  " "code with no newline is invalid"]]]
    (testing "malformed inputs"
      (doseq [[expected input err-message] args-list]
        (let [[sut-fn display-text] (setup)]
          (sut-fn input)
          (is (= expected @display-text) err-message))))))

(deftest support-both-unix-and-windows-newlines
  (testing "A code ending with unix newline is treated as valid"
    (let [[sut-fn display-text] (setup)]
      (sut-fn "product-1\n")
      (is (valid-price-message? @display-text))))

  (testing "A code ending with windows newline is treated as valid"
    (let [[sut-fn display-text] (setup)]
      (sut-fn "product-1\r\n")
      (is (valid-price-message? @display-text)))))