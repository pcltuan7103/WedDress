<%-- 
    Document   : otpConfirm
    Created on : Jun 30, 2023, 11:50:35 AM
    Author     : 
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirm OTP Page</title>
    </head>
    <style>
        .form-container {
            max-width: 400px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f2f2f2;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
            margin-top:20px;
        }

        .form-container label {
            display: block;
            margin-bottom: 10px;
            font-weight: bold;
        }

        .form-container input[type="text"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
            font-size: 14px;
            margin-bottom: 20px;
        }

        .form-container button[type="submit"] {
            background-color: #4caf50;
            color: #fff;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }

        .form-container button[type="submit"]:hover {
            background-color: #45a049;
        }
    </style>

    <body>
        <jsp:include page="header.jsp"></jsp:include>
        <c:if test="${sessionScope.USER_ID_CHANGE_PASSWORD != null}">
            <div class="form-container">
                <form action="DispatcherServlet" method="get">
                    <label for="otp">Enter OTP:</label>
                    <input type="text" id="otp" name="otp">
                    <input type="submit" name="btAction" value="Verify" class="btn btn-primary"/>
                </form>
            </div>
        </c:if>
        <c:if test="${sessionScope.USER_REGISTER != null}">
            <div class="form-container">
                <form action="DispatcherServlet" method="get">
                    <label for="otp">Enter OTP:</label>
                    <input type="text" id="otp" name="otp">
                    <input type="submit" name="btAction" value="Register" class="btn btn-primary"/>
                </form>
            </div>
        </c:if>
        <c:if test="${sessionScope.USER_ID != null}">
            <div class="form-container">
                <form action="DispatcherServlet" method="get">
                    <label for="otp">Enter OTP:</label>
                    <input type="text" id="otp" name="otp">
                    <input type="submit" name="btAction" value="Verify" class="btn btn-primary"/>
                </form>
            </div>
        </c:if>
        <jsp:include page="footer.jsp"></jsp:include>
    </body>
</html>
