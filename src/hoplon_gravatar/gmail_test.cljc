(ns hoplon-gravatar.gmail-test
 (:require
  hoplon-gravatar.gmail
  [clojure.test :refer [deftest is]]))

(deftest ??maybe-alias?
 (doseq [[i o] [["foo@bar.com" false]
                ["f.oo@bar.com" true]
                ["f+oo@bar.com" true]]]
  (is (= o (hoplon-gravatar.gmail/maybe-alias? i)))))

(deftest ??normalize-email-alias
 (doseq [e ["foo+bar@x.x"
            "f.o.o+b.a.r@x.x"
            "foo@x.x"]]
  (is (= "foo@x.x" (hoplon-gravatar.gmail/normalize-email-alias e)))))
