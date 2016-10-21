<#import "/spring.ftl" as spring/>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>users</title>

  <!-- Bootstrap core CSS -->
  <link href="<@spring.url "/bootstrap-3.3.7-dist/css/bootstrap.min.css"/>" rel="stylesheet">

</head>

<body>
<table class="table table-striped table-bordered">
  <thead>
  <tr>
    <th>id</th>
    <th>email</th>
    <th>password</th>
  </tr>
  </thead>
  <tbody>
  <#list users as user>
  <tr>
    <td>${user.id}</td>
    <td>${user.email}</td>
    <td>${user.password}</td>
  </tr>
  </#list>
  </tbody>
</table>
</body>
</html>

