(ns alko-similar-client.views.product
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.string :as string]
            ["@heroicons/react/24/outline/ReceiptPercentIcon" :as percent-icon]
            [alko-similar-client.views.similar :as similar]
            [alko-similar-client.views.filter :as product-filter]
            [alko-similar-client.views.colors :refer [color-variants]]))

(def id (r/atom ""))
(def filter-by (r/atom {}))
(def min-price (r/atom nil))
(def max-price (r/atom nil))

(defn product-name
  [name]
  [:p {:class "text-2xl font-locator font-thin mr-2"} name])

(defn product-type
  [type subtype beer-type]
  (let [subtype-selected        (if (string/blank? subtype)
                                  beer-type
                                  subtype)
        subtype-key             (if (string/blank? subtype-selected)
                                  type
                                  subtype-selected)
        subtype-color-processed (->> (-> subtype-key
                                         (string/lower-case)
                                         (string/split #" "))
                                     (string/join "_"))
        subtype-color-key       (-> (if (= type "viskit")
                                      (str subtype-color-processed "_viskit")
                                      subtype-color-processed)
                                    keyword)
        subtype-color           (get color-variants subtype-color-key)
        color                   (if (some? subtype-color)
                                  subtype-color
                                  "bg-gray-800 text-white")]
    [:div {:class "flex flex-row flex-wrap"}
     [:p {:class "border border-gray-300 py-1 px-2 pl my-1 mr-1 text-sm font-locator"} (string/capitalize type)]
     (when (and (some? subtype-selected)
                (not= (string/lower-case type)
                      (string/lower-case subtype-selected))) [:p {:class (str "border py-1 px-2 my-1 text-sm font-locator " color)} (string/capitalize subtype-key)])]))

(defn product-price
  [price]
  (let [price-float (-> price
                        (js/parseFloat))
        price-euros (-> price-float
                        (js/Math.floor))
        price-cents (-> price-float
                        (- price-euros)
                        (* 100)
                        (js/Math.round))
        price-cents-str (if (< price-cents 10)
                          (str "0" price-cents)
                          (str price-cents))]
    [:p {:class "flex flex-row justify-end"}
     [:span {:class "text-4xl font-locator font-thin"} price-euros]
     [:span {:class "text-xl font-locator font-thin mt-[0.05rem] ml-[0.2rem] underline"} price-cents-str]
     [:span {:class "text-xl font-locator font-thin mt-[0.08rem] ml-[0.2rem]"} "€"]]))

(defn product-package-size
  [package-size]
  [:p {:class "text-m font-locator font-thin text-gray-500 text-right mt-[-0.5rem]"} package-size])

(defn product-description
  [description]
  [:p {:class "text-sm py-2 font-locator"} description])

(defn product-country
  [country region label-info]
  [:div {:class "flex flex-row content-center flex-wrap"}
   [:div {:class "bg-globe h-6 w-6 mr-2 my-auto"}]
   [:p {:class "border border-gray-300 py-1 px-2 pl my-1 mr-1 text-sm font-locator"} country]
   (when (-> region
             string/blank?
             not)
     [:p {:class "border border-gray-300 py-1 px-2 pl my-1 mr-1 text-sm font-locator"} region])
   (when (-> label-info
             string/blank?
             not)
     [:p {:class "border border-gray-300 py-1 px-2 pl my-1 mr-1 text-sm font-locator"} label-info])])

(defn product-grapes
  [grapes]
  (when (-> grapes
            string/blank?
            not)
    [:div {:class "flex flex-row content-center flex-wrap"}
     [:div {:class "bg-grapes h-6 w-6 mr-2 my-auto bg-no-repeat"}]
     (let [grapes-split (string/split grapes #", ")]
       (for [grape grapes-split]
         [:p {:class "border border-gray-300 py-1 px-2 pl my-1 mr-1 text-sm font-locator"
              :key grape} grape]))]))

(defn product-percentage
  [alcohol-percentage]
  [:div {:class "flex flex-row content-center"}
   [:> percent-icon {:style {:height        "1.5rem" ; :> creates a Reagent component from a React one
                             :width         "1.5rem"
                             :margin-right  "0.5rem"
                             :margin-top    "auto"
                             :margin-bottom "auto"}}]
   [:p {:class "border border-gray-300 py-1 px-2 pl my-1 mr-1 text-sm font-locator"} (str alcohol-percentage "%")]])

(defn product-link
  [id]
  (let [alko-url "https://www.alko.fi/tuotteet/"]
    [:div {:class "flex flex-row content-center"}
     [:div {:class "bg-search h-6 w-6 mr-2 my-auto"}]
     [:a {:class "border border-gray-300 py-1 px-2 pl my-1 mr-1 text-sm font-locator underline"
          :href (str alko-url id)
          :target "_blank"}
      "alko.fi"]]))

(defn product-info
  []
  (let [product @(rf/subscribe [:product])
        similar @(rf/subscribe [:similar])
        prices  @(rf/subscribe [:prices])]
    (when (and (some? product)
               (some? similar))
      (let [{:keys [id
                    name
                    description
                    package-size
                    price type
                    subtype
                    beer-type
                    country
                    region
                    label-info
                    grapes
                    alcohol-percentage
                    image]} product
            prices-parsed (->> similar
                               (map #(:price %))
                               (map #(js/parseFloat %)))
            min      (->> prices-parsed
                          (apply min)
                          (js/Math.floor))
            max      (->> prices-parsed
                          (apply max)
                          (js/Math.round))]
        (when (or (nil? prices)
                  (not= id (:product-id prices)))
          (rf/dispatch [:set-prices min max id])) 
        [:div {:class "max-w-screen-lg h-screen mx-auto p-4"}
         [:div {:class "flex flex-row justify-center mb-10"}
          [:div {:class "max-w-[70%]"}
           [:div {:class "flex flex-row place-content-between"}
            [:div
             (product-name name)
             (product-type type subtype beer-type)]
            [:div {:class "flex flex-col pl-4"}
             (product-price price)
             (product-package-size package-size)]]
           (product-description description)
           (product-country country region label-info)
           (product-grapes grapes)
           (product-percentage alcohol-percentage)
           (product-link id)]
          [:img {:class "max-h-60 ml-8 max-w-[70%]"
                 :src    image}]]
         (product-filter/filter-similar filter-by min-price max-price product prices)
         (similar/product-similar similar filter-by min-price max-price)]))))