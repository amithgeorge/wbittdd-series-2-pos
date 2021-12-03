(ns com.amithgeorge.pos.scan-code-test
  (:require [clojure.test :refer [deftest testing is]]
            [com.amithgeorge.pos.pos :as sut]))

(deftest handle-code-for-existing-product
  (testing "Given a valid code for an existing product, display its price"
    (let [input "product-1\n"
          display-text (atom "DISPLAY_NOT_CALLED")
          display-fn (fn [msg] (reset! display-text msg))]
      (sut/scan display-fn input)
      (is (= "USD 15.50" @display-text))))

  (testing "Given a valid code for another existing product, display its price"
    (let [input "product 2\n"
          display-text (atom "DISPLAY_NOT_CALLED")
          display-fn (fn [msg] (reset! display-text msg))]
      (sut/scan display-fn input)
      (is (= "USD 37.19" @display-text)))))

(deftest handle-code-for-product-not-found
  (testing "Given a valid code for product whose info we don't have, display not found message"
    (let [display-text (atom "DISPLAY_NOT_CALLED")
          display-fn (fn [msg] (reset! display-text msg))]
      (sut/scan display-fn "product-random-id\n")
      (is (= "Not found!" @display-text)))))

(deftest handle-malformed-code
  (testing "Given code for existing product, without ending line, display invalid code message"
    (let [display-text (atom "DISPLAY_NOT_CALLED")
          display-fn (fn [msg] (reset! display-text msg))]
      (sut/scan display-fn "product-1")
      (is (= "Invalid code!" @display-text)))))