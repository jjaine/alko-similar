(ns alko-similar-client.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [clojure.string :as string]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]))

(goog-define URL "https://alko-similar.dy.fi")

(def products-endpoint (str URL "/api/product/"))
(def similars-endpoint (str URL "/api/similar/"))
(def populars-endpoint (str URL "/api/products/popular"))
(def recents-endpoint (str URL "/api/products/recent"))
(def update-endpoint (str URL "/api/products"))

(reg-event-fx
 :get-product
 (fn [{:keys [db]} [_ id]]
   {:db         (assoc-in db [:loading :product] true)
    :http-xhrio {:method          :get
                 :uri             (str products-endpoint id)
                 :response-format (ajax/json-response-format {:keyword? true})
                 :on-success      [:get-product-success]
                 :on-failure      [:endpoint-request-error :get-product]}}))

(reg-event-fx
 :log-product
 (fn [{:keys [_db]} [_ id]]
   {:http-xhrio {:method          :post
                 :uri             update-endpoint
                 :params          {:id id}
                 :format          (ajax/json-request-format {:keyword? true})
                 :response-format (ajax/json-response-format {:keyword? true}) 
                 :on-success      [:log-product-success]
                 :on-failure      [:endpoint-request-error :log-product]}}))

(reg-event-fx
 :get-popular
 (fn [{:keys [db]} _]
   {:db         (assoc-in db [:loading :popular] true)
    :http-xhrio {:method          :get
                 :uri             populars-endpoint
                 :response-format (ajax/json-response-format {:keyword? true})
                 :on-success      [:get-popular-success]
                 :on-failure      [:endpoint-request-error :get-popular]}}))

(reg-event-fx
 :get-recent
 (fn [{:keys [db]} _]
   {:db         (assoc-in db [:loading :recent] true)
    :http-xhrio {:method          :get
                 :uri             recents-endpoint
                 :response-format (ajax/json-response-format {:keyword? true})
                 :on-success      [:get-recent-success]
                 :on-failure      [:endpoint-request-error :get-recent]}}))

(reg-event-fx
 :get-popular-product
 (fn [{:keys []} [_ id]]
   {:http-xhrio {:method          :get
                 :uri             (str products-endpoint id)
                 :response-format (ajax/json-response-format {:keyword? true})
                 :on-success      [:get-popular-product-success]
                 :on-failure      [:get-popular-product-failure]}}))

(reg-event-fx
 :get-recent-product
 (fn [{:keys []} [_ id]]
   {:http-xhrio {:method          :get
                 :uri             (str products-endpoint id)
                 :response-format (ajax/json-response-format {:keyword? true})
                 :on-success      [:get-recent-product-success]
                 :on-failure      [:get-recent-product-failure]}}))

(reg-event-fx
 :get-similar
 (fn [{:keys [db]} [_ id filter-by min-price max-price]]
   (let [filter-by-values (->> filter-by
                               keys
                               (map name)
                               (string/join ","))
         filter-by-url    (when (not (string/blank? filter-by-values))
                            (str "filter-by=" filter-by-values))
         min-price-url    (when (not (or (nil? min-price)
                                         (= min-price 0)))
                            (str "min-price=" min-price))
         max-price-url    (when (not (or (nil? max-price)
                                         (= max-price js/Number.MAX_SAFE_INTEGER)))
                            (str "max-price=" max-price))
         parameters       (string/join "&" (remove nil? [filter-by-url min-price-url max-price-url]))
         parameters-url   (when (not (string/blank? parameters))
                            (str "?" parameters))]
     (js/console.log "get-similar" id parameters-url)
     {:db         (assoc-in db [:loading :similar] true)
      :http-xhrio {:method          :get
                   :uri             (str similars-endpoint id parameters-url)
                   :response-format (ajax/json-response-format {:keyword? true})
                   :on-success      [:get-similar-success]
                   :on-failure      [:endpoint-request-error :get-similar]}})))

(reg-event-fx
 :set-prices
 (fn [{:keys [db]} [_ min-price max-price product-id]]
   {:db (assoc-in db [:prices] {:product-id product-id
                                :min-price  min-price
                                :max-price  max-price})}))

(reg-event-fx
 :set-scanner
 (fn [{:keys [db]} [_ show]]
   {:db (assoc-in db [:scanner] show)}))

(reg-event-db
 :reset-product
 (fn [db _]
   (-> db
       (assoc-in [:loading :product] false)
       (assoc-in [:product] nil))))

(reg-event-db
 :reset-popular
 (fn [db _]
   (-> db
       (assoc-in [:loading :popular] false)
       (assoc-in [:popular] nil))))

