(ns hoplon-gravatar.gmail-test
 (:require
  hoplon-gravatar.gmail
  [clojure.test :refer [deftest is]]))

(deftest ??should-normalize-email?
 (doseq [[i o] [["foo@bar.com" false]
                ["f.oo@bar.com" true]
                ["f+oo@bar.com" true]]]
  (is (= o (hoplon-gravatar.gmail-test/should-normalize-email? i)))))

(deftest ??normalize-email
 (doseq [e ["foo+bar@x.x"
            "f.o.o+b.a.r@x.x"
            "foo@x.x"]]
  (is (= "foo@x.x" (hoplon-gravatar.gmail-test/normalize-email e)))))
