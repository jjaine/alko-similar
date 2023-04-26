(ns alko-similar-server.scraper
  (:require [clojure.java.io :as io]
            [dk.ative.docjure.spreadsheet :as ss]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-http.client :as http-client]
            [ring.util.response :as rr]))

(def alko-url "https://www.alko.fi/INTERSHOP/static/WFS/Alko-OnlineShop-Site/-/Alko-OnlineShop/fi_FI/Alkon%20Hinnasto%20Tekstitiedostona/alkon-hinnasto-tekstitiedostona.xlsx")

(defn- copy [uri path]
  (println "Copying file from" uri "to" path) 
  (let [file (-> (http-client/get alko-url
                                  {:headers {:user-agent ""}
                                   :as :stream})
                 :body)]
    (io/copy file (java.io.File. path))))

(def data (atom nil))

(defn process-data
  []
  (->> (ss/load-workbook "resources/alko.xlsx")
       (ss/select-sheet "Alkon Hinnasto Tekstitiedostona")
       (ss/select-columns
        {:A :id
         :B :name
         :D :package-size
         :E :price
         :I :type
         :J :subtype
         :L :beer-type
         :M :country
         :N :region
         :P :label-info
         :R :grapes
         :S :description
         :T :package-type
         :V :alcohol-percentage
         :AD :ean})
       (drop 4)
       (remove nil?)))

(defn scrape-data []
  (println "Scraping data...")
  (let [current-time     (t/now)
        prev-scrape-time (if (.exists (io/as-file "resources/prev-scrape-time.txt"))
                          (f/parse (slurp "resources/prev-scrape-time.txt"))
                           nil)]
    ; check if prev time is on prev day
    (if (or (nil? prev-scrape-time)
            (not= (t/day prev-scrape-time) (t/day current-time)))
      (do
        (copy alko-url "resources/alko.xlsx")
        (reset! data (process-data))
        (spit "resources/prev-scrape-time.txt" (f/unparse (f/formatters :date) current-time))
        (rr/response {:operation "scraped"
                      :info      (str "Scraped at " (f/unparse (f/formatters :date) current-time))}))
      (do
        (reset! data (process-data))
        (rr/response {:operation "none"
                      :info      "Already scraped today"})))))

(comment
  (scrape-data)
  @data
  (reset! data nil)
  (reset! data (process-data))
  )
