(ns ^{:hoplon/page "index.html"} pages.index
 (:require
  [hoplon.core :as h]
  [javelin.core :as j]))

(h/html
 (h/head
  (h/title "Hoplon Gravatar"))
 (h/body
  (h/div
   "Hi")))
