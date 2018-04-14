(ns hoplon-gravatar.javelin
 (:require
  goog.net.Jsonp
  gravatar.api
  wheel.email.spec
  ajax.core
  clojure.walk
  medley.core
  camel-snake-kebab.core
  [javelin.core :as j]
  [hoplon.core :as h]
  [cljs.spec.alpha :as spec]))

(defn -attempt-profile!
 [result-cell email]
 (let [url (str "https://www.gravatar.com/" (gravatar.api/email->hash email) ".json")]
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
 (j/with-let [result-cell (j/cell nil)]
  (h/do-watch email-cell
   (fn [_ n]
    (reset! result-cell nil)
    (when (spec/valid? :wheel.email/email n)
     (-attempt-profile! result-cell n)
     ; normalize aliases and other oddness
     (when (gravatar.api/should-normalize-email? n)
      (-attempt-profile! result-cell (gravatar.api/normalize-email n))))))))
