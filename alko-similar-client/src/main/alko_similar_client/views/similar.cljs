(ns alko-similar-client.views.similar
  (:require [clojure.walk :as walk]
            [clojure.string :as string]
            [re-frame.core :as rf]
            [alko-similar-client.views.colors :refer [color-variants]]
            [alko-similar-client.views.filter :refer [clear-filters]]))

(defn product-name
  [name] 
  [:p {:class "text-xs font-locator mr-2 pt-1 px-1 whitespace-normal text-center"} name])

(defn product-type
  [type subtype beer-type]
  (let [subtype-key             (if (string/blank? subtype)
                                  (if (string/blank? beer-type)
                                    type
                                    beer-type)
                                  subtype)
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
    [:div {:class "flex flex-col"}
     [:p {:class (str "py-1 px-2 text-[0.65rem] text-center font-locator " color)} (string/capitalize subtype-key)]
     [:p {:class "border-b border-gray-300 py-1 px-2 pl text-[0.65rem] text-center font-locator"} (string/capitalize type)]
     ]))

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
     [:span {:class "text-2xl font-locator font-thin"} price-euros]
     [:span {:class "text-s font-locator font-thin mt-[0.07rem] ml-[0.1rem] underline"} price-cents-str]
     [:span {:class "text-s font-locator font-thin mt-[0.07rem] ml-[0.1rem]"} "€"]]))

(defn product-package-size
  [package-size]
  [:p {:class "text-s font-locator font-thin text-gray-600 text-right"} package-size])

(defn product-similarity-score
  [score product-score]
  (let [similarity-percentage (-> (/ score product-score)
                                  (* 100)
                                  (js/Math.round))
        mask-width            (-> (- 100 similarity-percentage)
                                  (str "%"))
        text-color            (if (<= similarity-percentage 50)
                                "text-black drop-shadow-percentage"
                                "text-red-100")]
    [:div {:class "relative"}
     [:div {:class "w-full bg-gradient-to-r from-red-400 to-red-800 h-4 flex justify-end"}
      [:div {:class "bg-gray-200 leading-none h-full"
             :style {:width mask-width}}]]
     [:p {:class (str "font-bold text-center text-xs top-0 absolute left-1/2 translate-x-[-50%] " text-color)} (str similarity-percentage "%")]]))

(defn product-country
  [country]
  [:div {:class "flex flex-col content-center flex-wrap my-2"}
   [:div {:class "flex flex-row content-center"}
    [:div {:class "bg-globe h-6 w-6 ml-2 mr-1 my-auto"}]
    [:p {:class "border border-gray-300 py-1 px-2 pl my-1 mr-1 text-xs font-locator"} country]]])

(defn product-link
  [id]
  (let [alko-url "https://www.alko.fi/tuotteet/"]
  [:div {:class "flex flex-col content-center flex-wrap border-t border-gray-300"}
   [:div {:class "flex flex-row content-center"
          :key id
          :on-click (fn [e] (.stopPropagation e))}
    [:div {:class "bg-search h-6 w-6 mr-2 my-auto"}]
    [:a {:class "py-1 px-2 pl my-1 mr-1 text-sm font-locator underline"
         :href (str alko-url id)
         :target "_blank"}
     "alko.fi"]]]))

(defn product-similar
  [similar filter-by min-price max-price]
  [:div {:class "flex flex-row flex-wrap justify-center"}
   (for [similar-product similar]
     (let [similar-keywords (walk/keywordize-keys similar-product)
           {:keys [id
                   name
                   package-size
                   price type
                   subtype
                   beer-type
                   country
                   image
                   score
                   product-score]} similar-keywords]
       [:div {:class  "flex flex-col w-36 border border-gray-300 m-1 justify-between cursor-pointer"
              :on-click #(do (clear-filters)
                             (reset! filter-by {})
                             (reset! min-price nil)
                             (reset! max-price nil)
                             (rf/dispatch [:reset-prices])
                             (rf/dispatch [:reset-similar])
                             (rf/dispatch [:get-product id])
                             (rf/dispatch [:get-similar id]))
              :key id}
        [:div
         (product-similarity-score score product-score)
         (product-type type subtype beer-type)
         (product-name name)]
        [:div
         (product-country country)
         [:div {:class "flex flex-col relative"}
          [:div {:class "flex flex-col absolute right-0 px-2 py-1 mr-2 bg-gray-100/80 rounded-lg space-y-[-0.5rem]"}
           (product-price price)
           (product-package-size package-size)]
          [:div {:class "flex flex-col items-center h-[15rem] justify-end flex-wrap"}
           [:img {:class "max-h-[15rem] max-w-[8rem] object-contain pb-2 px-1"
                  :src   image}]]
          (product-link id)]]]))])