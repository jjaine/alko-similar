(ns alko-similar-client.app
  (:require [reagent.dom :as rdom]))

(defn app
  []
  [:div "Alko similar app"])

(defn ^:dev/after-load start
  []
  (rdom/render [app]
               (.getElementById js/document "root")))

(defn ^:export init
  []
  (start))