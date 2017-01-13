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
//        $("#add").click(function (e) {
//            console.log('aaaaaaaaaaaaaaaaaaaaaaaaa');
//            $("#items").append('<div><input name="price" type="text" /></div>');
//        });
//            $("body").on("click", ".delete", function (e) {
//    $(this).parent("div").remove();
//        });
//        $('#priceid').change(function () {
//            $('#firstname').val($(this).val());
//        });

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
                    location.reload(true);
                    console.log(data);
                },
                complete: function () {
//                    $('body').hideLoader();
                    return false; // required to block normal submit ajax used
                }
            });
        });
//        $('.btn btn-success active').click(function () {
//            var Size = $('#sizeId').val();
//            var Color = $('#colorId').val();
//            var Price = $('#priceid').val();
//            var Discount = $('#discountid').val();
//            var Quantity = $('#quntid').val();
//        });

        var itemCount = 0;
        $(document).ready(function () {
            var objs = [];
            var temp_objs = [];
            $("#add").click(function () {
                var html = "";
                var obj = {
                    "ROW_ID": itemCount,
                    "ITEM_Size": $("#sizeId").val(),
                    "ITEM_Color": $("#colorId").val(),
                    "ITEM_Price": $("#priceid").val(),
                    "ITEM_Discount": $("#discountid").val(),
                    "ITEM_Quantity": $("#quntid").val()
                }
                objs.push(obj);
                itemCount++;
                html = "<tr id='tr" + itemCount + "'><td>" + obj['ITEM_Size'] + "</td> <td>" + obj['ITEM_Color'] + " </td> <td>" + obj['ITEM_Discount'] + " </td> <td>" + obj['ITEM_Quantity'] + " </td> <td>" + obj['ITEM_Price'] + " </td> </tr>";
                $("#bill_table").append(html)
                $("#" + itemCount).click(function () {
                    var buttonId = $(this).attr("id");
                    $("#tr" + buttonId).remove();
                });
                $("#sizeId").val("");
                $("#colorId").val("");
                $("#priceid").val("");
                $("#discountid").val("");
                $("#quntid").val("");
                $("#szbid").html("Size");
                $("#clbid").html("Color");
                
            });
        });
    });
</script>
<style>
    .nav {
        padding-left: 46% !important;
    }
