(ns alko-similar-server.scraper
  (:require [clojure.java.io :as io]
            [dk.ative.docjure.spreadsheet :as ss]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-http.client :as http-client]
            [ring.util.response :as rr]
            [clojure.string :as str]))

(def alko-url "https://www.alko.fi/INTERSHOP/static/WFS/Alko-OnlineShop-Site/-/Alko-OnlineShop/fi_FI/Alkon%20Hinnasto%20Tekstitiedostona/alkon-hinnasto-tekstitiedostona.xlsx")

(defn- copy [uri path]
  (println "Copying file from" uri "to" path)
  (let [res (http-client/get alko-url
                             {:headers {:user-agent ""}
                              :as :stream})]
    (if (and (= (:status res) 200)
             (str/includes? (get (:headers res) "Content-Type") "document"))
      (let [file (:body res)]
        (io/copy file (java.io.File. path))
        true)
      (println "Can't open workbook - unsupported file type:" (get (:headers res) "Content-Type")))))

(def data (atom nil))

(defn process-data
  []
  (if (.exists (io/as-file "resources/alko.xlsx"))
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
         (remove nil?))
    nil))

(defn scrape-data []
  (println "Scraping data...")
  (let [current-time     (t/now)
        prev-scrape-time (if (.exists (io/as-file "resources/prev-scrape-time.txt"))
                          (f/parse (slurp "resources/prev-scrape-time.txt"))
                           nil)]
    ; check if prev time is on prev day
    (if (or (nil? prev-scrape-time)
            (not= (t/day prev-scrape-time) (t/day current-time)))
      (let [scrape-status (copy alko-url "resources/alko.xlsx")
            new-data      (process-data)]
        (if (nil? new-data) 
          (rr/response {:operation "failed"
                        :info      "Failed to scrape data, file did not exist"})
          (do (reset! data new-data)
              (if scrape-status
                (do (spit "resources/prev-scrape-time.txt" (f/unparse (f/formatters :date) current-time))
                    (rr/response {:operation "scraped"
                                  :info      (str "Scraped at " (f/unparse (f/formatters :date) current-time))}))
                (rr/response {:operation "updated"
                              :info      (str "Updated from disk at " (f/unparse (f/formatters :date) current-time) ", previous scrape at " (f/unparse (f/formatters :date) prev-scrape-time))})))))
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
