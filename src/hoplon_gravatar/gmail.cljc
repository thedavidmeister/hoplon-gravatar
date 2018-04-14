(ns hoplon-gravatar.gmail)

; https://gmail.googleblog.com/2008/03/2-hidden-ways-to-get-more-from-your.html
(defn maybe-alias?
 "True if the local part of an email contains a period . or plus + character.
  Gmail ignores these characters but Gravatar needs an md5 hash so we may get a
  false negative on email aliases sent to Gravatar when working with Gmail email
  addresses."
 [email]
 (let [[local & rest] (clojure.string/split email #"@")]
  (or
   (clojure.string/includes? local "+")
   (clojure.string/includes? local "."))))

(defn normalize-email-alias
 "Strips out period . and plus + characters from an email. See `maybe-alias?`."
 [email]
 (let [[local & rest] (clojure.string/split email #"@")]
  (clojure.string/join
   "@"
   (into
    (-> local
     ; drop everything after +'s
     (clojure.string/replace #"\+.*" "")
     ; drop all .'s
     (clojure.string/replace "." "")
     vector)
    rest))))
