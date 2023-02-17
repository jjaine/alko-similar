(ns alko-similar-client.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :product
 (fn [db _]
   (:product db)))
