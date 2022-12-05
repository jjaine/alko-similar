(ns alko-similar-server.scraper
  (:require [clojure.java.io :as io]
            [dk.ative.docjure.spreadsheet :as ss]
            [clj-time.core :as t]
            [clj-time.format :as f]))

(def alko-url "https://www.alko.fi/INTERSHOP/static/WFS/Alko-OnlineShop-Site/-/Alko-OnlineShop/fi_FI/Alkon%20Hinnasto%20Tekstitiedostona/alkon-hinnasto-tekstitiedostona.xlsx")

(defn- copy [uri file]
  (with-open [in (io/input-stream uri)
              out (io/output-stream file)]
    (io/copy in out)))

(defn scrape-data []
  (let [current-time (t/now)
        prev-scrape-time (f/parse (slurp "resources/prev-scrape-time.txt"))]
    ; check if prev time is on prev day
    (if (or (nil? prev-scrape-time)
            (not= (t/day prev-scrape-time) (t/day current-time)))
      ((copy alko-url "resources/alko.xlsx")
       (spit "resources/prev-scrape-time.txt" (f/unparse (f/formatters :date) current-time)))
      (println "Already scraped today"))))

(def data
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

