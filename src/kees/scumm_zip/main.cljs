(ns kees.scumm-zip.main
  (:require [reagent.dom.client :as client]
            [kees.scumm-zip.views :as views]))

(defonce root
  (client/create-root (js/document.getElementById "root")))

(defn ^:dev/after-load mount-ui []
  (client/render root [views/root]))

(defn ^:export boot [& _]
  (mount-ui))