</style>
<div class="container">
    <ul class="nav nav-pills">
        <li class="active"><a data-toggle="pill" href="#Product">Add New Product</a></li>
        <li><a data-toggle="pill" href="#menu3">Category</a></li>
        <li><a data-toggle="pill" href="#menu2">Add Size</a></li>
        <li><a data-toggle="pill" href="#menu1">Add Color</a></li>
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
                        <label class="col-sm-2 control-label" for="inputSuccess">Image url</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="imgid" name="imgurl" type="text" placeholder="Image url" required="">
                        </div>
                    </div>

                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Description</label>
                        <div class="col-sm-10">
                            <textarea class="form-control" name="description" rows="2" placeholder="description" id="descriptionid" required=""></textarea>
                        </div>
                    </div>

                    <div class="form-group has-success has-feedback">
                        <div class="col-sm-12">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>
                                            Size
                                        </th>
                                        <th>
                                            Color
                                        </th>
                                        <th>
                                            Original Price
                                        </th>
                                        <th>
                                            Sale Price
                                        </th>
                                        <th>
                                            Quantity
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td style="width: 10%;">
                                            <div class="form-group has-success has-feedback" id="Psize">
                                                <div class="dropdown" id="Psize">
                                                    <button id="szbid" class="btn btn-primary btn-sm dropdown-toggle" type="button" data-toggle="dropdown">Size
                                                        <span class="caret"></span></button>
                                                    <ul class="dropdown-menu">
                                                        <c:forEach var="sizee" items="${sizeAndColor.sizeDtos}" varStatus="loop"> 
                                                            <li class="sizee" id="${sizee.sizeId}"><a href="#">${sizee.sizeText}</a></li>
                                                            </c:forEach>
                                                    </ul>
                                                </div>
                                            </div>
                                        </td>
                                        <td style="width: 10%;">
                                            <div class="form-group has-success has-feedback">
                                                <div class="dropdown">
                                                    <button id="clbid" class="btn btn-primary btn-sm dropdown-toggle" type="button" data-toggle="dropdown">Color
                                                        <span class="caret"></span></button>
                                                    <ul class="dropdown-menu">
                                                        <c:forEach var="color" items="${sizeAndColor.colorDtos}" varStatus="loop"> 
                                                            <li class="color" id="${color.colorId}"><a href="#">${color.colorText}</a></li>
                                                            </c:forEach>
                                                    </ul>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="form-group has-success has-feedback" id="items">
                                                <div class="col-sm-10" id="items">
                                                    <input class="form-control" id="priceid" type="number" name="priceid" placeholder="price" pattern="[0-9]+([\.,][0-9]+)?" step="1" value="0" min="0" required="">
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="form-group has-success has-feedback">
                                                <div class="col-sm-10">
                                                    <input class="form-control" id="discountid" value="0" name="discountedPrice" type="number" placeholder="discount" step="1" pattern="[0-9]+([\.,][0-9]+)?" value="discount" min="0" required="">
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="form-group has-success has-feedback">
                                                <div class="col-sm-10">
                                                    <input class="form-control" id="quntid" name="available" value="1" type="number" placeholder="quantity" pattern="[0-9]+([\.,][0-9]+)?" value="quantity" min="1" required="">
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <button type="button" id="add" class="btn btn-success active">
                                                <span class="glyphicon glyphicon-plus"></span> Add Product
                                            </button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="col-sm-12" id="billing_items_div">
                            <table class='table table-bordered'  id='bill_table'> 
                                <thead>
                                    <tr>
                                        <th>Size</th>
                                        <th>Color</th>
                                        <th>Price</th>
                                        <th>Discount</th>
                                        <th>Quantity</th>
                                    </tr>
                                </thead>
                            </table>
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
        <div id="menu1" class="tab-pane fade in">
            <div class="col-sm-2"></div>
            <div class="col-sm-8">
                <h2 class="text-center text-info">Add Color</h2>
                <form class="form-horizontal" id="" name="" action="">
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Color Name</label>
                        <div class="col-sm-10">
                            <input class="form-control" name="" id="" type="text" placeholder="Color Name" required="">
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
        </div>
        <div id="menu2" class="tab-pane fade">
            <div class="col-sm-2"></div>
            <div class="col-sm-8">
                <h2 class="text-center text-info">Add size group</h2>
                <form class="form-horizontal" id="" name="" action="">
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Add size group</label>
                        <div class="col-sm-10">
                            <input class="form-control" name="" id="" type="text" placeholder="Add size group" required="">
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
                <form class="form-horizontal" id="" name="" action="">
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Size Group</label>
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" style="margin-left: 1%;" type="button" data-toggle="dropdown">products color
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu" style="margin-left: 18%;">
                                <li><a href="#">HTML</a></li>
                                <li><a href="#">CSS</a></li>
                                <li><a href="#">JavaScript</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Size</label>
                        <div class="col-sm-10">
                            <input class="form-control" name="" id="" type="text" placeholder="Size" required="">
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
        </div>
        <div id="menu3" class="tab-pane fade">
            <div class="col-sm-2"></div>
            <div class="col-sm-8">
                <h2 class="text-center text-info">Category</h2>
                <form class="form-horizontal" id="" name="" action="">
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Category Name</label>
                        <div class="col-sm-10">
                            <input class="form-control" name="" id="" type="text" placeholder="Category Name" required="">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Category Parent</label>
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" style="margin-left: 1%;" type="button" data-toggle="dropdown">Category Parent
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu" style="margin-left: 18%;">
                                <li><a href="#">HTML</a></li>
                                <li><a href="#">CSS</a></li>
                                <li><a href="#">JavaScript</a></li>
                            </ul>
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
        </div>
    </div>
</div>
<!--    </body>
</html>-->
