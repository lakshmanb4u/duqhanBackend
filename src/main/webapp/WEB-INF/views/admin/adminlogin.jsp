<%-- 
    Document   : adminlogin
    Created on : Mar 7, 2017, 10:54:22 AM
    Author     : weaversAndroid
--%>

<div id="contact" class="container well">
    <h3 class="text-center">Login</h3>
    <form class="form-horizontal" method="POST" name="loginBean" action="/web/adminlogin-action">
        <div class="form-group">
            <label class="control-label col-sm-2" for="email">Email:</label>
            <div class="col-sm-10">
                <input type="email" class="form-control" id="email" name="email" placeholder="Enter email">
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="pwd">Password:</label>
            <div class="col-sm-10"> 
                <input type="password" name="password" class="form-control" id="password" placeholder="Enter password">
            </div>
        </div>
        <div class="form-group"> 
            <div class="col-sm-offset-2 col-sm-10">
                <div class="checkbox">
                    <label><input type="checkbox"> Remember me</label>
                </div>
            </div>
        </div>
        <div class="form-group"> 
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Submit</button>
            </div>
        </div>
    </form>
</div>