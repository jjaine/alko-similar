(ns alko-similar-client.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :product
 (fn [db _]
   (:product db)))

(reg-sub
 :popular
 (fn [db _]
   (:popular db)))

(reg-sub
 :recent
 (fn [db _]
   (:recent db)))

(reg-sub
 :similar
 (fn [db _]
   (:similar db)))

(reg-sub
 :prices
 (fn [db _]
   (:prices db)))

(reg-sub
 :scanner
 (fn [db _]
   (:scanner db)))

(reg-sub
 :errors
  (fn [db _]
    (:errors db)))