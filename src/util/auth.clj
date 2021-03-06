(ns util.auth
    (:require [halo_api.settings :as settings]
              [crypto.random :refer [base64]]
              [monger.collection :as mc]
              [halo-api.mongo :as mongo]
              [buddy.core.nonce :as nonce]
              [buddy.core.codecs :as codecs]
              [buddy.auth :refer [throw-unauthorized]]))

(defn gen-token
      []
      (let [randomdata (nonce/random-bytes 16)]
           (codecs/bytes->hex randomdata)))

(defn save-token! [user-id]
      (let [[token this-time]  [(gen-token) (.getTime (java.util.Date.))]]
           (:key (mc/insert-and-return mongo/db "token" {:key token
                                                         :user_id user-id
                                                         :expires (+ this-time settings/token-expire-time)}))))
(defn get-or-generate-token
      [user-id]
      (if (not (nil? user-id))
        (let [record (last (mc/find-maps mongo/db "token" {:user_id user-id}))]
          (if (< (.getTime (java.util.Date.)) (:expires record 0))
            (:key record)
            (save-token! user-id)))))

(defn authenticate?
      [token]
      (let [record (first (mc/find-maps mongo/db "token" {:key token}))]
           (if (< (.getTime (java.util.Date.)) (:expires record 0))
               (:user_id record))))

(defn authenticate-token
      [token]
      #_(println "authenticate-token" token)
      (let [user_id (authenticate? token)]
        #_(println "userid" user_id)
        (if (nil? user_id)
          (throw-unauthorized)
          (mc/find-map-by-id mongo/db "user" user_id))))
