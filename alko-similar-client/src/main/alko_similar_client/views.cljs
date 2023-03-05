(ns alko-similar-client.views
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            ["@heroicons/react/24/outline/ExclamationTriangleIcon" :as error-icon]
            ["@heroicons/react/24/outline/GlobeEuropeAfricaIcon" :as globe-icon]
            ["@heroicons/react/24/outline/ReceiptPercentIcon" :as percent-icon]
            [clojure.string :as string]))

(def colors-variants
  {:lager "bg-lager text-white"
   :tumma_lager "bg-tumma_lager text-white"
   :pils "bg-pils text-black"
   :vahva_lager "bg-vahva_lager text-white"
   :vehnäolut "bg-vehnäolut text-black"
   :ale "bg-ale text-white"
   :stout_&_porter "bg-stout_&_porter text-white"
   :erikoisuus "bg-erikoisuus text-white"
   :ipa "bg-ipa text-white"
   :marjaisa_&_raikas "bg-marjaisa_&_raikas text-white"
   :pehmeä_&_hedelmäinen "bg-pehmeä_&_hedelmäinen text-black"
   :mehevä_&_hilloinen "bg-mehevä_&_hilloinen text-white"
   :vivahteikas_&_kehittynyt "bg-vivahteikas_&_kehittynyt text-white"
   :roteva_&_voimakas "bg-roteva_&_voimakas text-white"
   :pehmeä_&_kepeä "bg-pehmeä_&_kepeä text-black"
   :lempeä_&_makeahko "bg-lempeä_&_makeahko text-white"
   :pirteä_&_hedelmäinen "bg-pirteä_&_hedelmäinen text-white"
   :vivahteikas_&_ryhdikäs "bg-vivahteikas_&_ryhdikäs text-white"
   :runsas_&_paahteinen "bg-runsas_&_paahteinen text-white"
   :kepeä_&_viljainen_viskit "bg-kepeä_&_viljainen_viskit text-black"
   :pehmeä_&_hedelmäinen_viskit "bg-pehmeä_&_hedelmäinen_viskit text-white"
   :hedelmäinen_&_aromikas_viskit "bg-hedelmäinen_&_aromikas_viskit text-white"
   :vivahteikas_&_ryhdikäs_viskit "bg-vivahteikas_&_ryhdikäs_viskit text-white"
   :runsas_&_voimakas_viskit "bg-runsas_&_voimakas_viskit text-white"
   :greippinen "bg-greippinen text-black"
   :sitruksinen "bg-sitruksinen text-black"
   :hedelmäinen "bg-hedelmäinen text-white"
   :marjaisa "bg-marjaisa text-white"
   :maustetut_ja_muut "bg-maustetut_ja_muut text-white"
   :vs-konjakit "bg-vs-konjakit text-black"
   :vsop-konjakit "bg-vsop-konjakit text-white"
   :xo-konjakit "bg-xo-konjakit text-white"
   :muut_konjakit "bg-muut_konjakit text-white"
   :tastestyle_441 "bg-tastestyle_441"
   :tastestyle_443 "bg-tastestyle_443"
   :tastestyle_445 "bg-tastestyle_445"
   :tastestyle_447 "bg-tastestyle_447"
   :tastestyle_449 "bg-tastestyle_449"})

(def id (r/atom ""))

(defn header
  []
  [:div.px-6 {:class "w-full flex flex-col justify-center items-center"}
   [:img {:src "./img/logo.svg"
          :class "w-20 h-20 m-4"
          :on-click (fn []
                      (js/console.log "header")
                      (reset! id "")
                      (rf/dispatch [:reset-product])
                      (rf/dispatch [:reset-errors]))}]])

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
                 :value       @id
                 :on-change   (fn [val] (reset! id (parse-id (.-value (.-target val)))))}]
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
                 :on-click #(rf/dispatch [:get-product @id])}]]]]
     [:div {:class "w-full h-1/4 shadow-lg rounded-xl bg-red-300 flex flex-col justify-center items-center"}
      [:button {:class "w-1/4 h-1/4 rounded-xl p-4 bg-blue-300"}
       "Use camera to scan barcode"]]]))

(defn product-info
  []
  (let [product @(rf/subscribe [:product])]
    (if (some? product)
      (let [{:keys [id
                    name
                    description
                    package-size
                    price type
                    subtype
                    beer-type
                    country
                    package-type
                    alcohol-percentage
                    image
                    similar]} product
            subtype-key (if (string/blank? subtype)
                          beer-type
                          subtype)
            subtype-color-processed (->> (-> subtype-key
                                             (string/lower-case)
                                             (string/split #" "))
                                         (string/join "_"))
            subtype-color-key (-> (if (= type "viskit")
                                    (str subtype-color-processed "_viskit")
                                    subtype-color-processed)
                                  keyword)
            subtype-color (get colors-variants subtype-color-key)
            color (if (some? subtype-color)
                    subtype-color
                    "bg-gray-800")
            price-float (-> price
                            (js/parseFloat))
            price-euros (-> price-float
                            (js/Math.floor))
            price-cents (-> price-float
                            (js/Math.floor)
                            (- price-float)
                            (* 100)
                            (js/Math.floor)
                            (* -1))]
        (js/console.log "color" color)
        (js/console.log "product-info" id name description package-size price type subtype beer-type country package-type alcohol-percentage image similar)
        [:div {:class (str "max-w-screen-lg h-screen mx-auto p-4" (if (some? product) "" " hidden"))}
         [:div {:class "flex flex-row justify-center"}
          [:div
           [:div {:class "flex flex-row place-content-between"}
            [:div
             [:p {:class "text-2xl font-locator font-thin mr-2"} name]
             [:div {:class "flex flex-row"}
              [:p {:class "border border-gray-300 py-1 px-2 pl m-1 text-sm font-locator"} (string/capitalize type)]
              [:p {:class (str "border py-1 px-2 m-1 text-sm font-locator " color)} (string/capitalize subtype-key)]]]
            [:div {:class "flex flex-col"}
             [:p {:class "flex flex-row"}
              [:span {:class "text-4xl font-locator font-thin"} price-euros]
              [:span {:class "text-m font-locator font-thin mt-[0.1rem] ml-[0.2rem] underline"} price-cents]]
             [:p {:class "text-m font-locator font-thin text-gray-500 text-right"} (str package-size)]]]
           [:p {:class "text-sm py-2 font-locator "} description]
           [:div {:class "flex flex-row content-center"}
            [:> globe-icon {:style {:height        "1.5rem" ; :> creates a Reagent component from a React one
                                    :width         "1.5rem"
                                    :margin-left   "0.5rem"
                                    :margin-right  "0.5rem"
                                    :margin-top    "auto"
                                    :margin-bottom "auto"}}]
            [:p {:class "border border-gray-300 py-1 px-2 pl m-1 text-sm font-locator"} country]]
           [:div {:class "flex flex-row content-center"}
            [:> percent-icon {:style {:height        "1.5rem" ; :> creates a Reagent component from a React one
                                      :width         "1.5rem"
                                      :margin-left   "0.5rem"
                                      :margin-right  "0.5rem"
                                      :margin-top    "auto"
                                      :margin-bottom "auto"}}]
            [:p {:class "border border-gray-300 py-1 px-2 pl m-1 text-sm font-locator"} (str alcohol-percentage "%")]]]
          [:img {:class "max-h-60 ml-8"
                 :src    image}]]])
      [:p "border-solid border-4 border-gray-500 m-2"])))

(defn home []
  [:div
   [header]
   [search-by-url-or-id]
   [product-info]])