(ns alko-similar-client.app-view
  (:require [re-frame.core :as rf] 
            [alko-similar-client.views.product :as product]
            [alko-similar-client.views.home :as home]))

(defn header
  []
  [:div.px-6 {:class "w-full flex flex-col justify-center items-center"}
   [:img {:src "./img/logo.svg"
          :class "w-20 h-20 m-4"
          :on-click (fn []
                      (js/console.log "header")
                      (reset! product/id "")
                      (rf/dispatch [:reset-product])
                      (rf/dispatch [:reset-popular])
                      (rf/dispatch [:reset-recent])
                      (rf/dispatch [:reset-similar])
                      (rf/dispatch [:reset-prices])
                      (rf/dispatch [:set-scanner false])
                      (reset! home/show-scanner false)
                      (rf/dispatch [:reset-errors])
                      (rf/dispatch [:get-popular])
                      (rf/dispatch [:get-recent]))}]])

(defn app []
  (rf/dispatch [:get-popular])
  (rf/dispatch [:get-recent])
  [:div
   [header]
   [home/search-by-url-or-id]
   [product/product-info]])