<%-- 
    Document   : paytmpayment
    Created on : Jun 22, 2017, 12:52:56 PM
    Author     : weaversAndroid
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
            <p>Connecting to Payment Gateway</p>
            <div class="whirl duo" style="height: 50px;"></div>
        </div>
    </div>
</div>
<form method='post' action="${paymenturl}" name='paytmForm' id="paytmForm">
    <input type='hidden' name="ORDER_ID" id="ORDER_ID" value="${ORDER_ID}">
    <input type='hidden' name="CUST_ID" id="CUST_ID" value="${CUST_ID}">
    <input type='hidden' name="TXN_AMOUNT" id="TXN_AMOUNT" value="${TXN_AMOUNT}">
    <input type='hidden' name="MID" id="MID"value="${MID}">
    <input type='hidden' name="CHANNEL_ID" id="CHANNEL_ID" value="${CHANNEL_ID}">
    <input type='hidden' name="INDUSTRY_TYPE_ID" id="INDUSTRY_TYPE_ID" value="${INDUSTRY_TYPE_ID}">
    <input type='hidden' name="WEBSITE" id="WEBSITE" value="${WEBSITE}">
    <input type='hidden' name="MOBILE_NO" id="MOBILE_NO" value="${MOBILE_NO}">
    <input type='hidden' name="EMAIL" id="EMAIL" value="${EMAIL}">
    <input type='hidden' name="CALLBACK_URL" id="CALLBACK_URL" value="${CALLBACK_URL}">
    <input type='hidden' name="CHECKSUMHASH" id="CHECKSUMHASH" value="${CHECKSUMHASH}">
    <input type='submit' name="fff" value="ok" style="opacity: 0;">
</form>
    <input type="hidden" id="appType" value="${appType}">
<script type="text/javascript">
    setTimeout(function () {
        if (document.getElementById('appType').value == 2) {
//            console.log(document.getElementById('CHECKSUMHASH').value);
//            console.log('&lt;%= request.getContextPath() %&gt;');
//            console.log('<%= request.getContextPath() %>');
            document.getElementById("paytmForm").submit();
        }
    }, 3000);
//    $(document).ready(function () {
//        if ($("#appType").val() == 2) {
//            window.location = "http://duqhan.com/#/store/after-payment";
////            window.location.replace("http://duqhan.com/#/store/after-payment");
//        }
//    });
</script>