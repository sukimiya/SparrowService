# SparrowService

A Double Authorization CMS, can enable you have both admin page and OAuth REST Services.
Basic Service Framework with RESTful, OAuth, MongoDB. And reactive. 
* RESTful Services
* OAuth access control
* Easy NoSQL coding
* Reactive code support

projects:
RP Services: nest
DB Module:  crud
OAuth Services: auth(integrated with nest)

##Nest
path: /

##OAuth 
path: /oauth

Test case:

1.Authorize End User and client get code(with autoApprove=true)
```
http://localhost:8089/oauth/authorize?client_id=<clientid>&response_type=code&redirect_uri=<redirect_some_uri>&scope=read write&state=something&grant_type=authorization_cod
```
Login as:<username>
header:
```
Access-Control-Allow-Credentials: true
Access-Control-Allow-Methods: *
Access-Control-Max-Age: 3600
Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept, Key, Authorization
Cache-Control: no-store
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
X-Frame-Options: DENY
Location: http://e2x.io/?code=Nfbx84&state=something
Content-Language: zh-CN
Content-Length: 0
Date: Thu, 11 Apr 2019 18:52:08 GMT
```
Request Header
```$xslt
Authorization: Basic c3VraW1peWE6MTk1NDcwNDc=
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36
Accept: */*
Accept-Encoding: gzip, deflate, br
Accept-Language: zh-CN,zh;q=0.9,en;q=0.8
Cookie: JSESSIONID=81744C5787EDBD148DBDD593E21FAF7A
Note: XHR automatically adds headers like Accept, Accept-Language, Cookie, User-Agent, etc.
```

2.Get token
```
http://localhost:8089/oauth/token?code=<code>&client_id=<clientid>&client_secret=<secret>&redirect_uri=<redirect_some_uri>&grant_type=authorization_code
Need Login as:<clientid>
```

example:
```
Set-Cookie: JSESSIONID=3D89B63593696522A6557E40977433BE; Path=/; HttpOnly
Access-Control-Allow-Origin: chrome-extension://aejoelaoggembcahagimdiliamlcdmfm
Access-Control-Allow-Credentials: true
Access-Control-Allow-Methods: *
Access-Control-Max-Age: 3600
Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept, Key, Authorization
Cache-Control: no-store
Pragma: no-cache
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 11 Apr 2019 18:47:00 GMT

Authorization: Basic Y2xpZW50MToxMjM0NTY=
Origin: chrome-extension://aejoelaoggembcahagimdiliamlcdmfm
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36
Content-Type: application/x-www-form-urlencoded
Accept: */*
Accept-Encoding: gzip, deflate, br
Accept-Language: zh-CN,zh;q=0.9,en;q=0.8
Cookie: JSESSIONID=1457AE15F8AC567BE96A19DD596B0273
```

3.Check the role with End User
```
http://localhost:8089/scr/roles?access_token=<token>
```
return:[{"authority":<some_role>}]