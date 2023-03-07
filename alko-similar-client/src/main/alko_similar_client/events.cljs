(ns alko-similar-client.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [clojure.string :as string]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]))

(goog-define URL "http://alko-similar.dy.fi:3000")

(def products-endpoint (str URL "/api/product/"))
(def similars-endpoint (str URL "/api/similar/"))

(reg-event-fx
 :get-product
 (fn [{:keys [db]} [_ id]]
   (js/console.log "get-product" id) 
   {:db         (assoc-in db [:loading :product] true)
    :http-xhrio {:method          :get
                 :uri             (str products-endpoint id)
                 :response-format (ajax/json-response-format {:keyword? true})
                 :on-success      [:get-product-success]
                 :on-failure      [:endpoint-request-error :get-product]}}))

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
 (fn [{:keys [db]} [_ min-price max-price]]
   {:db (assoc-in db [:prices] {:min-price min-price
                                :max-price max-price})}))

(reg-event-db
 :reset-product
 (fn [db _]
   (-> db
       (assoc-in [:loading :product] false)
       (assoc-in [:product] nil))))

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

(reg-event-db
 :get-product-success
 (fn [db [_ product]]
   (let [product-fn (fn [product]
                       (let [product (into {} product)]
                         (reduce-kv
                          (fn [acc k v]
                            (assoc acc (keyword k) v))
                          {}
                          product)))
         product-map (product-fn product)]
     (-> db
         (assoc-in [:loading :product] false)
         (assoc-in [:product] product-map)))))

(reg-event-db
 :get-similar-success
 (fn [db [_ similar]]
   (let [similar-fn (fn [similar]
                      (let [similar (into {} similar)]
                        (reduce-kv
                         (fn [acc k v]
                           (assoc acc (keyword k) v))
                         {}
                         similar)))
         similar-map (map similar-fn (get similar "similar"))]
     (-> db
         (assoc-in [:loading :similar] false)
         (assoc-in [:similar] similar-map)))))

(reg-event-db
 :endpoint-request-error
 (fn [db [_ request-type response]]
   (js/console.log "endpoint-request-error" request-type response)
   (-> db
       (assoc-in [:errors request-type] (first (get response :response)))
       (assoc-in [:loading request-type] false))))
