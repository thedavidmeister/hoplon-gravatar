(ns hoplon-gravatar.javelin
 (:require
  goog.net.Jsonp
  hoplon-gravatar.api
  hoplon-gravatar.gmail
  ajax.core
  clojure.walk
  medley.core
  camel-snake-kebab.core
  [javelin.core :as j]
  [hoplon.core :as h]
  [cljs.spec.alpha :as spec]))

(defn -attempt-profile!
 [result-cell email]
 (let [url (str "https://www.gravatar.com/" (hoplon-gravatar.api/email->hash email) ".json")]
  ; google has no error handling! gross 404's in the console everywhere :(
  (.send (goog.net.Jsonp. url)
   ""
   (fn [r]
    (let [kebab-keys (partial medley.core/map-keys camel-snake-kebab.core/->kebab-case-keyword)]
     (reset! result-cell
      (-> r
       js->clj
       (get "entry")
       first
       kebab-keys
       (update :name kebab-keys))))))))

(defn profile-cell
 [email-cell]
 (let [email (j/formula-of [email-cell] email-cell)]
  (j/with-let [result-cell (j/cell nil)]
   (h/do-watch email
    (fn [_ n]
     (reset! result-cell nil)
     (when (string? n)
      (-attempt-profile! result-cell n)
      ; normalize aliases and other oddness
      (when (hoplon-gravatar.gmail/maybe-alias? n)
       (-attempt-profile! result-cell (hoplon-gravatar.gmail/normalize-email-alias n)))))))))
