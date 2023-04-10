# SCUMM engine-style dialogue trees

A data structure and webpage viewer for conversation dialogue trees like the ones commonly found in old LucasArts/SCUMM adventure games, RPGs like Elder Scrolls or Baldur's Gate, etc.

Copied from [Making an Adventure Game with Clojure - Bryce Covert](https://github.com/brycecovert/clojurewest-use-lisp-with-game-2016) ([see video](https://www.youtube.com/watch?v=lql2yFXzKUs) from Clojure/west 2016). This makes great use of zippers!

## Features

- Traverse up, down, and across a branching conversation.
- Run arbitrary logic to advance the state of the tree, e.g unlocking or hiding conversation options.

## Dev

Run and see for yourself with these commands:

```shell
gh repo clone kees-/scumm-zip && cd scumm-zip
npm i
npm run watch
open http://localhost:8280/
```

The conversation trees are found in [`kees.scumm-zip.dialogue/dialogues`](src/kees/scumm_zip/dialogue.cljs).
