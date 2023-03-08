(ns alko-similar-client.views.home
  (:require ["@heroicons/react/24/outline/ExclamationTriangleIcon" :as error-icon]
            ["@heroicons/react/24/outline/MagnifyingGlassIcon" :as search-icon]
            ["@heroicons/react/24/outline/CameraIcon" :as camera-icon]
            ["react-zxing" :refer [useZxing]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.string :as string]
            [alko-similar-client.views.product :as product]))

(def show-scanner (r/atom false))

(defn parse-id
  [val]
  (if (string/includes? val "alko.fi")
    (let [idx (string/index-of val "tuotteet/")]
      (subs val (+ idx 9) (+ idx 15)))
    val))

(defn barcode-reader
  []
  (let [on-result (fn [result]
                    (let [id (. result -text)]
                      (rf/dispatch [:get-product id])
                      (rf/dispatch [:get-similar id])
                      (reset! show-scanner false)
                      (rf/dispatch [:set-scanner @show-scanner])))
        zxing     (useZxing #js {"onResult" on-result})
        ref       (. zxing -ref)]
    [:div {:class "flex justify-center m-2"}
     [:video {:class "w-80 rounded-xl overflow-hidden"
              :ref ref}]]))

(defn search-by-url-or-id
  []
  (let [product @(rf/subscribe [:product])
        errors  @(rf/subscribe [:errors])
        scanner @(rf/subscribe [:scanner])
        error   (get errors :get-product)]
    (when (nil? product)
      [:div {:class "h-screen"}
       [:div {:class "shadow-lg bg-alko-gray flex flex-col items-center min-h-[10rem]"}
        [:div {:class "py-6"}
         [:div {:class "flex flex-row items-center"}
          [:div {:class    "cursor-pointer relative left-14 w-14 h-12"
                 :on-click #(do (rf/dispatch [:reset-errors])
                                (reset! product/id "")
                                (reset! show-scanner (not @show-scanner))
                                (rf/dispatch [:set-scanner @show-scanner]))}
           [:div {:class "border-r border-gray-300 h-7 pl-4 my-[0.625rem] flex items-center"}
            [:> camera-icon {:style {:height "1.75rem" ; :> creates a Reagent component from a React one
                                     :width  "1.75rem"
                                     :color  "#333333"}}]]]
          [:input {:class       "w-[25rem] h-4 rounded-xl py-6 pr-6 pl-[4.2rem] my-4"
                   :type        "text"
                   :placeholder "Tuotteen numero tai alko.fi-linkki"
                   :value       @product/id
                   :on-change   (fn [val] (reset! product/id (parse-id (.-value (.-target val)))))
                   :on-key-down (fn [e] (when (= 13 (.-keyCode e))
                                          (rf/dispatch [:get-product @product/id])
                                          (rf/dispatch [:get-similar @product/id])))}]
          [:div {:class    "cursor-pointer relative right-14 w-14 h-12"
                 :on-click #(do (rf/dispatch [:get-product @product/id])
                                (rf/dispatch [:get-similar @product/id]))}
           [:div {:class "border-l border-gray-300 h-7 pl-3 my-[0.625rem] flex items-center"}
            [:> search-icon {:style {:height "1.75rem" ; :> creates a Reagent component from a React one
                                     :width  "1.75rem"
                                     :color  "#333333"}}]]]]
         (when (some? error)
           [:div {:class (str "flex flex-row w-80 ml-[6.5rem]")}
            [:> error-icon {:style {:height       "1.75rem" ; :> creates a Reagent component from a React one
                                    :width        "1.75rem"
                                    :margin-right "0.5rem"
                                    :color        "#d00000"}}]
            [:p {:style {:height "2rem"
                         :color  "#d00000"}} error]])
         (when scanner
           [:f> barcode-reader])]]
       ])))