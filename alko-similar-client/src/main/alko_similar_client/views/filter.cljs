(ns alko-similar-client.views.filter
  (:require ["@heroicons/react/24/outline/FunnelIcon" :as funnel-icon]
            ["@heroicons/react/24/outline/CurrencyEuroIcon" :as euro-icon]
            ["rc-slider$default" :as Slider]
            [re-frame.core :as rf]))

(defn clear-filters 
  []
  (let [array (.getElementsByTagName js/document "input")
        filtered (filter #(= "checkbox" (.-type %)) array)] 
    (doseq [input filtered]
      (set! (.. input -checked) false))))

(defn price-slider
  [product min-price max-price filter-by min max]
  [:div {:class "w-48 flex flex-row flex-wrap justify-center"}
   [:> euro-icon {:style {:height        "1.5rem" ; :> creates a Reagent component from a React one
                          :width         "1.5rem"
                          :margin-right  "0.5rem"
                          :margin-top    "auto"
                          :margin-bottom "auto"}}]
   [:div {:class "w-40"}
    [:> Slider {:min             min
                :max             max
                :default-value   [@min-price @max-price]
                :range           true
                :allow-cross     false
                :on-after-change (fn [[new_min new_max]]
                                   
                                   (if (= new_min min)
                                     (reset! min-price min)
                                     (reset! min-price new_min))
                                   (if (= new_max max)
                                     (reset! max-price max)
                                     (reset! max-price new_max))
                                   (rf/dispatch [:get-product (:id product) @filter-by @min-price @max-price]))}]]])

;package-size, type, subtype, country, package-type
(defn filter-similar
  [filter-by min-price max-price product min max]
  [:div {:class "flex flex-row flex-wrap justify-center mt-10"}
   [:> funnel-icon {:style {:height        "1.5rem" ; :> creates a Reagent component from a React one
                            :width         "1.5rem"
                            :margin-right  "0.5rem"
                            :margin-top    "auto"
                            :margin-bottom "auto"}}]
   (for [key [:type :subtype :country :package-type :package-size]]
     [:label {:class "text-sm font-locator font-thin my-1 mr-2 py-1 px-2 flex flex-row content-center border border-gray-300"
              :key key}
      [:input {:type "checkbox"
               :on-change (fn [e] (if (.-checked e.target)
                                    (swap! filter-by assoc key true)
                                    (swap! filter-by dissoc key))
                            (rf/dispatch [:get-product (:id product) @filter-by @min-price @max-price]))}]
      [:span {:class "ml-1.5 pt-0.5"}
       (key product)]]) 
   #_(price-slider product min-price max-price filter-by min max)])