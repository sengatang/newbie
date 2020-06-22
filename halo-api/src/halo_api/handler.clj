(ns halo-api.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [settings :as settings]
            [halo-api.mongo :as mongo]
            [util.auth :as auth]
            [util.wechat :as wechat]
            [ring.adapter.jetty :as jetty]))

(s/defschema Pizza
  {:name s/Str
   (s/optional-key :description) s/Str
   :size (s/enum :L :M :S)
   :origin {:country (s/enum :FI :PO)
            :city s/Str}})

(println "========================= SEVER STARTED! ========================")

;(def auth-backend
;  (token-backend {:authfn authenticate-token}))

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "Halo-api"
                    :description "Compojure Api example"}
             :tags [{:name "api", :description "some apis"}]}}}

    (context "/wechat" []
             :tags ["wechat"]

      (GET "/check-auth"
           {:keys [headers params body] :as request}
           (def echostr (:echostr params))
           (ok echostr))

      (GET "/auth"
           {:keys [headers params body] :as request}
           (let [code (:code params)]
                (let [response
                      (client/get (str settings/wechat-baseurl "/oauth2/access_token")
                                  {:accept       :json
                                   :query-params {:appid      (:appid settings/wechat)
                                                  :secret     (:appsecret settings/wechat)
                                                  :code       code
                                                  :grant_type "authorization_code"}})]
                     (def res (json/read-str (:body response) :key-fn keyword))
                     (let [[openid access_token] [(:openid res) (:access_token res)]]
                          (println openid access_token)
                          (def user (first (mc/find-maps mongo/db "user" {:openid openid})))
                          ;(println "user" user)
                          (if (nil? user)
                            (let [user-info (wechat/get-wechat-userifo access_token openid)]
                              ;(println "user-info" user-info)
                              (def user (mc/insert-and-return mongo/db "user" {:gender (:sex user-info)
                                                                               :nickname (:nickname user-info)
                                                                               :city (:cty user-info)
                                                                               :country (:country user-info)
                                                                               :openid (:openid user-info)
                                                                               :avatar-url (:headimgurl user-info)}))))
                          ;(println "userid" (:_id user))
                          (ok (auth/get-or-generate-token (:_id user))))))))

    (context "/base" []
      :tags ["base"]

      (POST "/logout"
           {:keys [headers params body] :as request}
        (let [user (auth/authenticate-token (get headers "authorization"))]
          ;delete token
          (mc/remove mongo/db "token" { :user_id (:_id user)}))
        (ok {:msg "success"}))

      (POST "/order"
            {:keys [headers params body] :as request}
            (let [user (auth/authenticate-token (get headers "authorization"))]
              (let [order (mc/insert-and-return mongo/db "order" {:user_id (:_id user)
                                                                  :item (:item params)
                                                                  :fee (:fee params)
                                                                  :status "unpaid"})]
                (mc/update mongo/db "user" {:_id (:_id user)} {$push {:orders (:_id order)}})
                (ok {:item (:item order)
                     :fee (:fee order)
                     :status (:status order)})))))));find user,return token


(defn -main
  [& args]
  (jetty/run-jetty app {:port 80}))
