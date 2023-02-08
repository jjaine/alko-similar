(ns alko-similar-server.similar.product
  (:require [alko-similar-server.scraper :as scraper]
            [clojure.pprint :refer [pprint]]
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

(defn get-details
  [request]
  #_(pprint request)
  (if-let [id (-> request
                  :path-params
                  :product-id)]
    (if-let [selected (->> (filter (comp #{id} :id) @scraper/data)
                           first)]
      (let [img-url  (generate-image-url selected)
            response (assoc selected :image img-url)]
        (rr/response response))
      (rr/not-found (str "Id not found " id)))
    (rr/not-found "No id given")))

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
          #(assoc %1 :score (check-attributes %1 attributes 0))))))

(defn get-similar
  [product]
  (let [description     (:description product)
        scores          (get-similar-scores description)
        sorted          (-> (sort-by :score scores)
                            reverse)
        filtered        (->> sorted
                             (filter #(not= (:id %) (:id product))))
        top             (take 10 filtered)
        top-with-images (->> top
                             (map #(assoc % :image (generate-image-url %))))]
    top-with-images))

(comment
  (get-details {:path-params {:product-id "915083"}})
  (let [product (get-details {:path-params {:product-id "915083"}})
        similar (get-similar (:body product))]
    (pprint similar)))