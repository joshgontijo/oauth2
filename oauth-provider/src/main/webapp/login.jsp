<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>	
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>App</title>	
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet">
        <style>
            p { margin-bottom: 30px; }
        </style>    
    </head>
    <body>
        <div class="container-fluid">
            <div class="col-md-4 col-md-offset-4">

                <div class="page-header">
                    <h1>Login</h1>
                </div>

                <p>Return to: ${return_to}</p>
                <p>redirect_uri: ${redirect_uri}</p> 
                <p>client_id ${client_id}</p>
                <p>login error: ${loginErrorMessage}</p>
                
                <form method="post" action="login">
                    <input type="hidden" name="return_to" value="${return_to}" />
                    <input type="hidden" name="redirect_uri" value="${redirect_uri}" />
                    <input type="hidden" name="client_id" value="${client_id}" />
                    
                    <label>Username: </label> 
                    <input type="text" name="username" value="josue"/>
                    <br />
                    <label>Password: </label> 
                    <input type="password" name="password" value="josue"/>
                    <br />
                    <button type="submit" class="btn btn-large btn-primary">Login</button>
                </form>
            </div>

    </body>
</html>
