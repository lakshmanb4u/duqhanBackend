<%-- 
    Document   : addProduct
    Created on : Jan 11, 2017, 3:00:43 PM
    Author     : Weavers-web
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add New Product</title>
    </head>
    <body>
        <script type="text/javascript">
            $(document).ready(function () {
                $(".dropdown-menu li a").click(function () {
                    $(this).parents(".dropdown").find('.btn').html($(this).text() + ' <span class="caret"></span>');
                    $(this).parents(".dropdown").find('.btn').val($(this).data('value'));
                });
            });
        </script>
        <div class="container">
            <div class="col-sm-2"></div>
            <div class="col-sm-8">
                <h2 class="text-center text-info">Add New Product</h2>
                <form class="form-horizontal">
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Products Name</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="focusedInput" type="text" placeholder="products name">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Product Size</label>
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" style="margin-left: 1%;" type="button" data-toggle="dropdown">products size
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu" style="margin-left: 18%;">
                                <li><a href="#">HTML</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Product Color</label>
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" style="margin-left: 1%;" type="button" data-toggle="dropdown">products color
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu" style="margin-left: 18%;">
                                <li><a href="#">HTML</a></li>
                            </ul>
                        </div>
                    </div>

                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Price</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="focusedInput" type="number" placeholder="price" pattern="[0-9]+([\.,][0-9]+)?" step="0.01" value="price" min="0">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Discount</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="focusedInput" type="number" placeholder="discount" step="0.01" pattern="[0-9]+([\.,][0-9]+)?" value="discount" min="0">
                        </div>
                    </div>
                    <div class="form-group has-success has-feedback">
                        <label class="col-sm-2 control-label" for="inputSuccess">Quantity</label>
                        <div class="col-sm-10">
                            <input class="form-control" id="focusedInput" type="number" placeholder="quantity" pattern="[0-9]+([\.,][0-9]+)?" value="quantity" min="1">
                        </div>
                    </div>
                    <div class="col-sm-6">
                    </div>
                    <div class="col-sm-6 text-center">
                        <a href="#" class="btn btn-primary btn-lg active center-block" role="button" aria-pressed="true">Submit</a>
                    </div>
                </form>
            </div>
            <div class="col-sm-2"></div>
        </div>
    </body>
</html>
