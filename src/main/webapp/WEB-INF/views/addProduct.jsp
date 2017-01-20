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
<script type="text/javascript">
    var isSizee = true;
    var isColor = true;
    var isFirst = true;

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
            $("#sizetextId").val($(this).html());
        });
        $(".dropdown-menu li.color").click(function () {
            $("#colorId").val($(this).prop('id'));
            $("#colorId").val($(this).attr('id'));
            $("#colortextId").val($(this).html());
        });
        $(".dropdown-menu li.sizegroup").click(function () {
            $("#sizegroupId").val($(this).prop('id'));
            $("#sizegroupId").val($(this).attr('id'));
        });
        $(".dropdown-menu li.parentcategory").click(function () {
            $("#parentcategoryId").val($(this).prop('id'));
            $("#parentcategoryId").val($(this).attr('id'));
        });
        $('#discountid').on('keyup blur change', function (e) {
            if (parseFloat($('#discountid').val()) > parseFloat($('#priceid').val())) {
                $('#discountid').val($('#priceid').val());
            }
        });
        $('#priceid').on('keyup blur change', function (e) {
            $('#discountid').val($('#priceid').val());
        });
//===========================image upload==========================//
        $("#image-form").submit(function (e) {
            var that = $(this);
            e.preventDefault();
            var data = new FormData($(this)[0]);
            $.ajax({
                url: that.attr("action"),
                data: data,
                dataType: 'text',
                processData: false,
                contentType: false,
                type: 'POST',
                success: function (data) {
                    var jdata = JSON.parse(data);
                    if (jdata.status !== "failure") {
                        if ($("#ImgFront").val() === "") {
                            $("#ImgFront").val(jdata.status);
                            $("#fileId").val("");
                        } else {
                            addImageTable(jdata.status);
                        }
                    } else {
                        alert("Error: Image not upload..Try again");
                    }
                }
            });
        });
//===========================form submit===========================//
        $("#addProductId").submit(function (e) {
            var that = $(this);
            e.preventDefault();
            var jsondata = {};
            jsondata.name = $('#nameid').val();
            jsondata.imgurl = $('#ImgFront').val();
            jsondata.categoryId = $('#categoryId').val();
            jsondata.description = $('#descriptionid').val();
            var imageDtos = [];
            $('.subimg').each(function () {
                var imageDto = {};
                imageDto.imgUrl = $(this).find('td').eq(0).text();
                imageDtos.push(imageDto);
            });
            jsondata.imageDtos = imageDtos;
            var sizeColorMaps = [];
            $('.maptr').each(function (index) {
                var sizeColorMap = {};
                sizeColorMap.colorId = $(this).find('.colorkey').val();
                sizeColorMap.sizeId = $(this).find('.sizekey').val();
                sizeColorMap.orginalPrice = $(this).find('td').eq(2).text();
                sizeColorMap.salesPrice = $(this).find('td').eq(3).text();
                sizeColorMap.count = $(this).find('td').eq(4).text();
                sizeColorMaps.push(sizeColorMap);
//                    console.log(index + ": " + $(this).text());
            });
            jsondata.sizeColorMaps = sizeColorMaps;
//            console.log("ttttttttttttttttttt" + JSON.stringify(jsondata));
            if (sizeColorMaps.length > 0 && $('#categoryId').val() !== null && $('#categoryId').val() !== '' && sizeColorMaps.length > 0) {
                $.ajax({
                    type: "POST",
                    data: JSON.stringify(jsondata),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    url: that.attr("action"),
                    success: function (data) {
                        location.reload(true);
                        console.log(data);
                    },
                    complete: function () {
                        return false; // required to block normal submit ajax used
                    }
                });
            } else {
                alert("REQUIRD : Category and product.");
            }
        });
