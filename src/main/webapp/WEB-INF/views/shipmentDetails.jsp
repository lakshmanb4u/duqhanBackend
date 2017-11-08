<%-- 
    Document   : shipmentDetails
    Created on : Feb 13, 2017, 12:49:01 PM
    Author     : weaversAndroid
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    $(document).ready(function () {
        $('#buy-shipment').click(function () {
            var shipmentDto = {};
            shipmentDto.userId = $(this).attr('data-userId');
            shipmentDto.payKey = $(this).attr('data-payKey');
            $.ajax({
                type: "POST",
                data: JSON.stringify(shipmentDto),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                url: "/web/buy-pending-shipment",
                success: function (data) {
                    location.reload(true);
                },
                complete: function () {
                    return false;
                }
            });
        });
    });
</script>
<div class="container">
    <div class="row">
        <div class="col-lg-12">
            <table class="table">
                <thead>
                    <tr>
                        <th>User Id</th>
                        <th>Pay Key</th>
                        <th>Shipment Id</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="shipmentDto" items="${shipmentDtos}" varStatus="loop">
                        <tr>
                            <td>${shipmentDto.userId}</td>
                            <td>${shipmentDto.payKey}</td>
                            <td>${shipmentDto.shipmentId}</td>
                            <td>
                                <button type="button" id="buy-shipment" data-userId="${shipmentDto.userId}" data-payKey="${shipmentDto.payKey}" class="btn btn-success active">
                                    <span class="glyphicon glyphicon-plus"></span> Ship-
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
