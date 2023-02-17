(ns alko-similar-client.app
  (:require [reagent.dom :as rdom]
            [alko-similar-client.views :as views]
            [alko-similar-client.events]
            [alko-similar-client.subs]))

(defn ^:dev/after-load start
  []
  (rdom/render [views/home]
               (.getElementById js/document "root")))

(defn ^:export init
  []
  (start))