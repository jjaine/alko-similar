(ns alko-similar-server.handlers
  (:require [alko-similar-server.db :as db]
            [ring.util.response :as rr]
            [clj-time.core :as t]
            [clj-time.coerce :as tc]))

(defn list-popular-products
  [db]
  (fn [_request]
    (let [products (db/get-popular-products db)]
      (rr/response products))))

(defn list-recent-products
  [db]
  (fn [_request]
    (let [products (db/get-recent-products db)]
      (rr/response products))))

(defn update-product!
  [db]
  (fn [request]
    (let [id               (-> request :parameters :body :id)
          product          (-> request :parameters :body)
          existing-product (db/find-product-by-id db id)
          updated?         (if (some? existing-product) 
                             (let [look-count (:product/look_count existing-product)
                                   updated-product (-> existing-product
                                                       (assoc :product/look_count (inc look-count))
                                                       (assoc :product/look_date (tc/to-sql-time (t/now))))]
                               (db/update-product! db updated-product))
                             (db/insert-product! db product))]
      (if updated?
        (rr/response {:message "Product updated"
                      :data    (str "id " id)})
        (rr/not-found {:message "Product not found"
                       :data    (str "id " id)})))))