//========================add map table===========================

        $("#add").click(function () {
            var addPermit = true;
            if (isFirst) {
                if ($("#sizeId").val() === "") {
                    isSizee = false;
                    $("#szbid").prop('disabled', true);
                }
                if ($("#colorId").val() === "") {
                    isColor = false;
                    $("#clbid").prop('disabled', true);
                }
                if ($("#sizeId").val() === "" && $("#colorId").val() === "") {
                    $("#add").prop('disabled', true);
                }
                isFirst = false;
            } else {
                if ($("#sizeId").val() === "" && isSizee) {
                    alert("Please enter size");
                    addPermit = false;
                }
                if ($("#colorId").val() === "" && isColor) {
                    alert("Please enter color");
                    addPermit = false;
                }
            }

            if (addPermit) {
                if ($("#priceid").val() !== "" && $("#discountid").val() !== "" && $("#quntid").val() !== "") {
                    var itemCount = 0;
                    var objs = [];
                    var html = "";
                    var obj = {
                        "ROW_ID": itemCount,
                        "ITEM_Size": $("#sizeId").val(),
                        "ITEM_SizeText": $("#sizetextId").val(),
                        "ITEM_Color": $("#colorId").val(),
                        "ITEM_ColorText": $("#colortextId").val(),
                        "ITEM_Price": $("#priceid").val(),
                        "ITEM_Discount": $("#discountid").val(),
                        "ITEM_Quantity": $("#quntid").val()
                    };
                    objs.push(obj);
                    itemCount++;
                    html = "<tr id='tr" + itemCount + "' class='maptr'><td>" + obj['ITEM_SizeText'] + "<input type='hidden' class='sizekey' value=" + obj['ITEM_Size'] + "></td> <td>" + obj['ITEM_ColorText'] + "<input type='hidden' class='colorkey' value=" + obj['ITEM_Color'] + "> </td> <td>" + obj['ITEM_Price'] + " </td> <td>" + obj['ITEM_Discount'] + " </td> <td>" + obj['ITEM_Quantity'] + " </td> </tr>";
                    $("#bill_table").append(html);
                    $("#" + itemCount).click(function () {
                        var buttonId = $(this).attr("id");
                        $("#tr" + buttonId).remove();
                    });
                    $("#sizeId").val("");
                    $("#colorId").val("");
                    $("#priceid").val("0");
                    $("#discountid").val("0");
                    $("#quntid").val("1");
                    $("#szbid").html("Size");
                    $("#clbid").html("Color");
                } else {
                    alert("REQUIR: Original Price, Sale Price, Quantity.");
                }
            }
        });
//========================add more img===========================
        $("#addImg").click(function () {
            if ($("#imgid").val() !== "") {
                addImageTable($("#imgid").val());
            }
        });
        //**************** Add Color *********************
        $("#addColor").submit(function (e) {
            var that = $(this);
            e.preventDefault();
            var jsondata = {};
            var color = $('#addcolorId').val();
            jsondata.addcolor = $('#addcolorId').val();
            if (color !== '' && color !== null) {
                $.ajax({
                    type: "POST",
                    data: color,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    url: that.attr("action"),
                    beforeSend: function () {
                        $("#addColor").validate();
                    },
                    success: function (data) {
                        location.reload(true);
                        console.log(data);
                    },
                    complete: function () {
                        return false;
                    }
                });
            }
        });
        // ****************** Add size group ************************
        $("#Addsize ").submit(function (e) {
            var that = $(this);
            e.preventDefault();
            var jsondata = {};
            var sizeGroop = $('#AddSizeGroupId').val();
            jsondata.addsize = $('#AddSizeGroupId').val();
            if (sizeGroop !== '' && sizeGroop !== null) {
                $.ajax({
                    type: "POST",
                    data: sizeGroop,
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    url: that.attr("action"),
                    beforeSend: function () {
                        $("#Addsize").validate();
                    },
                    success: function (data) {
                        location.reload(true);
                        console.log(data);
                    },
                    complete: function () {
                        return false;
                    }
                });
            }
        });
        //******************** Save size *********************
        $("#SizeGroup ").submit(function (e) {
            var that = $(this);
            e.preventDefault();
            var jsondata = {};
            jsondata.sizeGroupId = $('#sizegroupId').val();
            jsondata.sizeText = $('#size-name').val();
            $.ajax({
                type: "POST",
                data: JSON.stringify(jsondata),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                url: that.attr("action"),
                beforeSend: function () {
                    $("#SizeGroup").validate();
                },
                success: function (data) {
                    location.reload(true);
                    console.log(data);
                },
                complete: function () {
                    return false;
                }
            });
        });
        //********************* Category *****************
        $("#CategoryId ").submit(function (e) {
            var that = $(this);
            e.preventDefault();
            var jsondata = {};
            jsondata.categoryName = $('#category-name').val();
            jsondata.patentId = $('#parentcategoryId').val();
            $.ajax({
                type: "POST",
                data: JSON.stringify(jsondata),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                url: that.attr("action"),
                beforeSend: function () {
                    $("#CategoryId").validate();
                },
                success: function (data) {
                    location.reload(true);
                    console.log(data);
                },
                complete: function () {
                    return false;
                }
            });
        });
    });
    function addImageTable(imgUrl) {
        var itemCount = 0;
        var objs = [];
        var html = "";
        var obj = {
            "ROW_ID": itemCount,
            "IMG_Size": imgUrl
        };
        objs.push(obj);
        itemCount++;
        html = "<tr id='tr" + itemCount + "' class='subimg'><td>" + obj['IMG_Size'] + " </td> </tr>";
        $("#bill_table2").append(html);
        $("#imgid").val("");
    }

    $body = $("body");
    $(document).on({
        ajaxStart: function () {
            $body.addClass("loading");
        },
        ajaxStop: function () {
            $body.removeClass("loading");
        }
    });
