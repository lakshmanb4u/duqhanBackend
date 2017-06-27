<%-- 
    Document   : paymentStatus
    Created on : Jan 25, 2017, 7:35:11 PM
    Author     : Android-3
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="container">
    <input type="hidden" id="appType" value="${appType}">
    <div class="row" style="margin-top: 25%;">
        <div class="col-sm-12 text-center">
            <img src="/resources/images/logo.png" style="max-width: 100px;">
        </div>
        <div class="col-sm-12">
            <h2 class="text-center ${altclass}">${msg}</h2>
        </div>
    </div>
    <hr>
    <div class="row">
        <div class="col-sm-12">
            <div class="alert alert-info">
                <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
                This window will get closed within <span id="time" style="font-weight: bold;">10</span> seconds. If not close it using the top right corner button.
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        if ($("#appType").val() == 2) {
            window.location = "http://duqhan.com/#/store/after-payment";
//            window.location.replace("http://duqhan.com/#/store/after-payment");
        }
    });
    /*function startTimer(duration, display) {
     var timer = duration, minutes, seconds;
     setInterval(function () {
     // minutes = parseInt(timer / 60, 10);
     seconds = parseInt(timer % 60, 10);
     
     // minutes = minutes < 10 ? "0" + minutes : minutes;
     seconds = seconds < 10 ? "0" + seconds : seconds;
     
     display.textContent = seconds;
     
     if (--timer < 0) {
     timer = 0;
     window.close();
     }
     }, 1000);
     }
     
     window.onload = function () {
     var tenSeconds = 10,
     display = document.querySelector('#time');
     startTimer(tenSeconds, display);
     };*/
</script>