(ns hoplon-gravatar.api
 (:require
  md5.core
  hoplon-gravatar.spec
  cemerick.url
  [clojure.spec.alpha :as spec]))

(defn email->hash
 [email]
 (-> email
  clojure.string/trim
  clojure.string/lower-case
  md5.core/string->md5-hex))

; https://en.gravatar.com/site/implement/images/
(defn email->url
 ([email & {:keys [s size d default-image f force-default r rating] :as config}]
  {:pre [(spec/valid? (spec/nilable :gravatar/config) config)]}
  ; https://en.gravatar.com/site/implement/hash/
  (let [s (or s size)
        d (or d default-image)
        f (or f force-default)
        r (or r rating)]
   (->
    (cemerick.url/url
     (str "https://www.gravatar.com/avatar/" (email->hash email)))
    (assoc :query
     (merge
      (when s {:s s})
      (when d {:d (if (keyword? d) (name d) d)})
      (when f {:f "y"})
      (when r {:r (name r)})))))))

(defn profile->name
 ([profile] (profile->name profile ""))
 ([profile email]
  (or
   (-> profile :name :formatted)
   (:display-name profile)
   (-> profile :name :given-name)
   (:preferred-username profile)
   (first (clojure.string/split email #"@")))))
