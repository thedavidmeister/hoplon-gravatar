(ns hoplon-gravatar.api-test
 (:require
  hoplon-gravatar.api
  [clojure.test :refer [deftest is]]))

; TESTS

(deftest ??email->hash
 (doseq [e ["foo@example.com"
            " foo@example.com"
            "foo@example.com "
            "FOO@example.com"]]
  (is (= "b48def645758b95537d4424c84d1a9ff" (hoplon-gravatar.api/email->hash e)))))

(deftest ??email->url
 (is (= "https://www.gravatar.com/avatar/b48def645758b95537d4424c84d1a9ff" (str (hoplon-gravatar.api/email->url "foo@example.com"))))
 (is
  (=
   "https://www.gravatar.com/avatar/b48def645758b95537d4424c84d1a9ff?d=mm&f=y&r=pg&s=50"
   (str (hoplon-gravatar.api/email->url "foo@example.com" :d :mm :f true :r :pg :s 50)))))

(deftest ??profile->name
 (let [p {:name {:formatted "fmt"
                 :given-name "g"
                 :family-name "f"}
          :display-name "display"
          :preferred-username "u"}
       e "foo@bar.com"]
  (is (= "fmt" (hoplon-gravatar.api/profile->name p e)))
  (let [p' (assoc-in p [:name :formatted] nil)]
   (is (= "display" (hoplon-gravatar.api/profile->name p' e)))
   (let [p'' (dissoc p' :display-name)]
    (is (= "g" (hoplon-gravatar.api/profile->name p'' e))
     (let [p''' (dissoc p'' :name)]
      (is (= "u" (hoplon-gravatar.api/profile->name p''' e)))))))
  (is (= "foo" (hoplon-gravatar.api/profile->name {} e)))
  (is (= "" (hoplon-gravatar.api/profile->name {})))))
