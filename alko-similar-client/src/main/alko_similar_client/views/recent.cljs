(ns alko-similar-client.views.recent
  (:require [re-frame.core :as rf] 
            [alko-similar-client.views.popular :as details]))

(defn products
  [recent-ids recent-products]
  (when (some? recent-ids)
    (doseq [p recent-ids]
      (when (nil? (get recent-products p))
        (rf/dispatch [:get-recent-product p])))
    (when (= (count recent-ids) (count recent-products))
      [:div {:class "flex flex-col bg-gray-100 pb-2"}
       [:h2 {:class "font-bold text-center text-xl my-2 pt-1"} "Viimeksi haetut"]
       (details/product-details recent-ids recent-products)])))