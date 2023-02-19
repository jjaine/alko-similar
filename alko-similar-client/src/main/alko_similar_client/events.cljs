(ns alko-similar-client.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [ajax.core :as ajax]
            [day8.re-frame.http-fx]))

(def products-endpoint "http://alko-similar.dy.fi/:3000/api/product/")

(reg-event-fx
 :get-product
 (fn [{:keys [db]} [_ id]]
   (js/console.log "get-product" id)
   {:db (assoc-in db [:loading :product] true)
    :http-xhrio {:method :get
                 :uri (str products-endpoint id)
                 :response-format (ajax/json-response-format {:keyword? true})
                 :on-success [:get-product-success]
                 :on-failure [:endpoint-request-error :get-product]}}))

(reg-event-db
 :reset-product
 (fn [db _]
   (-> db
       (assoc-in [:loading :product] false)
       (assoc-in [:product] nil))))

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
 :endpoint-request-error
 (fn [db [_ request-type response]]
            (-> db
                (assoc-in [:errors request-type] (get response :status-text))
                (assoc-in [:loading request-type] false))))

