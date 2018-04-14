(ns gravatar.api
 (:require
  hash.md5
  gravatar.spec
  cemerick.url
  [clojure.spec.alpha :as spec]
  [clojure.test :refer [deftest is]]))

; https://webapps.stackexchange.com/questions/26053/gmail-address-with-within-the-recipient-name
(defn should-normalize-email?
 [email]
 (let [[local & rest] (clojure.string/split email #"@")]
  (or
   (clojure.string/includes? local "+")
   (clojure.string/includes? local "."))))

(defn normalize-email
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

(defn email->hash
 [email]
 (-> email
  clojure.string/trim
  clojure.string/lower-case
  hash.md5/md5))

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

; TESTS

(deftest ??should-normalize-email?
 (doseq [[i o] [["foo@bar.com" false]
                ["f.oo@bar.com" true]
                ["f+oo@bar.com" true]]]
  (is (= o (should-normalize-email? i)))))

(deftest ??normalize-email
 (doseq [e ["foo+bar@x.x"
            "f.o.o+b.a.r@x.x"
            "foo@x.x"]]
  (is (= "foo@x.x" (normalize-email e)))))

(deftest ??email->hash
 (doseq [e ["foo@example.com"
            " foo@example.com"
            "foo@example.com "
            "FOO@example.com"]]
  (is (= "b48def645758b95537d4424c84d1a9ff" (email->hash e)))))

(deftest ??email->url
 (is (= "https://www.gravatar.com/avatar/b48def645758b95537d4424c84d1a9ff" (str (email->url "foo@example.com"))))
 (is
  (=
   "https://www.gravatar.com/avatar/b48def645758b95537d4424c84d1a9ff?d=mm&f=y&r=pg&s=50"
   (str (email->url "foo@example.com" :d :mm :f true :r :pg :s 50)))))

(deftest ??profile->name
 (let [p {:name {:formatted "fmt"
                 :given-name "g"
                 :family-name "f"}
          :display-name "display"
          :preferred-username "u"}
       e "foo@bar.com"]
  (is (= "fmt" (profile->name p e)))
  (let [p' (assoc-in p [:name :formatted] nil)]
   (is (= "display" (profile->name p' e)))
   (let [p'' (dissoc p' :display-name)]
    (is (= "g" (profile->name p'' e))
     (let [p''' (dissoc p'' :name)]
      (is (= "u" (profile->name p''' e)))))))
  (is (= "foo" (profile->name {} e)))
  (is (= "" (profile->name {})))))
