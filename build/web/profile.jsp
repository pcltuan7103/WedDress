
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Profile Page</title>
        <link 
            rel="stylesheet"
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-alpha1/dist/css/bootstrap.min.css"
            >
        <script src=
                "https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-alpha1/dist/js/bootstrap.bundle.min.js">
        </script>
        <script src=
                "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js">
        </script>
        <style>
            .popup {
                display: none;
                position: fixed;
                z-index: 1;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.4);
                overflow: auto;
            }

            .popup-content {
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                background-color: #fefefe;
                padding: 20px;
                border: 1px solid #888;
                width: 80%;
                max-width: 600px;
            }

            .close {
                color: #aaa;
                float: right;
                font-size: 28px;
                font-weight: bold;
                cursor: pointer;
            }

            .close:hover,
            .close:focus {
                color: black;
                text-decoration: none;
                cursor: pointer;
            }

            body {
                background: rgb(99, 39, 120)
            }

            .form-control:focus {
                box-shadow: none;
                border-color: #BA68C8
            }

            .profile-button {
                background: rgb(99, 39, 120);
                box-shadow: none;
                border: none
            }

            .profile-button:hover {
                background: #682773
            }

            .profile-button:focus {
                background: #682773;
                box-shadow: none
            }

            .profile-button:active {
                background: #682773;
                box-shadow: none
            }

            .back:hover {
                color: #682773;
                cursor: pointer
            }

            .labels {
                font-size: 11px
            }

            .add-experience:hover {
                background: #BA68C8;
                color: #fff;
                cursor: pointer;
                border: solid 1px #BA68C8
            }

            .popup {
                display: none;
                position: fixed;
                z-index: 1;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                overflow: auto;
                background-color: rgba(0, 0, 0, 0.4);
            }

            .popup-content {
                display: block;
                background-color: #fefefe;
                padding: 20px;
                border: 1px solid #888;
                width: 80%;
                max-width: 600px;
            }

            .close {
                color: #aaa;
                float: right;
                font-size: 28px;
                font-weight: bold;
                cursor: pointer;
            }

            .close:hover,
            .close:focus {
                color: black;
                text-decoration: none;
                cursor: pointer;
            }

            input[type=text]{
                width: 100%;
                padding: 1%;
            }

            .warning-message {
                font-weight: bold;
                color: red;
                text-align: center;
                margin-bottom: 20px;
            }

            .delete-pop-up-actions{
                display: flex;
                justify-content: center;
            }
        </style>
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
        <c:set var="pro" value="${sessionScope.USER}"></c:set>
            <div class="container rounded bg-white mt-5 mb-5">
                <div class="row">
                    <div class="col-md-5 border-right">
                        <div class="d-flex flex-column align-items-center text-center p-3 py-5">
                            <img class="rounded-circle mt-5" width="150px" src="https://st3.depositphotos.com/15648834/17930/v/600/depositphotos_179308454-stock-illustration-unknown-person-silhouette-glasses-profile.jpg">
                            <span class="font-weight-bold">${pro.firstName}</span>
                        <span class="text-black-50">${pro.email}</span>
                        <span> </span>
                    </div>
                </div>
                <div class="col-md-7 border-right">
                    <form action="DispatcherServlet" method="POST">
                        <div class="p-3 py-5">
                            <input type="hidden" name="txtId" value="${pro.profileId}"/>
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <h4 class="text-right">Profile Settings</h4>
                            </div>
                            <div class="row mt-2">
                                <div class="col-md-6"><label class="labels">First Name</label><input type="text" class="form-control" placeholder="enter first name" value="${pro.firstName}" name="txtFirstName"></div>
                                <div class="col-md-6"><label class="labels">Last Name</label><input type="text" class="form-control" value="${pro.lastName}" placeholder="enter last name" name="txtLastname"></div>
                            </div>
                            <div class="row mt-3">
                                <div class="col-md-8"><label class="labels">Password<span style="color: red">*</span></label><input readonly="true" type="password" class="form-control" placeholder="enter password" value="${pro.password}" name="txtPassword"></div>
                                <div class="col-md-4"><label class="labels">Click to change password</label><button onclick="showPasswordChangePopup()" type="button" class="btn btn-primary" id="passwordChangeButton">Change password</button></div>
                                <div class="col-md-12"><label class="labels">Mobile Number</label><input type="text" class="form-control" placeholder="enter phone number" value="${pro.phoneNumber}" name="txtPhoneNumber"></div>
                                <div class="col-md-12"><label class="labels">Address</label><input type="text" class="form-control" placeholder="enter address" value="${pro.address}" name="txtAddress"></div>
                                <div class="col-md-12"><label class="labels">Email<span style="color: red">*</span></label><input type="email" required="true" class="form-control" placeholder="enter email" value="${pro.email}" name="txtEmail"></div>

                            </div>
                            <div class="justify-content-center d-flex">
                                <div class="mt-5 mr-3 text-center"><a id="deleteProfileLink" class="btn btn-danger" href="#" data-profile-id="${pro.profileId}">Delete Profile</a></div>
                                <div class="mt-5 text-center"><input class="btn btn-primary profile-button" type="submit" value="Save Profile" name="btAction"/></div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div id="popupDeleteProfile" class="popup">
            <div id="popupContent" class="popup-content">
                <span class="close " onclick="closePopup()">&times;</span>

                <h2>Warning</h2>
                <p class="warning-message">Are you sure!!!</p>
                <div class="form-group delete-pop-up-actions">                       
                    <button class="btn btn-primary btn-gap" onclick="closePopup()" >Cancel</button>
                    <form action="DispatcherServlet" method="POST">
                        <input type="hidden" id="profileIdDelete" name="txtId">
                        <input type="submit" value="Delete Profile" name="btAction" class="btn btn-danger" />
                    </form>
                </div>

            </div>
        </div>

        <!-- Password change popup -->
        <div id="passwordChangePopup" class="popup">
            <div class="popup-content">
                <span class="close" onclick="closePopup()()">&times;</span>
                <h2>Change Password</h2>
                <form action="DispatcherServlet" method="post">
                    <label for="newPassword">Old Password: </label>
                    <input type="password" id="newPassword" name="oldPassword" required><br>
                    <label for="newPassword">New Password:</label>
                    <input type="password" id="newPassword" name="password" required><br>
                    <label for="newPassword">Password Confirm</label>
                    <input type="password" id="newPassword" name="confirmPassword" required><br>
                    <!-- You can add more input fields if needed (e.g., confirm password) -->

                    <input type="submit" value="changePassword" name="btAction" class="btn btn-outline-info">
                </form>
            </div>
        </div>


        <jsp:include page="footer.jsp"></jsp:include>
            <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
            <script>
                    if (${requestScope.INVALID_FIELD  != null}) {
                        swal("Warning!", '${requestScope.INVALID_FIELD}', "warning");
                    }
                    document.addEventListener('DOMContentLoaded', function () {
                        var deleteProfileLink = document.getElementById('deleteProfileLink');
                        deleteProfileLink.addEventListener('click', function (event) {
                            event.preventDefault();
                            var profileId = this.getAttribute('data-profile-id');
                            openPopupDeleteProfile(profileId);
                        });
                    });

                    function openPopupDeleteProfile(id) {

                        document.getElementById('profileIdDelete').value = id;
                        document.getElementById('popupDeleteProfile').style.display = 'block';

                    }

                    function closePopup() {
                        document.getElementById('popupDeleteProfile').style.display = 'none';
                        document.getElementById('passwordChangePopup').style.display = 'none';
                    }


                    function showPasswordChangePopup() {
                        var popup = document.getElementById("passwordChangePopup");
                        popup.style.display = "block";
                    }
        </script>
    </body>
</html>
