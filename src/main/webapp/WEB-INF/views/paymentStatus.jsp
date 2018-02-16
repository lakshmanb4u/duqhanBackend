<%-- 
    Document   : paymentStatus
    Created on : Jan 25, 2017, 7:35:11 PM
    Author     : Android-3
--%>
<link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">
<link href="../_resources/style/whirl.min.css" rel="stylesheet">
<style>
    body {margin: 0;}
    p {font-family: 'Roboto', sans-serif; font-size: 20px; margin-top: 10px;}
    .outer {display: table; position: absolute; height: 100%; width: 100%;}
    .middle {display: table-cell; vertical-align: middle;}
    .inner {margin-left: auto; margin-right: auto; width: 100%; text-align: center;}
</style>
<div class="outer">
    <div class="middle">
        <div class="inner">
            <p>Payment ${msg}. Redirecting to the application...</p>
            <div class="whirl duo" style="height: 50px;"></div>
        </div>
    </div>
</div>
<input type="hidden" id="appType" value="${appType}">
<script type="text/javascript">
    setTimeout(function () {
        if (document.getElementById('appType').value == 2) {    //2 = web
            window.location = "https://duqhan.com/#/store/after-payment";
//            window.location = "http://localhost:3000/#/store/after-payment";
        }
    }, 3000);
</script>