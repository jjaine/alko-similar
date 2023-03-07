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
  [product min-price max-price filter-by prices] 
  [:div {:class "max-w-[17rem] flex flex-row flex-wrap"}
   [:> euro-icon {:style {:height        "1.5rem" ; :> creates a Reagent component from a React one
                          :width         "1.5rem"
                          :margin-left   "0.2rem"
                          :margin-top    "auto"
                          :margin-bottom "auto"}}] 
   (let [min_value (if (not (nil? prices))
                     (:min-price prices)
                     0)
         max_value (if (not (nil? prices))
                     (:max-price prices)
                     10000)]
     (reset! min-price min_value)
     (reset! max-price max_value)
     [:div {:class "border border-gray-300 w-48 my-1 ml-2 py-1 px-2 flex flex-row items-center"}
      [:p {:class "text-sm font-locator font-thin mr-3 w-6"
           :id "min-price"} (str @min-price "€")]
      [:> Slider {:style {:flex-grow 1
                          :flex-shrink 1}
                  :min             min_value
                  :max             max_value
                  :default-value   [@min-price @max-price]
                  :range           true
                  :allow-cross     false
                  :on-change       (fn [[new_min new_max]]
                                     (let [min-elem (.getElementById js/document "min-price")
                                           max-elem (.getElementById js/document "max-price")]
                                       (set! (.. min-elem -innerHTML) (str new_min "€"))
                                       (set! (.. max-elem -innerHTML) (str new_max "€"))))
                  :on-after-change (fn [[new_min new_max]]
                                     (if (= new_min min)
                                       (reset! min-price min)
                                       (reset! min-price new_min))
                                     (if (= new_max max)
                                       (reset! max-price max)
                                       (reset! max-price new_max))
                                     (rf/dispatch [:get-similar (:id product) @filter-by @min-price @max-price]))}]
      [:p {:class "text-sm font-locator font-thin ml-3 w-8"
           :id "max-price"} (str @max-price "€")]])])

(defn filter-similar
  [filter-by min-price max-price product prices]
  [:div {:class "flex flex-row flex-wrap justify-center"}
   [:div {:class "flex flex-row flex-wrap justify-center"}
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
                             (rf/dispatch [:get-similar (:id product) @filter-by @min-price @max-price]))}]
       [:span {:class "ml-1.5 pt-0.5"}
        (key product)]])]
   [:div {:class "flex flex-row flex-wrap"}
    (price-slider product min-price max-price filter-by prices)]])