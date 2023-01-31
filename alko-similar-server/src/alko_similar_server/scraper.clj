(ns alko-similar-server.scraper
  (:require [clojure.java.io :as io]
            [dk.ative.docjure.spreadsheet :as ss]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [ring.util.response :as rr]))

(def alko-url "https://www.alko.fi/INTERSHOP/static/WFS/Alko-OnlineShop-Site/-/Alko-OnlineShop/fi_FI/Alkon%20Hinnasto%20Tekstitiedostona/alkon-hinnasto-tekstitiedostona.xlsx")

(defn- copy [uri file]
  (with-open [in  (io/input-stream uri)
              out (io/output-stream file)]
    (io/copy in out)))

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
         :M :country
         :S :description
         :T :package-type
         :V :alcohol-percentage})
       (remove nil?)))

(defn scrape-data []
  (println "Scraping data...")
  (let [current-time (t/now)
        prev-scrape-time (f/parse (slurp "resources/prev-scrape-time.txt"))]
    ; check if prev time is on prev day
    (if (or (nil? prev-scrape-time)
            (not= (t/day prev-scrape-time) (t/day current-time)))
      (do
        (copy alko-url "resources/alko.xlsx")
        (reset! data (process-data))
        (spit "resources/prev-scrape-time.txt" (f/unparse (f/formatters :date) current-time))
        (rr/response {:operation "scraped"
                      :info (str "Scraped at " (f/unparse (f/formatters :date) current-time))}))
       (do
         (reset! data (process-data))
         (rr/response {:operation "none"
                          :info "Already scraped today"})))))

(comment
  (scrape-data)
  @data
  (reset! data nil)
  (reset! data (process-data))
  )
