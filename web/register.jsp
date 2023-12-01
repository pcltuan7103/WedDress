<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register Page</title>
        <style>
            .valid {
                border: 2px solid green;
            }

            .invalid {
                border: 2px solid red !important;
            }


        </style>
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-6">
                        <h2 class="text-center">Register</h2>
                        <form action="DispatcherServlet" method="POST">
                            <div class="form-group">
                                <label for="txtUserName">User Name<span style="color: red">*</span></label>
                                <input type="text" id="txtUserName" name="txtUserName" class="form-control" value="${param.txtUserName}" required=""/>
                        </div>
                        <div class="form-group">
                            <label for="txtPassword">Password<span style="color: red">*</span></label>
                            <input type="password" id="txtUserName" name="txtPassword" class="form-control" value="${param.txtPassword}" required=""/>
                        </div>
                        <!--                        <div class="form-group">
                                                    <label for="txtFirstName">First Name</label>
                                                    <input type="text" id="txtFirstName" name="txtFirstName" class="form-control" value="${param.txtFirstName}" required=""/>
                                                </div>
                                                <div class="form-group">
                                                    <label for="txtLastName">Last Name</label>
                                                    <input type="text" id="txtLastName" name="txtLastName" class="form-control" value="${param.txtLastName}" required=""/>
                                                </div>-->
                        <div class="form-group">
                            <label for="txtEmail">Email<span style="color: red">*</span></label>
                            <input type="text" name="txtEmail" class="form-control" value="${param.txtEmail}" id="emailInput" placeholder="Enter your email" onchange="validateEmail()" oninput="clearError()" required=""/>
                        </div>
                        <!--                        <div class="form-group">
                                                    <label for="txtPhone">Phone</label>
                                                    <input type="text" id="txtPhone" name="txtPhone" class="form-control" value="${param.txtPhone}" required=""/>
                                                </div>
                                                <div class="form-group">
                                                    <label for="txtAddress">Address</label>
                                                    <input type="text" id="txtAddress" name="txtAddress" class="form-control" value="${param.txtAddress}" required=""/>
                                                </div>-->
                        <div class="text-center mt-2 mb-3">
                            <button type="submit" name="btAction" value="Register" class="btn btn-secondary">Add Account</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <jsp:include page="footer.jsp"></jsp:include>
            <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
            <script>
                                if (${requestScope.ERROR_USER_NAME != null}) {
                                    swal("Invalid UserName or email exist");
                                }
                                function validateEmail() {
                                    var emailInput = document.getElementById("emailInput");
                                    var email = emailInput.value.trim();

                                    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

                                    if (email === "") {
                                        setError(emailInput, "Please enter an email address");
                                        emailInput.classList.remove("valid");
                                        emailInput.classList.add("invalid");
                                    } else if (!emailRegex.test(email)) {
                                        setError(emailInput, "Please enter a valid email address");
                                        emailInput.classList.remove("valid");
                                        emailInput.classList.add("invalid");
                                    } else {
                                        clearError(emailInput);
                                        emailInput.classList.remove("invalid");
                                        emailInput.classList.add("valid");
                                    }
                                }

                                function setError(inputElement, errorMessage) {
                                    inputElement.setCustomValidity(errorMessage);
                                }

                                function clearError() {
                                    var emailInput = document.getElementById("emailInput");
                                    emailInput.setCustomValidity("");
                                }

        </script>
    </body>
</html>
