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

                <label>Name: </label>${app.name}
                <br />
                <label>Required access: </label>//TODO =)

                <br>

                <form method="POST" action="authorize">
                    <!-- Attributes are set from AuthorizeServlet -->
                    <input type="hidden" name="redirect_uri" value="${redirect_uri}" />
                    <input type="hidden" name="client_id" value="${client_id}" />
                    
                    <button type="submit" class="btn btn-large btn-warning" name="grant" value="denied">Nope</button>
                    <button type="submit" class="btn btn-large btn-primary" name="grant" value="granted">Allow</button>
                </form>
            </div>
    </body>
</html>
