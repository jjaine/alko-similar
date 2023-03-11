(ns alko-similar-server.product
  (:require [alko-similar-server.scraper :as scraper] 
            [ring.util.response :as rr]
            [clojure.string]))

(def image-url "https://images.alko.fi/images/cs_srgb,f_auto,t_medium/cdn/")

(defn generate-image-url
  [product]
  (let [id (:id product)
        name (:name product)
        name (-> name
                 (clojure.string/replace #" " "-")
                 (clojure.string/lower-case))
        image-url (str image-url id "/" name ".jpg")]
    image-url))

;; checks if a description has an attribute
(defn has-attribute
  [description attribute]
  (if (= description nil)
    false
    (boolean
     (clojure.string/includes?
      (clojure.string/lower-case description)
      (clojure.string/lower-case attribute)))))

;; for each matching attribute in product's description, increase the index score
(defn check-attributes
  [product attributes index]
  (if (empty? attributes)
    index
    (if (has-attribute (product :description) (first attributes))
      (recur product (rest attributes) (inc index))
      (recur product (rest attributes) index))))

;; count the scores for all products and store the result in the list of maps
(defn get-similar-scores
  [description]
  (let [attributes (->> (-> description
                            (clojure.string/split #", "))
                        (remove #(clojure.string/blank? %))
                        (map #(clojure.string/lower-case %)))]
    (->> @scraper/data
         (map
          #(assoc %1
                  :score (check-attributes %1 attributes 0)
                  :product-score (count attributes))))))

(defn get-similar
  ([product]
   (get-similar product [] nil nil))
  ([product filter-parameters]
   (get-similar product filter-parameters nil nil))
  ([product min-price max-price]
   (get-similar product [] min-price max-price))
  ([product filter-parameters min-price max-price]
   (let [description             (:description product)
         scores                  (get-similar-scores description)
         sorted                  (-> (sort-by :score scores)
                                     reverse)
         filtered                (->> sorted
                                      (filter #(not= (:id %) (:id product))))
         filtered-with-params    (loop [params filter-parameters
                                        prods  filtered]
                                   (if (empty? params)
                                     prods
                                     (let [param               (first params)
                                           filtered-with-param (filter #(= (param product) (param %)) prods)]
                                       (recur (rest params) filtered-with-param))))
         filtered-with-min-price (if (-> min-price
                                         nil?
                                         not)
                                   (filter #(>= (Float/parseFloat (:price %)) min-price)
                                           filtered-with-params)
                                   filtered-with-params)
         filtered-with-max-price (if (-> max-price
                                         nil?
                                         not)
                                   (filter #(<= (Float/parseFloat (:price %)) max-price)
                                           filtered-with-min-price)
                                   filtered-with-min-price)
         top                     (take 6 filtered-with-max-price)
         top-with-images         (->> top
                                      (map #(assoc % :image (generate-image-url %))))]
     top-with-images)))

(defn get-details
  [request]
  (if-let [id (-> request
                  :path-params
                  :product-id)]
    (let [selected-by-id  (-> (filter (comp #{id} :id) @scraper/data)
                              first)
          selected-by-ean (-> (filter (comp #{id} :ean) @scraper/data)
                              first)
          selected        (if (nil? selected-by-id)
                            selected-by-ean
                            selected-by-id)]
      (if (nil? selected)
        (rr/not-found [(str "Tuntematon tuote!")])
        (let [img-url  (generate-image-url selected)
              response (assoc selected :image img-url)]
          (rr/response response)))) 
    (rr/not-found ["Anna tuotenumero!"])))

(defn get-similars
  [request]
  (if-let [id (-> request
                  :path-params
                  :product-id)]
    (let [selected-by-id  (-> (filter (comp #{id} :id) @scraper/data)
                              first)
          selected-by-ean (-> (filter (comp #{id} :ean) @scraper/data)
                              first)
          selected        (if (nil? selected-by-id)
                            selected-by-ean
                            selected-by-id)]
      (if (nil? selected)
        (rr/not-found [(str "Product not found with id " id)])
        (let [filter-parameters (-> request
                                    :query-params
                                    (get "filter-by"))
              parameters        (if (-> filter-parameters
                                        nil?
                                        not)
                                  (->> (-> filter-parameters
                                           (clojure.string/split #","))
                                       (map keyword))
                                  [])
              min-price-param   (-> request
                                    :query-params
                                    (get "min-price"))
              min-price         (if (-> min-price-param
                                        nil?
                                        not)
                                  (Float/parseFloat min-price-param)
                                  nil)
              max-price-param   (-> request
                                    :query-params
                                    (get "max-price"))
              max-price         (if (-> max-price-param
                                        nil?
                                        not)
                                  (Float/parseFloat max-price-param)
                                  nil)
              similar           (get-similar selected parameters min-price max-price)]
          (rr/response {:similar similar}))))
    (rr/not-found ["No product id given"])))

(defn product-exists
  [id]
  (let [selected-by-id  (-> (filter (comp #{id} :id) @scraper/data)
                            first)
        selected-by-ean (-> (filter (comp #{id} :ean) @scraper/data)
                            first)
        selected        (if (nil? selected-by-id)
                          selected-by-ean
                          selected-by-id)]
    (not (nil? selected))))

(comment
  (get-details {:path-params {:product-id "942617"}})
  (get-details {:path-params {:product-id "568238"}})
  (get-details {:path-params {:product-id "3504040120133"}})
  (get-details {:path-params {:product-id "350404012013"}})
  (get-details {:path-params {:product-id "4976879987307"}})
  (get-details {:path-params {}}) 

  (get-similars {:path-params {:product-id "942617"}})
  (get-similars {:path-params {:product-id "4976879987307"}})

  (get-similars {:path-params {:product-id "942617"}
                 :query-params {"filter-by" "country"
                                "min-price" "10"
                                "max-price" "20"}})

  (get-similars {:path-params {:product-id "942617"}
                 :query-params {"min-price" "10"
                                "max-price" "10"}})

  (get-similars {:path-params {}
                 :query-params {"filter-by" "country"
                                "min-price" "10"
                                "max-price" "20"}})

  (get-similar {:description "Meripihkanruskea, keskitäyteläinen, pehmeä, piparminttuinen, kermatoffeinen, kevyen havuinen, vaniljainen",
                :package-type "pullo",
                :alcohol-percentage "30.0",
                :name "Jaloviina Hanki",
                :type "Liköörit ja Katkerot",
                :id "953514",
                :package-size "0,5 l",
                :price "19.69",
                :country "Suomi",
                :subtype "Mausteliköörit"} '(:country) 10 20)

  (let [request {:path-params {:product-id "915083"
                               :filter-by '("country")}}]
    (if-let [id (-> request
                    :path-params
                    :product-id)]
      (if-let [selected (->> (filter (comp #{id} :id) @scraper/data)
                             first)]
        (let [img-url  (generate-image-url selected)
              response (assoc selected :image img-url)]
          (if-let [filter-parameters (-> request
                                         :path-params
                                         :filter-by)]
            ((println filter-parameters)
             (println (map keyword filter-parameters))
             (let [parameters (->>  filter-parameters

                                    (map keyword))
                   similar    (get-similar selected parameters)]
               (rr/response (assoc response :similar similar))))
            (let [similar (get-similar selected [])]
              (rr/response (assoc response :similar similar)))))
        (rr/not-found [(str "Id not found " id)]))
      (rr/not-found ["No id given"])))
  )

