(ns alko-similar-client.views
  (:require [re-frame.core :as rf]
            [reagent.core :as r]))

(def id (r/atom ""))

(defn header
  []
  [:div.px-6 {:class "w-full flex flex-col justify-center items-center"}
   [:img {:src "./img/logo.svg"
          :class "w-20 h-20 m-4"
          :on-click (fn []
                      (js/console.log "header")
                      (reset! id "")
                        (rf/dispatch [:reset-product]))}]])

(defn search-by-url-or-id
  []
  (let [product @(rf/subscribe [:product])]
    [:div {:class (str "max-w-screen-lg h-screen mx-auto p-4" (if (some? product) " hidden" ""))}
     [:div {:class "w-full h-1/4 shadow-lg rounded-xl bg-yellow-300 flex flex-col justify-center items-center"}
      [:div {:class "w-5/6 h-1/4"}
       [:input {:class       "w-4/6 h-full rounded-xl p-4 m-4"
                :type        "text"
                :placeholder "Search by URL or ID"
                :on-change   (fn [val] (reset! id (.-value (.-target val))))}]
       [:input {:class    "w-1/6 h-full rounded-xl p-4 bg-blue-200"
                :type     "button"
                :value    "Search"
                :on-click #(rf/dispatch [:get-product @id])}]]]
     [:div {:class "w-full h-1/4 shadow-lg rounded-xl bg-red-300 flex flex-col justify-center items-center"}
      [:button {:class "w-1/4 h-1/4 rounded-xl p-4 bg-blue-200"}
       "Use camera to scan barcode"]]]))

(defn product-info
  []
  (let [product @(rf/subscribe [:product])]
    (js/console.log "product info changed" product)
    (if (some? product)
      (let [{:keys [id name description package-size price type subtype country package-type alcohol-percentage image similar]} product]
        (js/console.log "product-info" id name description package-size price type subtype country package-type alcohol-percentage image similar)
        [:div {:class (str "max-w-screen-lg h-screen mx-auto p-4" (if (some? product) "" " hidden"))}
         [:p "Product info" (str id) (str name) (str description) (str package-size) (str price) (str type) (str subtype) (str country) (str package-type) (str alcohol-percentage) (str image)]])
      [:p ""])))

(defn home []
  [:div
   [header]
   [search-by-url-or-id]
   [product-info]])