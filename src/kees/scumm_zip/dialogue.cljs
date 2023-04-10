(ns kees.scumm-zip.dialogue
  (:require [clojure.zip :as z]))

;; ========== LOGIC ============================================================
(defn see
  "For greying out options and other logic."
  [z]
  (-> z (z/edit assoc :seen? true)))

(defn back
  "Return to the previous conversation node."
  [z]
  (-> z see z/up))

(defn add
  "Closure for unlocking given dialogue options adjacent to the one selected."
  [items]
  (fn [z]
    (back (if-not (:seen? (z/node z))
            (reduce z/insert-right z (reverse items))
            z))))

(defn text
  "The text you speak when a choice is selected, defaulting to the heading."
  [zipper]
  (let [possibilities (some-fn :speech :heading)]
    (-> @zipper z/node possibilities (or "…"))))

(defn choices
  "Your current options for what to say."
  [zipper]
  (-> @zipper z/node :choices))

(defn responses
  "Get what the speaker says to your choice."
  [zipper]
  (-> @zipper z/node :run (or ["…"])))

(defn then
  "What to do with the 'last option' button, typically return to the parent."
  [zipper]
  (-> @zipper z/node :then))

(defn choose
  "Refocus the zipper on the node representing your conversation choice."
  [index atom]
  (let [actions (concat (repeat index z/right) [z/down])]
    (swap! atom (apply comp actions))))

(defmulti dialogues
  "Each conversation tree."
  keyword)

(defn dialogue-zipper
  "Coerce a conversation name's corresponding tree into a zipper."
  [option]
  (z/zipper
   (some-fn :choices :then)
   :choices
   (fn [node choice] (assoc node :choices choice))
   (dialogues option)))

;; ========== CONTENT ==========================================================
(defmethod dialogues :palace
  []
  {:run ["Hail, enchantress."
         "Allow me respite on your island."
         "My travels have been long."]
   :choices [{:heading "Welcome."
              :speech "Welcome, one of the sea. From a far distance I have seen you swim to my palace. Your vigor is possessed by no man."
              :run ["No beast am I, nor any more mortal."
                    "An herb has changed me. From Euboea I trace my power,"
                    "A god made by that which I travel."]
              :then back
              :choices [{:heading "What is your name?"
                         :run ["Glaucus, of a village well known as far"
                               "As that channel which separates my home is wide."]
                         :then (add [{:heading "And you come to me?"
                                      :speech "And to me you come, seeking the command of herbs and potions I possess, known as the sea is vast?"
                                      :run ["Yes."]
                                      :then back}])}
                        {:heading "Like spiderman?"
                         :speech "Oh, like spiderman?"
                         :run ["Well duh sure like that actually"
                               "But spiderman? That's what came to your mind?"]
                         :then back}
                        {:heading "What do you need?"
                         :speech "I make my island home yours. Come rest and eat, and we will discuss what you need."
                         :run ["I haven't gotten that far yet. Making this dialogue thingy took all day. But the implementation is straightforward and not very hacky."
                               "The data structure and application were made for each other!"]
                         :then back}]}]})

(defmethod dialogues :generic
  []
  {:run ["What are you doing here?"]
   :choices [{:heading "Clearly not much."
              :then back}]})

(defmethod dialogues :default
  []
  {:run ["You shouldn't be here."]})

(def dialogues-list
  "Plain string list of known conversations."
  (->> dialogues methods keys (map name) (remove #(= % "default"))))
