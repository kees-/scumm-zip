(ns kees.scumm-zip.views
  (:require [kees.scumm-zip.dialogue :as d]
            [reagent.core :as r]))

(defn reload!
  "Change both atoms given to display and target conversation."
  [dialogue active]
  (fn [e]
    (let [option (.. e -target -value)]
      (reset! active option)
      (reset! dialogue (d/dialogue-zipper option)))))

(defn dialogue-select
  "Choose what conversation to display."
  [dialogue active]
  (into [:select
         {:on-change (reload! dialogue active)}]
        (for [option d/dialogues-list]
          [:option
           {:value option}
           option])))

(defn controls
  "Admin control panel."
  [dialogue active]
  [:div.controls
   [dialogue-select dialogue active]
   [:button
    {:on-click #(reset! dialogue (d/dialogue-zipper @active))}
    (char 0x21BA)]])

(defn choice-button
  "One conversation option"
  [index choice atom]
  (when-not (:hidden? choice)
    [:li
     [:button.choice
      {:on-click #(d/choose index atom)
       :class (when (:seen? choice) "seen")}
      (:heading choice)]]))

(defn then-button
  "A button activating the `then` logic of the current node, if given."
  [action atom]
  (when action
    [:li
     [:button
      {:on-click #(swap! atom action)}
      "Return"]]))

(defn choices
  "All conversation options currently open to you."
  [choices dialogue]
  (let [indexed-choices (map-indexed vector (choices dialogue))
        then (d/then dialogue)]
    (reduce into [[:ul.choices]
                  (for [[index choice] indexed-choices]
                    [choice-button index choice dialogue])
                  [(then-button then dialogue)]])))

(defn responses
  "Speaker's most recently stated lines of conversation."
  [dialogue]
  (into [:ul.responses]
        (for [response (d/responses dialogue)]
          [:li response])))

(defn root
  "Main DOM node."
  []
  (let [active (r/atom (first d/dialogues-list))
        dialogue (r/atom (d/dialogue-zipper @active))]
    (fn []
      [:<>
       [:header
        [:h1 "Hello"]]
       [:main
        [controls dialogue active]
        [:div.dialogue-heading-box (d/text dialogue)]
        [:div.dialogue-box
         [responses dialogue]
         [choices d/choices dialogue]]]
       [:footer
        [:div.outbound
         [:a
          {:href "https://github.com/kees-/scumm-zip"
           :target "_blank"}
          (char 0x27A4)]]]])))
