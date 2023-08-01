(ns alko-similar-server.db
  (:require [next.jdbc.sql :as sql]
            [next.jdbc :as jdbc]))

(defn get-popular-products
  [db]
  (println "get-popular-products db" db)
  (with-open [conn (jdbc/get-connection db)]
    (println "get-popular-products conn" conn)
    (let [products (sql/query conn ["SELECT * FROM product ORDER BY look_count DESC LIMIT 5"])]
      (println "get-popular-products result" products)
      {:products products})))

(defn get-recent-products
  [db]
  (println "get-recent-products db" db)
  (with-open [conn (jdbc/get-connection db)]
    (println "get-recent-products conn" conn)
    (let [products (sql/query conn ["SELECT * FROM product ORDER BY look_date DESC LIMIT 5"])]
      (println "get-recent-products result" products)
      {:products products})))

(defn insert-product!
  [db product]
  (sql/insert! db :product product))

(defn find-product-by-id
  [db product-id]
  (with-open [conn (jdbc/get-connection db)]
    (let [[product] (sql/find-by-keys conn :product {:id product-id})]
      product)))

(defn update-product!
  [db product]
  (-> (sql/update! db :product product (select-keys product [:product/id]))
      :next.jdbc/update-count
      (pos?)))
