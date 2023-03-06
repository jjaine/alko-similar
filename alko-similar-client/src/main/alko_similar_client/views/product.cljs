(ns alko-similar-client.views.product
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [clojure.string :as string]
            ["@heroicons/react/24/outline/ReceiptPercentIcon" :as percent-icon]))

(def id (r/atom ""))

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

(def alko-url "https://www.alko.fi/tuotteet/")

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
                    region
                    label-info
                    grapes
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
                            (- price-euros)
                            (* 100)
                            (js/Math.round))
            price-cents-str (if (< price-cents 10)
                              (str "0" price-cents)
                              (str price-cents))]
        (js/console.log "color" color)
        (js/console.log "product-info" id name description package-size price type subtype beer-type label-info grapes country region package-type alcohol-percentage image similar)
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
              [:span {:class "text-m font-locator font-thin mt-[0.1rem] ml-[0.2rem] underline"} price-cents-str]]
             [:p {:class "text-m font-locator font-thin text-gray-500 text-right"} (str package-size)]]]
           [:p {:class "text-sm py-2 font-locator "} description]
           [:div {:class "flex flex-row content-center"}
            [:div {:class "bg-globe h-6 w-6 mx-2 my-auto"}]
            [:p {:class "border border-gray-300 py-1 px-2 pl m-1 text-sm font-locator"} country]
            (when (-> region
                      string/blank?
                      not)
              [:p {:class "border border-gray-300 py-1 px-2 pl m-1 text-sm font-locator"} region])
            (when (-> label-info
                      string/blank?
                      not)
              [:p {:class "border border-gray-300 py-1 px-2 pl m-1 text-sm font-locator"} label-info])]
           (when (-> grapes
                     string/blank?
                     not)
             [:div {:class "flex flex-row content-center"}
              [:div {:class "bg-grapes h-6 w-6 mx-2 my-auto bg-no-repeat"}]
              (let [grapes-split (string/split grapes #", ")]
                (for [grape grapes-split]
                  [:p {:class "border border-gray-300 py-1 px-2 pl m-1 text-sm font-locator"
                       :key grape} grape]))])
           [:div {:class "flex flex-row content-center"}
            [:> percent-icon {:style {:height        "1.5rem" ; :> creates a Reagent component from a React one
                                      :width         "1.5rem"
                                      :margin-left   "0.5rem"
                                      :margin-right  "0.5rem"
                                      :margin-top    "auto"
                                      :margin-bottom "auto"}}]
            [:p {:class "border border-gray-300 py-1 px-2 pl m-1 text-sm font-locator"} (str alcohol-percentage "%")]]
           [:div {:class "flex flex-row content-center"}
            [:div {:class "bg-search h-6 w-6 mx-2 my-auto"}]
            [:a {:class "border border-gray-300 py-1 px-2 pl m-1 text-sm font-locator underline"
                 :href (str alko-url id)
                 :target "_blank"}
             "alko.fi"]]]
          [:img {:class "max-h-60 ml-8"
                 :src    image}]]])
      [:p "border-solid border-4 border-gray-500 m-2"])))