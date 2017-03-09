<%-- 
    Document   : addProduct
    Created on : Mar 6, 2017, 4:18:18 PM
    Author     : weaversAndroid
--%>


<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ page session="true" %>--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<script src="/resources/js/addNewProduct.js"></script>
<!DOCTYPE html>



<div id="contact" class="container well">
    <h3 class="text-center">Add product</h3>
    <div class="row">
        <form method="POST" name="productBean" action="/admin/save-product-image" id="image-form" enctype="multipart/form-data" class="form-horizontal">
            <div class="container-fluid bg-3 text-center well">    
                <div class="row">
                    <div class="col-sm-3">
                        <label class="control-label" for="inputSuccess">Upload Image</label>
                    </div>
                    <div class="col-sm-6">
                        <div class="fileinput fileinput-exists" data-provides="fileinput">
                            <span class="btn btn-default btn-file" style="margin-left: 0;">
                                <input type="file" name="frontImage" id="fileId" required="">
                            </span>
                        </div>
                    </div>
                    <div class="col-sm-3">
                        <input class="btn btn-info btn-sm" type="submit" value="Upload">
                    </div>
                </div>
            </div>
        </form>
        <form class="form-horizontal" id="addProductId" name="productBean" action="/admin/save-product">
            <input type="hidden" id="categoryId" name="categoryId" value="">
            <input type="hidden" id="vendorId" name="vendorId" value="">
            <input type="hidden" id="colorId" name="colorId" value="">
            <input type="hidden" id="colortextId" name="colortextId" value="">
            <input type="hidden" id="sizeId" name="sizeId" value="">
            <input type="hidden" id="sizetextId" name="sizetextId" value="">
            <input type="hidden" id="sizegroupId" name="sizegroupId" value="">
            <input type="hidden" id="parentcategoryId" name="parentcategoryId" value="">
            <!--<input type="hidden" id="categoryspecId" name="categoryspecId" value="">-->
            <input type="hidden" id="specId" name="specId" value="">
            <div class="container-fluid bg-3 text-center well">    
                <div class="row">
                    <div class="col-sm-3">
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle get-category" style="margin-left: 1%;" type="button" data-toggle="dropdown">category
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu get-category-list" style="margin-left: 18%;">

                            </ul>
                        </div>
                    </div>
                    <div class="col-sm-3">
                        <button type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#categoryModal">Add Category..</button>
                    </div>
                    <div class="col-sm-3"> 
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle get-vendor" style="margin-left: 1%;" type="button" data-toggle="dropdown">Vendor
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu get-vendor-list" style="margin-left: 18%;">

                            </ul>
                        </div>
                    </div>
                    <div class="col-sm-3"> 
                        <button type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#vendorModal">Add Vendor..</button>
                    </div>
                </div>
            </div>
            <div class="form-group has-success has-feedback">
                <label class="col-sm-2 control-label" for="inputSuccess">Products Name</label>
                <div class="col-sm-10">
                    <input class="form-control" name="name" id="nameid" type="text" placeholder="products name" required="">
                </div>
            </div>

            <div class="form-group has-success has-feedback">
                <label class="col-sm-2 control-label" for="inputSuccess">Front Image</label>
                <div class="col-sm-10">
                    <input class="form-control" id="ImgFront" name="imgurl" type="text" placeholder="Front Image" required="">
                </div>
            </div>
            <div class="form-group has-success has-feedback">
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
                <label class="col-sm-2 control-label" for="inputSuccess">Shipping Time (Day)</label>
                <div class="col-sm-10">
                    <input class="form-control" name="shippingtime" id="shippingtimeid" type="text" placeholder="Shipping Time (Day)">
                </div>
            </div>
            <div class="form-group has-success has-feedback">
                <label class="col-sm-2 control-label" for="inputSuccess">Shipping Cost</label>
                <div class="col-sm-10">
                    <input class="form-control" id="shippingcostid" value="0" name="shippingcost" type="number" placeholder="Shipping Cost" step="1" pattern="[0-9]+([\.,][0-9]+)?" value="discount" min="0">
                </div>
            </div>
            <div class="form-group has-success has-feedback">
                <label class="col-sm-2 control-label" for="inputSuccess">External Link</label>
                <div class="col-sm-10">
                    <input class="form-control" name="externallink" id="externallinkid" type="text" placeholder="External Link">
                </div>
            </div>

            <h3 class="text-center">Product Specification</h3>
            <div class="row well">
                <div id="fitures-div">

                </div>
                <div class="row" id="newSpecificationbtn" style="display: none">
                    <button type="button" class="btn btn-info btn-sm pull-right" data-toggle="modal" data-target="#specificationModal">Add New Specification</button>
                </div>
            </div>
            <h3 class="text-center">Product Inventory</h3>
            <div class="form-group has-success has-feedback">
                <div class="container-fluid bg-3 text-center well">    
                    <div class="row">
                        <div class="col-sm-2">
                            <label for="text">Size</label>
                            <div class="form-group has-success has-feedback" id="Psize">
                                <div class="dropdown" id="Psize">
                                    <button id="szbid" class="btn btn-primary btn-sm dropdown-toggle get-size" type="button" data-toggle="dropdown">Size
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu get-size-list"></ul>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-1">
                            <label for="text">Add</label>
                            <button type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#sizeModal">...</button>
                        </div>
                        <div class="col-sm-2">
                            <label for="text">Color</label>
                            <div class="form-group has-success has-feedback">
                                <div class="dropdown">
                                    <button id="clbid" class="btn btn-primary btn-sm dropdown-toggle get-color" type="button" data-toggle="dropdown">Color
                                        <span class="caret"></span></button>
                                    <ul class="dropdown-menu get-color-list">
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-1">
                            <label for="text">Add</label>
                            <button type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#colorModal">...</button>
                        </div>
                        <div class="col-sm-2">
                            <label for="text">Original Price</label>
                            <input class="form-control" id="priceid" type="number" name="priceid" placeholder="price" pattern="[0-9]+([\.,][0-9]+)?" step="1" value="0" min="0">
                        </div>
                        <div class="col-sm-2"> 
                            <label for="text">Sale Price:</label>
                            <input class="form-control" id="discountid" value="0" name="discountedPrice" type="number" placeholder="discount" step="1" pattern="[0-9]+([\.,][0-9]+)?" value="discount" min="0">
                        </div>
                        <div class="col-sm-2"> 
                            <label for="text">Quantity:</label>
                            <input class="form-control" id="quntid" name="available" value="1" type="number" placeholder="quantity" pattern="[0-9]+([\.,][0-9]+)?" value="quantity" min="1">
                        </div>
                    </div>
                </div>
                <div class="container-fluid bg-3 text-center well">    
                    <div class="row">
                        <div class="col-sm-2">
                            <label for="text">Product Length:</label>
                            <input class="form-control" id="Productlen" name="plength" value="1" type="number" placeholder="Product Length" pattern="[0-9]+([\.,][0-9]+)?" value="ProductLength" value="0" min="0">
                        </div>
                        <div class="col-sm-2">
                            <label for="text">Product Width:</label>
                            <input class="form-control" id="Productwidth" name="pwidth" value="1" type="number" placeholder="Product Width" pattern="[0-9]+([\.,][0-9]+)?" value="ProductWidth" value="0" min="0">
                        </div>
                        <div class="col-sm-2"> 
                            <label for="text">Product Height:</label>
                            <input class="form-control" id="ProductHig" name="pheight" type="Number" placeholder="Product Height" pattern="[0-9]+([\.,][0-9]+)?" value="0" min="0">
                        </div>
                        <div class="col-sm-2"> 
                            <label for="text">Product Weight:</label>
                            <input class="form-control" id="Productwight" name="pweight" type="Number" placeholder="Product Weight" pattern="[0-9]+([\.,][0-9]+)?" value="0" min="0">
                        </div>
                        <div class="col-sm-4">
                            <button type="button" class="btn btn-info btn-lg" id="add" style="margin-top: 12%;">Add Product</button>
                        </div>
                    </div>
                </div>

                <div class="col-lg-12" id="billing_items_div">
                    <table class='table table-bordered'  id='bill_table'> 
                        <thead>
                            <tr>
                                <th>Size</th>
                                <th>Color</th>
                                <th>Original Price</th>
                                <th>Sale Price</th>
                                <th>Quantity</th>
                                <th>Product Length</th>
                                <th>Product Width</th>
                                <th>Product Height</th>
                                <th>Product Weight</th>
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
</div>
<div class="modal-loader"> Place at bottom of page </div>

