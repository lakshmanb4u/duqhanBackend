<%-- 
    Document   : addProduct
    Created on : Jan 11, 2017, 3:00:43 PM
    Author     : Weavers-web
--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ page session="true" %>--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--<html>-->
<!--    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add New Product</title>
    </head>
    <body>-->
<script type="text/javascript">
    $(document).ready(function () {
        $(".dropdown-menu li a").click(function () {
            $(this).parents(".dropdown").find('.btn').html($(this).text() + ' <span class="caret"></span>');
            $(this).parents(".dropdown").find('.btn').val($(this).data('value'));
        });

        $(".dropdown-menu li.category").click(function () {
            $("#categoryId").val($(this).prop('id'));
            $("#categoryId").val($(this).attr('id'));
        });
        $(".dropdown-menu li.sizee").click(function () {
            $("#sizeId").val($(this).prop('id'));
            $("#sizeId").val($(this).attr('id'));
        });
        $(".dropdown-menu li.color").click(function () {
            $("#colorId").val($(this).prop('id'));
            $("#colorId").val($(this).attr('id'));
        });

        $("#addProductId").submit(function (e) {
            var that = $(this);
            e.preventDefault();
            var jsondata = {};
            jsondata.name = $('#nameid').val();
            jsondata.imgurl = $('#imgid').val();
            jsondata.categoryId = $('#categoryId').val();
            jsondata.description = $('#descriptionid').val();
            jsondata.colorId = $('#colorId').val();
            jsondata.sizeId = $('#sizeId').val();
            jsondata.discountedPrice = $('#discountid').val();
            jsondata.price = $('#priceid').val();
            jsondata.available = $('#quntid').val();
            $.ajax({
                type: "POST",
                data: JSON.stringify(jsondata),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                url: that.attr("action"),
                beforeSend: function () {
//                    $('#sky-form button[type="submit"]').attr('disabled', true);
//                    $('#sky-form button[type="submit"]').addClass('.button-submitting');
                },
                success: function (data) {
//                    console.log('success');
//                    console.log(data);
//                    $('#sky-form button[type="submit"]').attr('disabled', false);
//                    $('#sky-form button[type="submit"]').removeClass('button-submitting');
//                    $('.alert').show();
//                    $('html, body').animate({scrollTop: 0}, 800);
                    console.log(data);
                },
                complete: function () {
//                    $('body').hideLoader();
                    return false; // required to block normal submit ajax used
                }
            });
        });

    });
</script>
<style>
    .nav {
        padding-left: 56% !important;
    }
</style>
<div class="container">
    <ul class="nav nav-pills">
        <li class="active"><a data-toggle="pill" href="#Product">Add New Product</a></li>
        <li><a data-toggle="pill" href="#menu1">Add size</a></li>
        <li><a data-toggle="pill" href="#menu2">Add color</a></li>
    </ul>
    <div class="tab-content">
        <div id="Product" class="tab-pane fade in active">
            <div class="col-sm-2"></div>
            <div class="col-sm-8">
                <h2 class="text-center text-info">Add New Product</h2>
                <form class="form-horizontal" id="addProductId" name="productBean" action="/web/save-product">
                    <input type="hidden" id="categoryId" name="categoryId" value="">
                    <input type="hidden" id="colorId" name="colorId" value="">
                    <input type="hidden" id="sizeId" name="sizeId" value="">
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Products Name</label>
                        <div class="col-sm-10">
                            <input class="form-control" name="name" id="nameid" type="text" placeholder="products name" required="">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">category</label>
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" style="margin-left: 1%;" type="button" data-toggle="dropdown">category
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu" style="margin-left: 18%;">
                                <c:forEach var="category" items="${sizeAndColor.categoryDtos}" varStatus="loop"> 
                                    <li class="category" id="${category.categoryId}">
                                        <a href="#">${category.categoryName}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Product Size</label>
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" style="margin-left: 1%;" type="button" data-toggle="dropdown">products size
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu" style="margin-left: 18%;">
                                <c:forEach var="sizee" items="${sizeAndColor.sizeDtos}" varStatus="loop"> 
                                    <li class="sizee" id="${sizee.sizeId}"><a href="#">${sizee.sizeText}</a></li>
                                    </c:forEach>
                            </ul>
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Product Color</label>
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" style="margin-left: 1%;" type="button" data-toggle="dropdown">products color
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu" style="margin-left: 18%;">
                                <c:forEach var="color" items="${sizeAndColor.colorDtos}" varStatus="loop"> 
                                    <li class="color" id="${color.colorId}"><a href="#">${color.colorText}</a></li>
                                    </c:forEach>
                            </ul>
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Image url</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="imgid" name="imgurl" type="text" placeholder="Image url" required="">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Price</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="priceid" type="number" name="price" placeholder="price" pattern="[0-9]+([\.,][0-9]+)?" step="1" value="0" min="0" required="">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Discount</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="discountid" value="0" name="discountedPrice" type="number" placeholder="discount" step="1" pattern="[0-9]+([\.,][0-9]+)?" value="discount" min="0" required="">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Quantity</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="quntid" name="available" value="1" type="number" placeholder="quantity" pattern="[0-9]+([\.,][0-9]+)?" value="quantity" min="1" required="">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Description</label>
                        <div class="col-sm-10">
                            <textarea class="form-control" name="description" rows="2" placeholder="description" id="descriptionid" required=""></textarea>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-9"></div>
                        <div class="col-sm-3">
                            <span class="pull-right">
                                <button type="submit" class="btn btn-success btn-lg active">Save</button>
                            </span>
                        </div>
                    </div>
                </form>
            </div>
            <div class="col-sm-2"></div>
        </div>
    </div>
    <div id="menu1" class="tab-pane fade">

    </div>
    <div id="menu2" class="tab-pane fade">

    </div>
    <div id="menu3" class="tab-pane fade">

    </div>
</div>
</div>

<!--    </body>
</html>-->
