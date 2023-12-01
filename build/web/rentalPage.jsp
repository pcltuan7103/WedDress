<%-- 
    Document   : rentalPage
    Created on : Jun 26, 2023, 9:57:09 PM
    Author     : ptd
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Rental Staff Page</title>
        <style>
            /* CSS styles go here */
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
            }

            h1 {
                text-align: center;
            }

            .container-parent {
                max-width: 1440px;
                margin: 20px auto;
                padding: 20px;
                background-color: #f4f4f4;
                border: 1px solid #ccc;
            }

            table {
                width: 100%;
                border-collapse: collapse;
            }

            th, td {
                padding: 8px;
                text-align: left;
                border-bottom: 1px solid #ccc;
            }

            th {
                background-color: #f2f2f2;
                font-weight: bold;
            }

            .actions {
                margin-top: 10px;
            }

            .actions button {
                margin-right: 10px;
            }
            img {
                height: 100px;
            }

            .actions{
                width: 250px;
            }

            .button {
                display: inline-block;
                padding: 10px 10px;
                background-color: lightblue;
                color: #fff;
                font-size: 16px;
                text-decoration: none;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }

            .button-update{
                margin-top: 20px;

            }

            .button:hover {
                background-color: #ccc;
            }

            .button:focus {
                outline: none;
            }

            .button:active {
                background-color: #3e8e41;
                transform: translateY(1px);
            }         

            .button-delete{
                background-color: lightcoral;
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
                margin: 15% auto;
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

            .button-gap{
                margin-right: 20px;
            }

            .tab {
                overflow: hidden;
            }

            .tab button {
                background-color: #f2f2f2;
                float: left;
                border: none;
                outline: none;
                cursor: pointer;
                padding: 10px 20px;
                transition: 0.3s;
            }

            .tab button:hover {
                background-color: #ddd;
            }

            .tab button.active {
                background-color: #ccc;
            }

            /* Content section styles */
            .tabcontent {
                display: none;
                padding: 20px;

                border-top: none;
            }



        </style>
    </head>
    <body>
        <c:if test="${sessionScope.USER.roleName ne 'rental_staff'}">
            <jsp:forward page="login.jsp"></jsp:forward>
        </c:if>
        <jsp:include page="header.jsp"></jsp:include>
        <c:set var="profile" value="${sessionScope.USER}" />
        <div class="container-parent">
            <div class="tab">
                <button class="tablinks active" onclick="openTab(event, 'Tab1')">Product</button>
                <button class="tablinks" onclick="openTab(event, 'Tab2')">Rent Confirm</button>
                <button class="tablinks" onclick="openTab(event, 'Tab3')">Add Product</button>

            </div>

            <div id="Tab1" class="tabcontent" style="display: block;">
                <h3>Product For Rent Management</h3>

                <table>
                    <tr>
                        <th>Image</th>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Price</th>       
                        <th>Actions</th>
                    </tr>

                    <c:forEach items="${sessionScope.PRODUCTS}" var="product">
                        <tr>
                            <td><img src="${product.image}" alt="product image"></td>
                            <td>${product.id}</td>
                            <td>${product.name}</td>
                            <td style="width: 500px">${product.description}</td>
                            <td>$ ${product.price}</td>                   
                            <td class="actions" style="width: fit-content">
                                <button class="button" onclick="openPopup('${product.image}', ${product.id}, '${product.name}', '${product.description}', ${product.price})">Update Product</button>                     
                                <button class="button button-delete" onclick="openPopupDelete('${product.id}')">Delete Product</button>                                                                 
                            </td>
                        </tr>
                    </c:forEach>


                </table>
            </div>
            <div id="Tab2" class="tabcontent" >
                <h3>Rental Product Confirmation</h3>
                <table>
                    <tr>                                       
                        <th>Name</th>
                        <th>Description</th>                      
                        <th>Photo Date</th>  
                        <th>Price</th>    
                        <th style="text-align: center">Actions</th>
                    </tr>
                    <c:forEach items="${sessionScope.LIST_CART_PRODUCT_ADMIN}" var="p">
                        <th style="border: none;">${count.index + 1}</th>

                        <tr>                       
                            <td>${p.name}</td>
                            <td>${p.description}</td>
                            <td>${p.orderDate}</td>
                            <td>$ ${p.price}</td>      
                        </tr>

                        <th style="border-bottom: none"></th>
                        <th style="border-bottom: none"></th>
                        <th style="border-bottom: none"></th>
                        <th style="border-bottom: none"></th>

                        <th style="border-bottom: none; text-align: center">
                            <form class="DispatcherServlet" method="POST">
                                <input type="hidden" name="orderId" value="${p.orderId}"/>
                                <input type="hidden" name="orderDetailId" value="${p.orderDetailId}"/>
                                <input type="submit" class="button btn-primary btn-delete-all-item" value="Confirm Rent" name="btAction"/>  
                            </form>

                        </th>
                        <tr style="border-top: 1px solid black;"></tr>
                    </c:forEach>
                </table>
            </div>

            <div id="Tab3" class="tabcontent" style="width: 500px">
                <h2>Adding Rental Product</h2>
                <form action="DispatcherServlet" method="POST">

                    <p for="locationImage">Image Link:</p>
                    <input type="text"  name="productImage" required="true"/>

                    <p for="locationName">Name:</p>
                    <input type="text" name="productName" required="true"/>

                    <p for="locationDescription">Description:</p>
                    <input type="text"  name="productDescription" required="true"/>

                    <p for="locationPrice">Price:</p>
                    <input type="text"  name="productPrice" required="true"/>
                    
                    <p for="locationPrice">Stock:</p>
                    <input type="text" name="productStock" required="true"/>
                    <br/> 
                    <input type="submit" value="Add Product" name="btAction" class="button button-update"/>
                </form>

            </div>

            <div id="popup" class="popup">
                <div id="popupContent" class="popup-content">
                    <span class="close" onclick="closePopup()">&times;</span>
                    <h2>Update Location</h2>
                    <form action="DispatcherServlet" method="POST">
                        <input type="hidden" id="productId" name="productId">

                        <p for="locationImage">Image Link:</p>
                        <input type="text" id="productImage" name="productImage" required="true"/>

                        <p for="locationName">Name:</p>
                        <input type="text" id="productName" name="productName" required="true" />

                        <p for="locationDescription">Description:</p>
                        <input type="text" id="productDescription" name="productDescription" required="true"/>

                        <p for="locationPrice">Price:</p>
                        <input type="text" id="productPrice" name="productPrice" required="true"/>
                        <br/> 
                        <input type="submit" value="UpdateProduct" name="btAction" class="button button-update"/>
                    </form>
                </div>
            </div>

            <div id="popupDelete" class="popup">
                <div id="popupContent" class="popup-content">
                    <span class="close" onclick="closePopup()">&times;</span>

                    <h2>Warning</h2>
                    <p class="warning-message">Are you sure!!!</p>
                    <div class="form-group delete-pop-up-actions">                       
                        <button class="button button-gap" onclick="closePopup()" >Cancel</button>
                        <form action="DispatcherServlet" method="POST">
                            <input type="hidden" id="rentalProductId" name="txtProductId">
                            <input type="submit" value="DeleteProduct" name="btAction" class="button button-delete" />
                        </form>
                    </div>

                </div>
            </div>

        </div>

        <jsp:include page="footer.jsp"></jsp:include>
        <script>
            function openTab(event, tabName) {
                var i, tabcontent, tablinks;
                tabcontent = document.getElementsByClassName("tabcontent");
                for (i = 0; i < tabcontent.length; i++) {
                    tabcontent[i].style.display = "none";
                }
                tablinks = document.getElementsByClassName("tablinks");
                for (i = 0; i < tablinks.length; i++) {
                    tablinks[i].className = tablinks[i].className.replace(" active", "");
                }
                document.getElementById(tabName).style.display = "block";
                event.currentTarget.className += " active";
            }


            function openPopup(image, id, name, description, price) {
                document.getElementById('productId').value = id;
                document.getElementById('productName').value = name;
                document.getElementById('productDescription').value = description;
                document.getElementById('productPrice').value = price;
                document.getElementById('productImage').value = image;

                document.getElementById('popup').style.display = 'block';
            }

//            function openPopupSchedule(id, name, description, price, orderDate) {
//                document.getElementById('scheduleId').value = id;
//                document.getElementById('scheduleName').value = name;
//                document.getElementById('scheduleDescription').value = description;
//                document.getElementById('schedulePrice').value = price;
//                document.getElementById('scheduleOrderDate').value = orderDate;
//
//                document.getElementById('popup').style.display = 'block';
//            }

            function openPopupDelete(id) {
                document.getElementById('rentalProductId').value = id;
                document.getElementById('popupDelete').style.display = 'block';
            }

            function closePopup() {
                document.getElementById('popup').style.display = 'none';
//                document.getElementById('popupSchedule').style.display = 'none';
                document.getElementById('popupDelete').style.display = 'none';
            }
        </script>
    </body>
</html>