<!--Add Category modal-->
<div class="modal fade" id="categoryModal" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Add Category</h4>
            </div>
            <div class="modal-body">
                <!--<form class="form-horizontal" id="CategoryId" name="CategoryBean" action="/web/save-category">-->
                <div class="form-group has-success has-feedback">
                    <label class="col-sm-2 control-label" for="inputSuccess">Category Name</label>
                    <div class="col-sm-10">
                        <input class="form-control" name="Category" id="category-name" type="text" placeholder="Category Name" required="">
                    </div>
                </div>
                <div class="form-group has-success has-feedback">
                    <label class="col-sm-2 control-label" for="inputSuccess">Category Parent</label>
                    <div class="dropdown">
                        <button class="btn btn-primary dropdown-toggle get-category-parent" style="margin-left: 1%;" type="button" data-toggle="dropdown">Category Parent
                            <span class="caret"></span></button>
                        <ul class="dropdown-menu get-category-parent-list" style="margin-left: 18%;">

                        </ul>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-9"></div>
                    <div class="col-sm-3">
                        <span class="pull-right">
                            <button type="button" id="addCategorySubmit" class="btn btn-default">
                                <span class="glyphicon glyphicon-save"></span>Save</button>
                        </span>
                    </div>
                </div>
                <!--</form>-->
            </div>
        </div>
    </div>
