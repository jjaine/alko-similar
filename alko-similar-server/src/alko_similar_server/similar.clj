(ns alko-similar-server.similar
  (:require [alko-similar-server.scraper :as scraper]))

(defn get-similar [id]
  (let [selected-wine (->> (filter (comp #{id} :id) scraper/data)
                           first)
        description (:description selected-wine)]
    (println description)))
