(ns halo_api.handler-test
  (:use clojure.test)
  (:require [halo-api.handler :refer :all]
            [halo-api.mongo :refer :all]
            [ring.mock.request :as mock]
            [monger.collection :as mc]
            [util.auth :refer :all]
            [clojure.data.json :as json]))

(defn user-init []
  (def test-user (mc/insert-and-return db "user" {:gender 1
                                                  :nickname "Test User"
                                                  :city "Utopia"
                                                  :country "Utopia"
                                                  :openid "test openid"}))
  (def test-token (save-token! (:_id test-user))))

(defn wrap-test [test-fn]
  (user-init)
  (test-fn))

(deftest test-app
  (testing "hello world"
    (let [response (app (mock/request :get "/hello/world"))]
      (let [res (json/read-str (slurp (:body response)) :key-fn keyword)]
        (is (= (:status response) 200))
        (is (= res {:msg "Hello World!!"}))))))

(deftest test-login
  (testing "login"
    (let [response (app (mock/request :get "/wechat/auth"))]
      (is (= (:status response) 400))
      (is (= (:body response) "Code Required!")))))

(deftest test-code-login
  (testing "login with fake code"
    (let [response (app (mock/request :get "/wechat/auth" {:code "fake one"}))]
      (is (= (:status response) 400))
      (is (= (:body response) "Wechat Openid Not Available!")))))

(deftest test-orders
  (testing "orders"
    (let [response (app (-> (mock/request :post "/base/orders" nil)
                            (mock/header "Authorization" test-token)
                            (mock/body {:item "text book" :fee 4500})))]
      (let [res (json/read-str (slurp (:body response)) :key-fn keyword)]
        (is (= (:status response) 200))
        (is (= res {:item "text book" :fee "4500" :status "unpaid"}))))))

(deftest test-get-orders
  (testing "get orders"
    (let [response (app (-> (mock/request :get"/base/orders")
                            (mock/header "Authorization" test-token)))]
      (let [res (json/read-str (slurp (:body response)):key-fn keyword)]
        (is (= (:status res) 200))
        (is (= (:body res) '[]))))))

(deftest test-logout
  (testing "logout"
    (let [response (app (-> (mock/request :post "/base/logout" nil)
                            (mock/header "Authorization" test-token)))]
      (mc/remove db "user" {:_id test-user})
      (let [res (json/read-str (slurp (:body response)) :key-fn keyword)]
        (is (= (:status response) 200))
        (is (= res {:msg "success"}))))))


(use-fixtures :once wrap-test)
(run-tests)
