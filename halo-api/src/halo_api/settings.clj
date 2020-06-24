(ns halo_api.settings)

(def wechat-baseurl "https://api.weixin.qq.com/sns")
(def wechat {:appid "wx266e47607c3efa09"
             :appsecret "7e4cebe9d919150ef7732d86f16dccc4"})

(def mongo {:host "http://senga.5gzvip.idcfengye.com"
            :port 27017})

(def token-expire-time (* 60 60 24 15 1000))                ;15 days



