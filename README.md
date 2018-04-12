# SparrowService
Basic Service Framework with RESTful, OAuth, MongoDB. And reactive. 

RP Services: nest
To implement :
Story Client in mongoDB repository

Test case:
1.Authorize End User and client get code(with autoApprove=true)
``http://localhost:8089/oauth/authorize?client_id=<clientid>&response_type=code&redirect_uri=<redirect_some_uri>&scope=read write&state=something&grant_type=authorization_code
Login as:<username>

2.Get token
``http://localhost:8089/oauth/token?code=<code>&client_id=<clientid>&client_secret=<secret>&redirect_uri=<redirect_some_uri>&grant_type=authorization_code
Need Login as:<clientid>

3.Check the role with End User
``http://localhost:8089/scr/roles?access_token=<token>
return:[{"authority":<some_role>}]