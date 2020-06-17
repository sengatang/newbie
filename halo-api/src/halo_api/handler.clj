(ns halo-api.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))

(s/defschema Pizza
  {:name s/Str
   (s/optional-key :description) s/Str
   :size (s/enum :L :M :S)
   :origin {:country (s/enum :FI :PO)
            :city s/Str}})

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "Halo-api"
                    :description "Compojure Api example"}
             :tags [{:name "api", :description "some apis"}]}}}

    (context "/api" []
      :tags ["api"]

      (GET "/plus" []
        :return {:result Long}
        :query-params [x :- Long, y :- Long]
        :summary "adds two numbers together"
        (ok {:result (+ x y)}))

      (POST "/echo" []
        :return Pizza
        :body [pizza Pizza]
        :summary "echoes a Pizza"
        (ok pizza))

       (GET "/hello" []
          :return s/Str
          :summary "hello world"
          (ok "Hello World"))
     )
    (context "/base" []
             :tags ["base"]

      (GET "/auth" []
            :return s/Str
            :query-params [signature :- s/Str timestamp :- s/Str nonce :- s/Str echostr :-]
            :summary "wechat auth"
            (def token "senga")
           (println "signature: " signature)
           (println "timestamp: " timestamp)
           (println "nonce: " nonce)
           (println "echostr: " echostr)

            ;(def arr (list token timestamp nonce))
            ;(sort arr)
            (ok echostr)
            ))
    )
  )

