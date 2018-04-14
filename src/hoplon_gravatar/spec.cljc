(ns hoplon-gravatar.spec
 (:require
  [clojure.spec.alpha :as spec]))

(spec/def :gravatar/size (spec/and int? #(<= 1 % 2048)))
(spec/def :gravatar/s :gravatar/size)

(spec/def :gravatar/default-image
 (spec/or
  :preset #{:404 :mm :identicon :monsterid :wavatar :retro :robohash :blank}
  :url string?))
(spec/def :gravatar/d :gravatar/default-image)

(spec/def :gravatar/force-default boolean?)
(spec/def :gravatar/f :gravatar/force-default)

(spec/def :gravatar/rating #{:g :pg :r :x})
(spec/def :gravatar/r :gravatar/rating)

(spec/def :gravatar/config
 (spec/keys
  :opt-un [:gravatar/size
           :gravatar/s
           :gravatar/default-image
           :gravatar/d
           :gravatar/force-default
           :gravatar/f
           :gravatar/rating
           :gravatar/r]))