</div>
<!--Add Category modal-->

<!--Add vendor modal-->
<div class="modal fade" id="vendorModal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Add Vendor</h4>
            </div>
            <div class="modal-body">
                <!--<form method="POST" class="form-horizontal" id="AddVendor" name="ColorDto" action="/web/save-vendor">-->
                <div class="form-group has-success has-feedback">
                    <input class="form-control" name="VendorName" id="VendorID" type="text" placeholder="Vendor Name" required="">
                </div>
                <div class="form-group has-success has-feedback">
                    <input class="form-control" name="Street" id="StreetID" type="text" placeholder="Street" required="">
                </div>
                <div class="form-group has-success has-feedback">
                    <input class="form-control" name="Street2" id="Street2ID" type="text" placeholder="Street (Optional)">
                </div>
                <div class="form-group has-success has-feedback">
                    <input class="form-control" name="City" id="CityID" type="text" placeholder="City" required="">
                </div>
                <div class="form-group has-success has-feedback">
                    <input class="form-control" name="State" id="StateID" type="text" placeholder="State" required="">
                </div>
                <div class="form-group has-success has-feedback">
                    <input class="form-control" name="Country" id="CountryID" type="text" placeholder="Country" required="">
                </div>
                <div class="form-group has-success has-feedback">
                    <input class="form-control" name="Zip" id="ZipID" type="text" placeholder="Zip" required="">
                </div>
                <div class="form-group has-success has-feedback">
                    <input class="form-control" name="Phone" id="PhoneID" type="text" placeholder="Phone" required="">
                </div>
                <div class="row">
                    <div class="col-sm-9"></div>
                    <div class="col-sm-3">
                        <span class="pull-right">
                            <button type="button" id="addVendorSubmit" class="btn btn-success btn-lg active">
                                <span class="glyphicon glyphicon-save"></span>Save</button>
                        </span>
                    </div>
                </div>
                <!--</form>-->
            </div>
        </div>
    </div>
