(ns util.wechat
    (:require [halo_api.settings :as settings]
              [clj-http.client :as client]
              [clojure.data.json :as json]))

(defn get-wechat-userifo [access_token openid]
      (json/read-str (:body (client/get (str settings/wechat-baseurl "/userinfo")
                                        {:accept       :json
                                         :query-params {:access_token access_token
                                                        :openid       openid
                                                        :lang         "zh_CN"}}))
                     :key-fn keyword))