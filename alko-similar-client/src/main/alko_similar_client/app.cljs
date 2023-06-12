(ns alko-similar-client.app
  (:require [reagent.dom :as rdom]
            [alko-similar-client.app-view :as view]
            [alko-similar-client.events]
            [alko-similar-client.subs]))

(defn ^:dev/after-load start
  []
  (rdom/render [view/app]
               (.getElementById js/document "root")))

(defn ^:export init
  []
  (start))
