(ns halo-api.mongo
    (:require [monger.core :as mg]
              [settings :as settings]
              [monger.collection :as mc]
              [monger.conversion :refer [from-db-object]])
    (:import org.bson.types.ObjectId))

(def conn (mg/connect settings/mongo))
(def db (mg/get-db conn "monger-test"))

(defn insert [cl params]
      (mc/insert db cl params))

(defn find [cl params]
      (from-db-object (mc/find db cl params) true))

(defn get-by-id [cl id]
      (mc/find-map-by-id db cl id))



;db.collection.find( { field1: { $elemMatch: { one: 1 } } } );