</div>
<!--Add vendor modal-->

<!--Add Color modal-->
<div class="modal fade" id="colorModal" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Add Color</h4>
            </div>
            <div class="modal-body">
                <!--<form class="form-horizontal" id="addColor" name="ColorBean" action="/web/save-color">-->
                <div class="form-group">
                    <label for="text">Color Name:</label>
                    <input class="form-control" name="addcolor" id="addcolorId" type="text" placeholder="Color Name" required="">
                </div>
                <div>
                    <button id="addColorSubmit" type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-save"></span>Save</button>
                </div>
                <!--</form>-->
            </div>
        </div>
    </div>
</div>
<!--Add Color modal-->

<!--Add Size modal-->
<div class="modal fade" id="sizeModal" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Add Size</h4>
            </div>
            <div class="modal-body">
                <!--<form class="form-horizontal" id="Addsize" name="SizeBean" action="/web/save-sizegroup">-->
                <div class="row">
                    <div class="col-sm-10">
                        <div class="form-group">
                            <!--<label for="text">Add new size group</label>-->
                            <input class="form-control" name="addsize" id="AddSizeGroupId" type="text" placeholder="New size group" required="">
                        </div>
                    </div>
                    <div class="col-sm-2">
                        <button type="button" id="addSizeGroupSubmit" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-save"></span>Save</button>
                    </div>
                </div>
                <!--</form>-->
                <hr><hr>
                <!--<form class="form-horizontal" id="SizeGroup" name="SizeGroupBean" action="/web/save-size">-->
                <div class="row">
                    <div class="col-sm-4">
                        <div class="form-group">
                            <!--<label for="pwd">Add new size</label>-->
                            <div class="dropdown">
                                <button class="btn btn-primary dropdown-toggle get-sizegroup" style="margin-left: 1%;" type="button" data-toggle="dropdown">Size Group
                                    <span class="caret"></span></button>
                                <ul class="dropdown-menu get-sizegroup-list" style="margin-left: 18%;">
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <!--<label for="text">size:</label>-->
                            <input class="form-control" name="size" id="size-name" type="text" placeholder="Size" required="">
                        </div>
                    </div>
                    <div class="col-sm-2">
                        <button type="button" id="addSizeSubmit" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-save"></span>Save</button>
                    </div>
                </div>
                <!--</form>-->
            </div>
        </div>

    </div>
</div>
<!--Add Size modal-->

<!--Add specification modal-->
<div class="modal fade" id="specificationModal" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Add Specification</h4>
            </div>
            <div class="modal-body">
                <div class="row">
<!--                    <div class="col-sm-4">
                        <div class="form-group has-success has-feedback">
                            <label class="col-sm-2 control-label" for="inputSuccess">Category</label>
                            <div class="dropdown">
                                <button class="btn btn-primary dropdown-toggle get-category-spec" style="margin-left: 1%;" type="button" data-toggle="dropdown">Category Parent
                                    <span class="caret"></span></button>
                                <ul class="dropdown-menu get-category-spec-list" style="margin-left: 18%;">

                                </ul>
                            </div>
                        </div>
                    </div>-->
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label for="text">Specification</label>
                            <input class="form-control" name="Specification" id="addSpecificationId" type="text" placeholder="Specification" required="">
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label for="text">Value</label>
                            <input class="form-control" name="Value" id="addSpecificationValue" type="text" placeholder="Value" required="">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button id="addSpecificationSubmit" type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-save"></span>Save</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--Add specification modal-->

<!--Add specification value modal-->
<div class="modal fade" id="specificvalueModal" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Add New Value</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label for="text">Color Name:</label>
                    <input class="form-control" name="addvalue" id="value-text" type="text" placeholder="Value" required="">
                </div>
                <div>
                    <button id="addValueSubmit" type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-save"></span>Save</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--Add specification value modal-->
