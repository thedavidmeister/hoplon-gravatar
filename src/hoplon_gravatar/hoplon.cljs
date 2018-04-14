(ns gravatar.hoplon
 (:require
  auth.data
  gravatar.data
  gravatar.api
  gravatar.javelin
  [hoplon.core :as h]
  [javelin.core :as j]))

(defn picture
 [email & {:keys [d default-image] :as args}]
 (let [; 2x size for retina display
       s (* 2 auth.data/face-size)
       d (or d default-image gravatar.data/default-image)
       email->url (fn [e]
                   (apply
                    (partial gravatar.api/email->url e :s s :d d)
                    (dissoc args :default-image :d)))
       ; https://github.com/thedavidmeister/estimate-work/issues/3623
       url (j/formula-of
            [email]
            (when email (email->url email)))
       normalize-url (j/formula-of
                      [url email]
                      (when (and url (gravatar.api/should-normalize-email? email))
                       (email->url (gravatar.api/normalize-email email))))
       ; if there is a normalized email, the default must be blank for url
       url' (j/cell= (if normalize-url (assoc-in url [:query :d] "blank") url))]
  (h/div
   :class "face"
   :garden (j/cell=
            (when url'
             {:background-image
              (str
               "url(\"" url' "\")"
               ; provide a fallback based on the normalized email
               (when normalize-url
                (str ", url(\"" normalize-url "\")")))})))))

(defn username
 [email]
 (let [profile (gravatar.javelin/profile-cell email)]
  (h/span
   :class "username"
   (j/cell= (gravatar.api/profile->name profile email)))))
