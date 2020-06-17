### Clojure 新人项目
[![Build Status](https://travis-ci.org/NickYadance/jiliguala-newbie.svg?branch=master)](https://travis-ci.org/NickYadance/jiliguala-newbie)
[![codecov](https://codecov.io/gh/NickYadance/jiliguala-newbie/branch/master/graph/badge.svg)](https://codecov.io/gh/NickYadance/jiliguala-newbie)
- [ ] （可选）完成基础语法https://github.com/functional-koans/clojure-koans.git的所有练习
- [ ] 基于[compojure-api + ring](https://github.com/metosin/compojure-api)搭建一个HTTP Rest Server，
提供接口输出Hello World，使用Postman进行测试
- [ ] 接入log4j2，为线上/线下提供不同的配置文件
- [ ] 提供一个接口，返回登录用户的微信信息，要求实现静默授权,可以直接使用微信公众平台测试号进行测试。(建议使用微信开发者工具在本地开发)
- [ ] 本地启动一个Mongodb，手动创建DB。准备一个edn配置文件，接入monger，在项目启动时读取配置文件并连接到Mongodb
- [ ] 在db中构造一个用户-订单关系，提供用户注册、注销、登录、下单等接口，使用Postman测试
- [ ] 编写单测，接入travis ci 和 cloverage。生成单测报告，要求code coverage在50%以上