</script>
<style>
    .nav {
        padding-top: 6% !important;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-sm-2">
            <img src="/resources/images/logo.png" style="height: 60%; width: 60%;">
        </div>
        <div class="col-sm-10">
            <ul class="nav nav-pills">
                <li class="active"><a data-toggle="pill" href="#Product">Add New Product</a></li>
                <li><a data-toggle="pill" href="#menu3">Category</a></li>
                <li><a data-toggle="pill" href="#menu2">Add Size</a></li>
                <li><a data-toggle="pill" href="#menu1">Add Color</a></li>
            </ul>
        </div>
    </div>
    <hr>
    <div class="tab-content">
        <div id="Product" class="tab-pane fade in active">
            <div class="col-sm-2"></div>
            <div class="col-sm-8">
                <h2 class="text-center text-info">Add New Product</h2>
                <form method="POST" name="productBean" action="web/save-product-image" id="image-form" enctype="multipart/form-data" class="form-horizontal">
                    <div class="row  form-group has-success has-feedback">
                        <div class="col-sm-2"><label class="col-sm-2 control-label" for="inputSuccess">Upload Image</label></div>
                        <div class="fileinput fileinput-exists col-sm-8" data-provides="fileinput">
                            <span class="btn btn-default btn-file" style="margin-left: 0;">
                                <input type="file" name="frontImage" id="fileId" required="">
                            </span>
                        </div>
                        <div class="col-sm-2"><span class="pull-right"><input type="submit" value="Upload"></span></div>
                    </div>
                </form>
                <form class="form-horizontal" id="addProductId" name="productBean" action="/web/save-product">
                    <input type="hidden" id="categoryId" name="categoryId" value="">
                    <input type="hidden" id="colorId" name="colorId" value="">
                    <input type="hidden" id="colortextId" name="colortextId" value="">
                    <input type="hidden" id="sizeId" name="sizeId" value="">
                    <input type="hidden" id="sizetextId" name="sizetextId" value="">
                    <input type="hidden" id="sizegroupId" name="sizegroupId" value="">
                    <input type="hidden" id="parentcategoryId" name="parentcategoryId" value="">
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
                        <label class="col-sm-2 control-label" for="inputSuccess">Front Image</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="ImgFront" name="imgurl" type="text" placeholder="Front Image" required="">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <!--                        <label class="col-sm-2 control-label" for="inputSuccess">Image url</label>
                                                <div class="col-sm-8">
                                                    <input class="form-control" id="imgid" name="imgurl" type="text" placeholder="Image url">
                                                </div>
                                                <div class="col-sm-1">
                                                    <button type="button" id="addImg" class="btn btn-success active">
                                                        <span class="glyphicon glyphicon-plus"></span> Add Image
                                                    </button>
                                                </div>-->
                        <div class="col-sm-8" id="billing_items_div">
                            <table class='table table-bordered text-center'  id='bill_table2' style="margin-left: 26%;
                                   margin-top: 5%;"> 
                            </table>
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
                                        <th>Size</th>
                                        <th>Color</th>
                                        <th>Original Price</th>
                                        <th>Sale Price</th>
                                        <th>Quantity</th>
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
                                                    <input class="form-control" id="priceid" type="number" name="priceid" placeholder="price" pattern="[0-9]+([\.,][0-9]+)?" step="1" value="0" min="0">
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="form-group has-success has-feedback">
                                                <div class="col-sm-10">
                                                    <input class="form-control" id="discountid" value="0" name="discountedPrice" type="number" placeholder="discount" step="1" pattern="[0-9]+([\.,][0-9]+)?" value="discount" min="0">
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="form-group has-success has-feedback">
                                                <div class="col-sm-10">
                                                    <input class="form-control" id="quntid" name="available" value="1" type="number" placeholder="quantity" pattern="[0-9]+([\.,][0-9]+)?" value="quantity" min="1">
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
                                        <th>Original Price</th>
                                        <th>Sale Price</th>
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
                <form class="form-horizontal" id="addColor" name="ColorBean" action="/web/save-color">
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Color Name</label>
                        <div class="col-sm-10">
                            <input class="form-control" name="addcolor" id="addcolorId" type="text" placeholder="Color Name" required="">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-9"></div>
                        <div class="col-sm-3">
                            <span class="pull-right">
                                <button type="submit" class="btn btn-success btn-lg active">
                                    <span class="glyphicon glyphicon-save"></span>Save</button>
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
                <form class="form-horizontal" id="Addsize" name="SizeBean" action="/web/save-sizegroup">
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Add size group</label>
                        <div class="col-sm-10">
                            <input class="form-control" name="addsize" id="AddSizeGroupId" type="text" placeholder="Add size group" required="">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-9"></div>
                        <div class="col-sm-3">
                            <span class="pull-right">
                                <button type="submit" class="btn btn-success btn-lg active">
                                    <span class="glyphicon glyphicon-save"></span>Save</button>
                            </span>
                        </div>
                    </div>
                </form>
                <form class="form-horizontal" id="SizeGroup" name="SizeGroupBean" action="/web/save-size">
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Size Group</label>
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" style="margin-left: 1%;" type="button" data-toggle="dropdown">Size Group
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu" style="margin-left: 18%;">
                                <c:forEach var="sizeegroup" items="${sizeAndColor.sizeGroupDtos}" varStatus="loop"> 
                                    <li id="${sizeegroup.sizeGroupId}" class="sizegroup"><a href="#">${sizeegroup.sizeText}</a></li>
                                    </c:forEach>
                            </ul>
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Size</label>
                        <div class="col-sm-10">
                            <input class="form-control" name="size" id="size-name" type="text" placeholder="Size" required="">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-9"></div>
                        <div class="col-sm-3">
                            <span class="pull-right">
                                <button type="submit" class="btn btn-success btn-lg active">
                                    <span class="glyphicon glyphicon-save"></span>Save</button>
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
                <form class="form-horizontal" id="CategoryId" name="CategoryBean" action="/web/save-category">
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Category Name</label>
                        <div class="col-sm-10">
                            <input class="form-control" name="Category" id="category-name" type="text" placeholder="Category Name" required="">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Category Parent</label>
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" style="margin-left: 1%;" type="button" data-toggle="dropdown">Category Parent
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu" style="margin-left: 18%;">
                                <c:forEach var="pcategory" items="${sizeAndColor.categoryDtos}" varStatus="loop"> 
                                    <li class="parentcategory" id="${pcategory.categoryId}">
                                        <a href="#">${pcategory.categoryName}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-9"></div>
                        <div class="col-sm-3">
                            <span class="pull-right">
                                <button type="submit" class="btn btn-success btn-lg active">
                                    <span class="glyphicon glyphicon-save"></span>Save</button>
                            </span>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal"><!-- Place at bottom of page --></div>

