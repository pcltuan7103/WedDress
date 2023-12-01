<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>My Wedding Planner</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
        <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
        <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <style>
            /* CSS styles for the header section */
            .header {
                background-color: #f1f1f1;
                padding: 20px;
                text-align: center;
            }

            .header h1 {
                margin: 0;
                color: #333;
            }

            .header .login-register {
                margin: 10px;
                display: flex;
                justify-content: center;

            }

            .header .login-register a {
                margin-right: 10px;
                text-decoration: none;
                color: #333;
            }

            .header .login-register a:hover {
                color: red;
            }

            .header .search-bar {
                margin-bottom: 10px;
            }

            .header .search-bar input[type="text"] {
                padding: 5px;
                width: 300px;
            }

            .header .search-bar input[type="submit"] {
                padding: 5px 10px;
                background-color: #333;
                color: #fff;
                border: none;
                cursor: pointer;
            }

            .container-header {
                display: flex;
                justify-content: center;
                align-items: center;

            }

            input[type="number"] {
                padding: 10px;
                font-size: 16px;
                border-radius: 5px;
                border: 1px solid #ccc;
                margin: 4px 7px 0px 10px;
            }

            .container-header input[type="submit"] {
                padding: 10px 20px;
                font-size: 16px;
                border-radius: 5px;
                border: none;
                background-color: #4CAF50;
                color: white;
            }

            .container-header input[type="submit"]:hover{
                background-color: #3e8e41;
                cursor: pointer;
            }

            a{
                text-decoration: none;
            }


        </style>
    </head>
    <body>
        <div class="header">
            <h1>My Wedding Planner</h1>

            <div class="login-register">
                <c:if test="${sessionScope.USER.roleName eq 'user' || sessionScope.USER == null}">
                    <a href="DispatcherServlet?btAction=Home">Home - </a>
                </c:if>
                <c:if test="${sessionScope.USER.roleName eq 'staff'}">
                    <a href="photoHome.jsp">Manage - </a>
                </c:if>
                <c:if test="${sessionScope.USER.roleName eq 'rental_staff'}">
                    <a href="rentalPage.jsp">Manage - </a>
                </c:if>
                <c:if test="${sessionScope.USER != null}">

                    <p href="#">Hello <a href="profile.jsp"><strong>${sessionScope.USER.userName}</strong></a></p>
                                <c:if test="${sessionScope.USER.roleName eq 'admin'}">
                        <a href="admin.jsp">Admin</a>
                        <a href="showFeedback.jsp">Manage Feedback</a>
                    </c:if>
                    <a href="DispatcherServlet?btAction=Logout"> - Logout</a>
                </c:if>
                <c:if test="${sessionScope.USER == null}">
                    <a href="login.jsp">Login - </a>
                    <a href="register.jsp">Register</a>
                </c:if>          
                <c:if test="${sessionScope.USER != null and sessionScope.USER.roleName eq 'user'}">
                    <a class="" href="feedback.jsp">
                        Feedback
                    </a>
                    <a class="" href="reject.jsp">
                        <i class="fa fa-history"></i> reject
                        <span class="badge badge-light">${sessionScope.LIST_REJECT_SCHEDULE.size() + sessionScope.LIST_REJECT_PRODUCT.size()}</span>
                    </a>
                    <a class="" href="cart.jsp">
                        <i class="fa fa-shopping-cart"></i> Cart
                        <span class="badge badge-light">${sessionScope.CART_ITEM}</span>
                    </a>
                </c:if>
            </div>

            <c:if test="${sessionScope.USER.roleName eq 'user' || sessionScope.USER == null}">
                <div class="search-bar">
                    <form action="DispatcherServlet" method="GET">
                        <input type="text" name="txtSearch" placeholder="Search for locations, studios, prices..." value="${param.txtSearch}">
                        <input type="submit" value="Search" name="btAction">
                    </form>
                </div>
                <div class="container-header">
                    <form action="DispatcherServlet" method="GET">
                        <input name="txtMinNumber" type="number" min="0" placeholder="Enter a positive number" id="txtMinNumber" value="${param.txtMinNumber}">
                        <input name="txtMaxNumber" type="number" min="0" placeholder="Enter a positive number" id="txtMaxNumber" value="${param.txtMaxNumber}">               
                        <input type="submit" value="Filter" id="filterButton" name="btAction">
                    </form>
                </div>
            </c:if>
        </div>

        <script>
            const inputNumber1 = document.getElementById('txtMinNumber');
            const inputNumber2 = document.getElementById('txtMaxNumber');
            const filterButton = document.getElementById('filterButton');

            if (filterButton) {
                filterButton.addEventListener('click', (event) => {
                    const number1 = parseInt(inputNumber1.value, 10);
                    const number2 = parseInt(inputNumber2.value, 10);

                    if (number1 >= 0 && number2 >= 0 && number2 > number1) {
                        // Filter the numbers or perform some action
                        console.log('Filtered numbers:', number1, number2);
                    } else {
                        event.preventDefault();
                        alert('Please enter valid positive numbers, with the second number being greater than the first number.');
                    }
                });
            }
        </script>

    </body>
</html>

