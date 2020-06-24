(ns halo-api.mongo
    (:require [halo_api.settings :as settings]
              [monger.core :as mg]
              [monger.conversion :refer [from-db-object]])
    (:import org.bson.types.ObjectId))

(def conn (mg/connect settings/mongo))
(def db (mg/get-db conn "monger-test"))



