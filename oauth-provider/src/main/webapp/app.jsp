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
                    <h1>Requisicao de Autorizacao</h1>
                </div>

                <p>Este aplicativo deseja acessar suas informacoes:</p>

                <label>Name: </label>${application.name}

                <br>

                <form method="post" action="authorize">
                    <input type="hidden" name="granted" value="true"/><!-- the users granted access-->
                    <input type="hidden" name="client_id" value="${application.clientId}"/>
                    <input type="hidden" name="redirect_url" value="${application.redirectUrl}"/>
                    
                    <button type="button" class="btn btn-large btn-warning">Nope</button>
                    <button type="submit" class="btn btn-large btn-primary">Allow</button>
                </form>
            </div>

    </body>
</html>
