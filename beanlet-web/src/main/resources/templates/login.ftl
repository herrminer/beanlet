<#import "/spring.ftl" as spring/>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title><@spring.message "login.sign.in"/></title>

  <!-- Bootstrap core CSS -->
  <link href="<@spring.url "/css/bootstrap.min.css"/>" rel="stylesheet">

  <!-- Custom styles for this template -->
  <link href="<@spring.url "/css/beanlet.css"/>" rel="stylesheet">

</head>

<body>
<div class="container">

  <form class="form-signin">
    <h2 class="form-signin-heading"><@spring.message "login.title"/></h2>
    <label for="inputEmail" class="sr-only"><@spring.message "login.email"/></label>
    <input type="email" id="inputEmail" class="form-control" placeholder="Email address" autocomplete="off" required autofocus>
    <label for="inputPassword" class="sr-only"><@spring.message "login.password"/></label>
    <input type="password" id="inputPassword" class="form-control" placeholder="Password" autocomplete="off" required>
    <div class="checkbox">
      <label>
        <input type="checkbox" value="remember-me"> <@spring.message "login.remember.me"/>
      </label>
    </div>
    <button class="btn btn-lg btn-primary btn-block" type="submit"><@spring.message "login.sign.in"/></button>
  </form>

</div> <!-- /container -->

</body>
</html>