(reg-event-db
 :reset-recent
 (fn [db _]
   (-> db
       (assoc-in [:loading :recent] false)
       (assoc-in [:recent] nil))))

(reg-event-db
 :reset-popular-products
 (fn [db _]
   (-> db
       (assoc-in [:popular-products] nil))))

(reg-event-db
 :reset-recent-products
 (fn [db _]
   (-> db
       (assoc-in [:recent-products] nil))))

(reg-event-db
 :reset-similar
 (fn [db _]
   (-> db
       (assoc-in [:loading :similar] false)
       (assoc-in [:similar] nil))))

(reg-event-db
 :reset-prices
 (fn [db _]
   (-> db
       (assoc-in [:prices] nil))))

(reg-event-db
 :reset-errors
 (fn [db _]
   (-> db
       (assoc-in [:errors] nil))))

(defn parse-product
  [product]
  (let [product (into {} product)]
    (reduce-kv
     (fn [acc k v]
       (assoc acc (keyword k) v))
     {}
     product)))

(defn parse-db-product
  [product]
  (let [product (into {} product)]
    (reduce-kv
     (fn [acc k v]
       (let [key (keyword (subs k 8))] ; remove "product/" prefix
         (assoc acc key v)))
     {}
     product)))

(reg-event-db
 :get-product-success
 (fn [db [_ product]]
   (let [product-map (parse-product product)]
     (-> db
         (assoc-in [:loading :product] false)
         (assoc-in [:product] product-map)))))

(reg-event-db
 :get-popular-success
 (fn [db [_ popular]]
   (let [products    (get popular "products") 
         popular-map (->> products 
                          (map parse-db-product)
                          (map :id))]
     (-> db
         (assoc-in [:loading :popular] false)
         (assoc-in [:popular] popular-map)))))

(reg-event-db
 :get-recent-success
 (fn [db [_ recent]]
   (let [products   (get recent "products")
         recent-map (->> products 
                         (map parse-db-product)
                         (map :id))]
     (-> db
         (assoc-in [:loading :recent] false)
         (assoc-in [:recent] recent-map)))))

(reg-event-db
 :get-popular-product-success
 (fn [db [_ popular-product]]
   (let [popular-map      (parse-product popular-product)
         id               (:id popular-map)
         popular-products (:popular-products db)
         popular-updated  (if (nil? popular-products)
                            {id popular-map}
                            (assoc popular-products id popular-map))]
     (-> db
         (assoc-in [:popular-products] popular-updated)))))

(reg-event-db
 :get-recent-product-success
 (fn [db [_ recent-product]]
   (let [recent-map      (parse-product recent-product)
         id              (:id recent-map)
         recent-products (:recent-products db)
         recent-updated  (if (nil? recent-products)
                           {id recent-map}
                           (assoc recent-products id recent-map))]
     (-> db
         (assoc-in [:recent-products] recent-updated)))))

(reg-event-db
 :get-recent-product-failure
 (fn [db [_ recent-product]]
   (let [uri             (:uri recent-product) 
         id              (subs uri (inc (.lastIndexOf uri "/")))
         recent-products (:recent-products db)
         recent-updated  (if (nil? recent-products)
                           {id nil}
                           (assoc recent-products id nil))]
     (-> db
         (assoc-in [:recent-products] recent-updated)))))

(reg-event-db
 :get-popular-product-failure
 (fn [db [_ popular-product]]
   (let [uri              (:uri popular-product)
         id               (subs uri (inc (.lastIndexOf uri "/")))
         popular-products (:popular-products db)
         popular-updated  (if (nil? popular-products)
                            {id nil}
                            (assoc popular-products id nil))]
     (-> db
         (assoc-in [:popular-products] popular-updated)))))

(reg-event-db
 :get-similar-success
 (fn [db [_ similar]]
   (let [products    (get similar "similar") 
         similar-map (map parse-product products)]
     (-> db
         (assoc-in [:loading :similar] false)
         (assoc-in [:similar] similar-map)))))

(reg-event-db
 :endpoint-request-error
 (fn [db [_ request-type response]]
   (js/console.log "endpoint-request-error" request-type response)
   (let [res (first (get response :response))]
     (-> db
         (assoc-in [:errors request-type] (if (nil? res)
                                            "Tuntematon tuote!"
                                            res))
         (assoc-in [:loading request-type] false)))))

(reg-event-db
 :log-product-success
 (fn [db [_ product]]
   (js/console.log "log-product-success" product)
   db))