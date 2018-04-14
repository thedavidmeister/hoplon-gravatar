(ns ^{:hoplon/page "index.html"} pages.index
 (:require
  [hoplon.core :as h]
  [javelin.core :as j]
  elem-lib.hoplon
  syntax-highlighter.hoplon
  hoplon-gravatar.javelin))

(h/html
 (h/head
  (h/title "Hoplon Gravatar")
  (syntax-highlighter.hoplon/stylesheet))
 (h/body
  (h/div
   (elem-lib.hoplon/elem
    "Javelin profile cell"
    "Fetches the gravatar profile for the passed email and returns a cell"
    #'hoplon-gravatar.javelin/profile-cell
    [["basic email string" "hoplon.gravatar@gmail.com"]
     ["basic email cell" (j/cell= "hoplon.gravatar@gmail.com")]
     ["with gmail alias" (j/cell= "hoplon.gravatar+foo@gmail.com")]]))))
