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
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Products Name</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="focusedInput" type="text" placeholder="products name" required="">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">category</label>
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" style="margin-left: 1%;" type="button" data-toggle="dropdown">category
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu" style="margin-left: 18%;">
                                <c:forEach var="category" items="${sizeAndColor.categoryDtos}" varStatus="loop"> 
                                    <li  id="${category.categoryId}"><a href="#">${category.categoryName}</a></li>
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
                                    <li id="${sizee.sizeId}"><a href="#">${sizee.sizeText}</a></li>
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
                                    <li id="${color.colorId}"><a href="#">${color.colorText}</a></li>
                                    </c:forEach>
                            </ul>
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Image url</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="focusedInput" type="text" placeholder="Image url" required="">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Price</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="focusedInput" type="number" placeholder="price" pattern="[0-9]+([\.,][0-9]+)?" step="0.01" value="price" min="0" required="">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Discount</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="focusedInput" value="0" type="number" placeholder="discount" step="0.01" pattern="[0-9]+([\.,][0-9]+)?" value="discount" min="0" required="">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Quantity</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="focusedInput" value="1" type="number" placeholder="quantity" pattern="[0-9]+([\.,][0-9]+)?" value="quantity" min="1" required="">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Description</label>
                        <div class="col-sm-10">
                            <textarea class="form-control" id="focusedInput" rows="2" placeholder="description" id="description" required=""></textarea>
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
