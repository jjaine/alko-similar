(ns alko-similar-client.views.popular
  (:require [re-frame.core :as rf]
            [clojure.walk :as walk]
            [alko-similar-client.views.similar :as details]))

(defn product-details
  [ids products]
  [:div {:class "flex flex-row flex-wrap justify-center"}
   (for [id ids]
     (when (some? (get products id))
       (let [product (get products id)
             product-keywords (walk/keywordize-keys product)
             {:keys [id
                     name
                     package-size
                     price type
                     subtype
                     beer-type
                     country
                     image]} product-keywords]
         [:div {:class  "flex flex-col w-36 m-1 justify-between cursor-pointer bg-white hover:outline hover:outline-gray-300"
                :on-click #(do (rf/dispatch [:get-product id])
                               (rf/dispatch [:get-similar id])
                               (rf/dispatch [:log-product id]))
                :key id}
          [:div
           (details/product-type type subtype beer-type)
           (details/product-name name)]
          [:div
           (details/product-country country)
           [:div {:class "flex flex-col relative"}
            [:div {:class "flex flex-col absolute right-0 px-2 py-1 mr-2 bg-gray-100/80 rounded-lg space-y-[-0.5rem]"}
             (details/product-price price)
             (details/product-package-size package-size)]
            [:div {:class "flex flex-col items-center h-[15rem] justify-end flex-wrap"}
             [:img {:class "max-h-[15rem] max-w-[8rem] object-contain pb-2 px-1"
                    :src   image}]]
            (details/product-link id)]]])))])

(defn products
  [popular-ids popular-products]
  (when (some? popular-ids)
    (doseq [p popular-ids]
      (when (nil? (get popular-products p))
        (rf/dispatch [:get-popular-product p])))
    (when (= (count popular-ids) (count popular-products))
      [:div {:class "flex flex-col bg-alko-gray pb-4"}
       [:h2 {:class "font-bold text-center text-xl my-2 pt-1 text-white"} "Suosituimmat"]
       (product-details popular-ids popular-products)])))