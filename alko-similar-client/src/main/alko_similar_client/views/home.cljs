(ns alko-similar-client.views.home
  (:require ["@heroicons/react/24/outline/ExclamationTriangleIcon" :as error-icon]
            [re-frame.core :as rf]
            [clojure.string :as string]
            [alko-similar-client.views.product :as product]))

(defn parse-id
  [val]
  (if (string/includes? val "alko.fi")
    (let [idx (string/index-of val "tuotteet/")]
      (subs val (+ idx 9) (+ idx 15)))
    val))

(defn search-by-url-or-id
  []
  (let [product @(rf/subscribe [:product])
        errors  @(rf/subscribe [:errors])
        error   (get errors :get-product)]
    [:div {:class (str "max-w-screen-lg h-screen mx-auto p-4" (if (some? product) " hidden" ""))}
     [:div {:class "w-full h-1/4 shadow-lg rounded-xl bg-yellow-300 flex flex-col justify-center items-center"}
      [:div {:class "w-5/6 h-1/2 flex flex-row justify-center items-center"}
       [:div {:class "w-4/6 h-full flex flex-col"}
        [:input {:class       "w-1/2 h-4 rounded-xl p-4 m-4"
                 :type        "text"
                 :placeholder "Paste alko.fi URL or product id"
                 :value       @product/id
                 :on-change   (fn [val] (reset! product/id (parse-id (.-value (.-target val)))))}]
        [:div {:class (str "w-1/2 h-1/2 flex flex-row" (if (some? error) "" " hidden"))}
         [:> error-icon {:style {:height       "1.75rem" ; :> creates a Reagent component from a React one
                                 :width        "1.75rem"
                                 :margin-left  "1rem"
                                 :margin-right "0.5rem"
                                 :color        "#d00000"}}]
         [:p {:style {:height "2rem"
                      :color  "#d00000"}} error]]]
       [:div {:class "w-2/6 h-full flex flex-col"}
        [:input {:class    "w-full h-4 rounded-xl p-4 m-4 bg-blue-200"
                 :type     "button"
                 :value    "Search"
                 :on-click #(do (rf/dispatch [:get-product @product/id])
                                (rf/dispatch [:get-similar @product/id]))}]]]]
     [:div {:class "w-full h-1/4 shadow-lg rounded-xl bg-red-300 flex flex-col justify-center items-center"}
      [:button {:class "w-1/4 h-1/4 rounded-xl p-4 bg-blue-300"}
       "Use camera to scan barcode"]]]))