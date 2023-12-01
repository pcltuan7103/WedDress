
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Page</title>
        <style>
            /* Tabbed interface styles */
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
            }

            h1 {
                text-align: center;
            }

            .container-parent {
                max-width: 1550px;
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
                /*border-bottom: 1px solid #ccc;*/
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

            .btn-delete-all-item{
                margin: 0 0 0 10px;
            }

            .card-price{
                padding: 10px;
                text-align: end;
            }


            .card .card-image img {
                height: 230px;
                width: 250px;
            }
            /*            .filter-container{
                            width: 300px;
                            display: flex;
                            justify-content: center;
                            margin-top: 42px;
                        }
            
                        .category{
                            width: 170px;
                            padding: 10px;
                            font-size: 18px;
                            text-align: center;
                        }
            
                        .show{
                            text-align: center;
                        }*/

        </style>
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
    </head>
    <body>
        <c:if test="${sessionScope.USER.roleName ne 'admin'}">
            <jsp:forward page="login.jsp"></jsp:forward>
        </c:if>
        <jsp:include page="header.jsp"></jsp:include>
        <c:set var="profile" value="${sessionScope.USER}" />
        <c:set var="totalPriceCart" value="0" />
        <c:set var="idOrder" value="0"/>
        <div class="container-parent">
            <div class="tab">
                <!--<button class="tablinks active" onclick="openTab(event, 'Tab1')">Staff</button>-->
                <button class="tablinks active" onclick="openTab(event, 'Tab1')">User</button>
                <button class="tablinks" onclick="openTab(event, 'Tab2')">Cart</button>
                <button class="tablinks" onclick="openTab(event, 'Tab3')">Add Location</button>
                <button class="tablinks" onclick="openTab(event, 'Tab4')">Add Product</button>
                <button class="tablinks" onclick="openTab(event, 'Tab5')">Manage Location</button>
                <button class="tablinks" onclick="openTab(event, 'Tab6')">Manage Product</button>

            </div>

            <div id="Tab1" class="tabcontent" style="display: block">
                <c:if test="${sessionScope.LIST_USER.size() > 0}">
                    <h3>User</h3>
                    <table>
                        <tr>                                       
                            <th>User Name</th>
                            <th>First Name</th>
                            <th>Last Name</th>                      
                            <th>Email</th>  
                            <th>Phone Number</th>  
                            <th>Address</th>           
                            <th>Role Name</th>           
                            <th style="text-align: center">Actions</th>

                        </tr>
                        <c:forEach items="${sessionScope.LIST_USER}" var="user">
                            <tr>                       
                                <td>${user.userName}</td>
                                <td>${user.firstName}</td>
                                <td>${user.lastName}</td>
                                <td>${user.email}</td>
                                <td>${user.phoneNumber}</td>      
                                <td>${user.address}</td>      
                                <td>${user.roleName}</td>      

                                <td class="actions" style="text-align: center">
                                    <button class="button" onclick="openPopupAccount('${user.profileId}', '${user.userName}',
                                                    '${user.firstName}', '${user.lastName}', '${user.email}',
                                                    '${user.phoneNumber}', '${user.address}', '${user.userId}', '${user.roleName}')">
                                        Update
                                    </button>   
                                    <button class="button button-delete btn-delete-all-item" onclick="openPopupDeleteAccount('${user.profileId}')">Delete</button>                   
                                </td>
                            </tr>
                        </c:forEach>
                    </table>

                </c:if>
            </div>

            <div id="Tab2" class="tabcontent">
                <div class="dropdown show">
                    <h2>Cart Item</h2>
                    <a class="btn btn-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Category
                    </a>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                        <a class="dropdown-item" href="DispatcherServlet?btAction=CategoryFilterAdmin&category=all">All</a>
                        <a class="dropdown-item" href="DispatcherServlet?btAction=CategoryFilterAdmin&category=location">Locations</a>
                        <a class="dropdown-item" href="DispatcherServlet?btAction=CategoryFilterAdmin&category=schedule">Bookings</a>
                        <a class="dropdown-item" href="DispatcherServlet?btAction=CategoryFilterAdmin&category=studio">Studios</a>
                        <a class="dropdown-item" href="DispatcherServlet?btAction=CategoryFilterAdmin&category=product">Products</a>
                        <a class="dropdown-item" href="DispatcherServlet?btAction=CategoryFilterAdmin&category=combo">Combos</a>
                    </div>
                </div>
                <c:if test="${sessionScope.LIST_CART_SCHEDULE_ADMIN.size() > 0 || LIST_CART_PRODUCT_ADMIN.size() > 0}">

                    <div class="filter-container">
                        <table>
                            <tr>         
                                <th>Order</th>
                                <th>Name</th>
                                <th>Description</th>                      
                                <th>Photo Date</th>  
                                <th>Date Rent From</th>  
                                <th>Date Rent To</th>  
                                <th>Price</th>    
                                <th>Status</th>    
                                <th style="text-align: center">Actions</th>    
                            </tr>

                            <c:forEach var="entry" items="${sessionScope.LIST_CART_SCHEDULE_ADMIN}">
                                <!-- Get the key and value from the map entry -->
                                <c:set var="key" value="${entry.key}" />
                                <c:set var="value" value="${entry.value}" />

                                <!-- Iterate over the list -->
                                <c:forEach var="item" items="${value}" varStatus="count">
                                    <!-- Display the list item -->
                                    <c:set var="totalPriceCart" value="${totalPriceCart + item.price}"/>
                                    <tr>
                                        <td>${item.orderId}</td>
                                        <td>${item.name}</td>
                                        <td style="width: 400px">${item.description}</td>
                                        <td style="width: 150px">${item.orderDate}</td>
                                        <td style="width: 150px">${item.orderStartDate}</td>
                                        <td style="width: 150px">${item.orderEndDate}</td>
                                        <td>${item.price}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item.status eq 'confirm'}">
                                                    confirm
                                                </c:when>
                                                <c:when test="${item.status eq 'pending'}">
                                                    ${item.status}
                                                </c:when>
                                            </c:choose>

                                        </td>
                                        <c:if test="${item.status eq 'pending'}">
                                            <td style="border-bottom: none; text-align: center"> 
                                                <button class="button" onclick="openPopupUpdate('${item.orderDetailId}', '${item.itemId}', '${item.itemType}', '${item.orderId}', '${item.orderStartDate}', '${item.orderEndDate}')">Change</button>
                                            </td>
                                        </c:if>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <c:if test="${count.index == 1 && item.status eq 'pending'}">
                                            <td style="border-bottom: none; text-align: center; display: flex">

                                                <form action="DispatcherServlet" method="GET">
                                                    <input type="hidden" name="orderId" value="${item.orderId}" />
                                                    <input type="hidden" name="itemId" value="${item.itemId}" />
                                                    <input type="submit" name="btAction" value="Confirm Schedule" class="button btn-secondary" />
                                                </form>                                               

                                                <button style="width: 110px" class="button button-delete btn-delete-all-item" onclick="openPopupDelete('${item.orderId}', '${item.orderDetailId}', '${item.itemId}', '${item.itemType}')">Delete Item</button>  
                                            </td>   
                                        </c:if>   
                                    </tr>

                                </c:forEach>
                                <br/>
                                <tr style="border-bottom: 1px solid black"></tr>
                            </c:forEach >

                            <c:forEach var="product" items="${sessionScope.LIST_CART_PRODUCT_ADMIN}">
                                <c:set var="totalPriceCart" value="${totalPriceCart + product.price}"/>
                                <tr>
                                    <td>${product.orderId}</td>
                                    <td>${product.name}</td>
                                    <td style="width: 400px">${product.description}</td>
                                    <td style="width: 150px">${product.orderDate}</td>
                                    <td style="width: 150px">${product.orderStartDate}</td>
                                    <td style="width: 150px">${product.orderEndDate}</td>
                                    <td>${product.price}</td>
                                    <td>${product.status}</td>
                                    <c:if test="${product.status eq 'pending'}">
                                        <td style="border-bottom: none; text-align: center; display: flex"> 

                                            <form action="DispatcherServlet" method="GET">
                                                <input type="hidden" name="orderId" value="${product.orderId}" />
                                                <input type="hidden" name="orderDetailId" value="${product.orderDetailId}" />
                                                <input type="submit" name="btAction" value="Confirm Rent" class="button btn-secondary" />
                                            </form>

                                            <button style="margin: 0 1rem" class="button" onclick="openPopupUpdate('${product.orderDetailId}', '${product.itemId}', '${product.itemType}', '${product.orderId}', '${product.orderStartDate}', '${product.orderEndDate}')">Change</button>
                                            <button style="width: 110px" class="button button-delete btn-delete-all-item" onclick="openPopupDelete('${product.orderId}', '${product.orderDetailId}', '${product.itemId}', '${product.itemType}')">Delete Item</button>  
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>

                        </table>

                    </c:if>
                </div>

                <!--            <div id="Tab4" class="tabcontent">
                                <h3>Staff</h3>
                                <table>
                                    <tr>                                       
                                        <th>User Name</th>
                                        <th>First Name</th>
                                        <th>Last Name</th>                      
                                        <th>Email</th>  
                                        <th>Phone Number</th>  
                                        <th>Address</th>           
                                        <th>Role Name</th>           
                                        <th style="text-align: center">Actions</th>
                
                                    </tr>
                                    <tbody>
                                    <form action="DispatcherServlet" method="POST"> 
                                        <tr>                       
                                            <td>
                                                <input type="text" name="txtUserName" value="${param.txtUserName}"/>
                                            </td>
                                            <td>
                                                <input type="text" name="txtFistName" value="${param.txtFistName}"/>
                                            </td>
                                            <td>
                                                <input type="text" name="txtLastName" value="${param.txtLastName}"/>
                                            </td>
                                            <td>
                                                <input type="text" name="txtEmail" value="${param.txtEmail}"/>
                                            </td>
                                            <td>
                                                <input type="text" name="txtPhone" value="${param.txtPhone}"/>
                                            </td>      
                                            <td>
                                                <input type="text" name="txtAddress" value="${param.txtAddress}"/>
                                            </td>      
                                            <td>
                                                <select name="roleId">
                <c:forEach items="${sessionScope.LIST_STAFF_ROLE}" var="r">
                    <option value="${r.roleId}">${r.roleName}</option>
                </c:forEach>
            </select>
        </td>      
    
        <td class="actions" style="text-align: center">
            <input type="submit" name="btAction" value="Add Account" class="btn btn-secondary"/>
        </td>
    </tr>
    </form>
    </tbody>
    </table>
    </div>-->

            </div>
            <div id="Tab3" class="tabcontent" style="width: 500px" >
                <h3>Add Location</h3>
                <form action="DispatcherServlet" method="POST">

                    <p for="locationImage">Image Link:</p>
                    <input type="text" name="txtLocationImage" required=""/>

                    <p for="locationName">Name:</p>
                    <input type="text" name="txtLocationName" required=""/>

                    <p for="locationDescription">Description:</p>
                    <input type="text" name="txtLocationDescription" required=""/>

                    <p for="locationPrice">Price:</p>
                    <input type="text" name="txtLocationPrice" required=""/>
                    <br/> 
                    <input type="submit" value="Add Location" name="btAction" class="button button-update"/>
                </form>
            </div>

            <div id="Tab4" class="tabcontent" style="width: 500px">
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

            <div id="Tab5" class="tabcontent" >
                <h3>Location For Rent Management</h3>

                <table>
                    <tr>
                        <th>Image</th>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Price</th>       
                        <th>Actions</th>
                    </tr>

                    <c:forEach items="${sessionScope.LOCATIONS}" var="location">
                        <tr>
                            <td><img src="${location.image}" alt="location image"></td>
                            <td>${location.id}</td>
                            <td>${location.name}</td>
                            <td style="width: 500px">${location.description}</td>
                            <td>$ ${location.price}</td>                   
                            <td class="actions" style="width: fit-content">
                                <button class="button" onclick="openPopupLocation('${location.image}', ${location.id}, '${location.name}', '${location.description}', ${location.price})">Update Location</button>                     
                                <button class="button button-delete" onclick="openPopupDeleteLocation('${location.id}')">Delete Location</button>                                                                 
                            </td>
                        </tr>
                    </c:forEach>


                </table>
            </div>

            <div id="Tab6" class="tabcontent">
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
                                <button class="button" onclick="openPopupProduct('${product.image}', ${product.id}, '${product.name}', '${product.description}', ${product.price})">Update Product</button>                     
                                <button class="button button-delete" onclick="openPopupDeleteProduct('${product.id}')">Delete Product</button>                                                                 
                            </td>
                        </tr>
                    </c:forEach>


                </table>
            </div>

            <div id="popupAccount" class="popup">
                <div id="popupContent" class="popup-content">
                    <span class="close" onclick="closePopup()">&times;</span>
                    <h2>Update Account</h2>
                    <form action="DispatcherServlet" method="POST">
                        <input type="hidden" id="accountId" name="accountId">                        
                        <input type="hidden" id="isUser" name="isUser">                        

                        <!--<p for="userName">User Name:</p>-->
                        <input type="hidden" id="userName" name="userName" required="true"/>

                        <p for="firstName">First Name:</p>
                        <input type="text" id="firstName" name="firstName" required="true"/>

                        <p for="lastName">Last Name:</p>
                        <input type="text" id="lastName" name="lastName" required="true"/>

                        <p for="email">Email:</p>
                        <input type="text" id="email" name="email" required="true"/>

                        <p for="phone">Phone: </p>
                        <input type="text" id="phoneNumber" name="phoneNumber" required="true"/>

                        <p for="address">Address: </p>
                        <input type="text" id="address" name="address" required="true"/>

                        <p for="address" id="roleNameText">Role Name </p>
                        <select name="roleId" id="userId">
                            <c:forEach items="${sessionScope.LIST_STAFF_ROLE}" var="r">
                                <option value="${r.roleId}">${r.roleName}</option>
                            </c:forEach>
                        </select>
                        <br/> 
                        <input type="submit" value="EditAccount" name="btAction" class="button button-update"/>
                    </form>
                </div>
            </div>


            <!-- pop-up item -->
            <div id="popupUpdate" class="popup">
                <div id="popupContent" class="popup-content" style="max-width: 900px">
                    <span class="close" onclick="closePopup()">&times;</span>

                    <h2>Update Item</h2>

                    <div class="card-container" >
                        <c:forEach items="${sessionScope.LIST_ORDER_ALL}" var="data">  
                            <div class="card" data-item-type="${data.itemType}" style="display: flex; flex-direction: row; margin: 20 0;">
                                <div class="card-image">
                                    <img src="${data.image}" alt="Image">
                                </div>
                                <div class="card-content ml-4">
                                    <h3>${data.name}</h3>
                                    <p>${data.description}</p>
                                    <p>Price For Rent: $ ${data.price}</p>
                                    <p><a href="ownerContact.jsp" class="contact-link">Contact</a></p>
                                    <form action="ChangeItemAdmin" method="POST">
                                        <input type="hidden" class="detail-input" name="detailId">                                     
                                        <input type="hidden" name="itemId" class="itemId-input"/>
                                        <input type="hidden" name="itemType" class="itemType-input"/>
                                        <input type="hidden" name="orderId" class="orderId-input"/>
                                        <input type="hidden" name="timeRange" class="timeRange-input"/>
                                        <input type="hidden" name="timeRangeReturn" class="timeRangeReturn-input"/>
                                        <input type="hidden" name="id" value="${data.itemId}"/>
                                        <button class="button">Change</button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </div>  
                </div>
            </div>


            <div id="popupDelete" class="popup">
                <div id="popupContent" class="popup-content">
                    <span class="close" onclick="closePopup()">&times;</span>

                    <h2>Warning</h2>
                    <p class="warning-message">Are you sure!!!</p>
                    <div class="form-group delete-pop-up-actions">                       
                        <button class="button button-gap" onclick="closePopup()" >Cancel</button>
                        <form action="deleteItem" method="POST">
                            <input type="hidden" id="orderId" name="orderId">
                            <input type="hidden" id="orderDetailId" name="orderDetailId">
                            <input type="hidden" id="iId" name="itemId">
                            <input type="hidden" id="iType" name="itemType">
                            <button class="button button-delete">Delete</button>
                        </form>
                    </div>

                </div>
            </div>

            <div id="popupLocation" class="popup">
                <div id="popupContent" class="popup-content">
                    <span class="close" onclick="closePopup()">&times;</span>
                    <h2>Update Location</h2>
                    <form action="DispatcherServlet" method="POST">
                        <input type="hidden" id="locationId" name="txtLocationId">

                        <p for="locationImage">Image Link:</p>
                        <input type="text" id="locationImage" name="txtLocationImage" required=""/>

                        <p for="locationName">Name:</p>
                        <input type="text" id="locationName" name="txtLocationName" required=""/>

                        <p for="locationDescription">Description:</p>
                        <input type="text" id="locationDescription" name="txtLocationDescription" required=""/>

                        <p for="locationPrice">Price:</p>
                        <input type="text" id="locationPrice" name="txtLocationPrice" required=""/>
                        <br/> 
                        <input type="submit" value="UpdateLocation" name="btAction" class="button button-update"/>
                    </form>
                </div>
            </div>

            <div id="popupProduct" class="popup">
                <div id="popupContent" class="popup-content">
                    <span class="close" onclick="closePopup()">&times;</span>
                    <h2>Update Product</h2>
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

            <div id="popupDeleteLocation" class="popup">
                <div id="popupContent" class="popup-content">
                    <span class="close" onclick="closePopup()">&times;</span>

                    <h2>Warning</h2>
                    <p class="warning-message">Are you sure!!!</p>
                    <div class="form-group delete-pop-up-actions">                       
                        <button class="button button-gap" onclick="closePopup()" >Cancel</button>
                        <form action="DispatcherServlet" method="POST">
                            <input type="hidden" id="locationDeleteId" name="txtDeleteId">
                            <input type="submit" value="DeleteLocation" name="btAction" class="button button-delete" />
                        </form>
                    </div>
                </div>
            </div>

            <div id="popupDeleteProduct" class="popup">
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

            <div id="popupDeleteAccount" class="popup">
                <div id="popupContent" class="popup-content">
                    <span class="close" onclick="closePopup()">&times;</span>

                    <h2>Warning</h2>
                    <p class="warning-message">Are you sure!!!</p>
                    <div class="form-group delete-pop-up-actions">                       
                        <button class="button button-gap" onclick="closePopup()" >Cancel</button>
                        <form action="DispatcherServlet" method="POST">
                            <input type="hidden" id="proId" name="txtId">
                            <input type="submit" value="Delete Account" name="btAction" class="button button-delete" />
                        </form>
                    </div>

                </div>
            </div>
            <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
            <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
            <script>
                            function openPopupLocation(image, id, name, description, price) {
                                document.getElementById('locationId').value = id;
                                document.getElementById('locationName').value = name;
                                document.getElementById('locationDescription').value = description;
                                document.getElementById('locationPrice').value = price;
                                document.getElementById('locationImage').value = image;

                                document.getElementById('popupLocation').style.display = 'block';
                            }

                            function openPopupProduct(image, id, name, description, price) {
                                document.getElementById('productId').value = id;
                                document.getElementById('productName').value = name;
                                document.getElementById('productDescription').value = description;
                                document.getElementById('productPrice').value = price;
                                document.getElementById('productImage').value = image;

                                document.getElementById('popupProduct').style.display = 'block';
                            }
                            function openPopupDeleteProduct(id) {
                                document.getElementById('rentalProductId').value = id;
                                document.getElementById('popupDeleteProduct').style.display = 'block';
                            }

                            function openPopupDeleteLocation(id) {
                                document.getElementById('locationDeleteId').value = id;
                                document.getElementById('popupDeleteLocation').style.display = 'block';
                            }

                            if (${requestScope.ERROR_USER_NAME != null}) {
                                swal("Invalid UserName");
                            }
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

                            function openPopupAccount(id, userName, firstName, lastName, email, phone, address, userId, roleName) {
                                document.getElementById('accountId').value = id;
                                document.getElementById('userName').value = userName;
                                document.getElementById('firstName').value = firstName;
                                document.getElementById('lastName').value = lastName;
                                document.getElementById('email').value = email;
                                document.getElementById('phoneNumber').value = phone;
                                document.getElementById('address').value = address;

                                console.log('accountId ', id)

                                if (roleName === 'user') {
                                    document.getElementById('userId').style.display = 'none';
                                    document.getElementById('isUser').value = 'true';
                                    document.getElementById('roleNameText').style.display = 'none';
                                } else {
                                    document.getElementById('userId').style.display = 'block';
                                    document.getElementById('roleNameText').style.display = 'block';
                                    document.getElementById('isUser').value = '';
                                    document.getElementById('userId').value = userId;
                                }


                                document.getElementById('popupAccount').style.display = 'block';
                            }

                            function openPopup(orderId, itemId) {
                                document.getElementById('txtOrderId').value = orderId;
                                document.getElementById('txtItemId').value = itemId;

                                document.getElementById('popup').style.display = 'block';
                            }

                            function openPopupDelete(orderId, orderDetailId, itemId, itemType) {
                                document.getElementById('orderId').value = orderId;
                                document.getElementById('orderDetailId').value = orderDetailId;
                                document.getElementById('iId').value = itemId;
                                document.getElementById('iType').value = itemType;

                                document.getElementById('popupDelete').style.display = 'block';
                            }

                            function openPopupDeleteAccount(profileId) {
                                document.getElementById('proId').value = profileId;
                                document.getElementById('popupDeleteAccount').style.display = 'block';
                            }

                            function closePopup() {
                                document.getElementById('popupAccount').style.display = 'none';
                                document.getElementById('popupUpdate').style.display = 'none';
                                document.getElementById('popupDelete').style.display = 'none';
                                document.getElementById('popupDeleteAccount').style.display = 'none';
                            }

                            function updateTotalPrice(selectElement) {
                                var locationValue = document.querySelector('select[name="location"]').value;
                                var studioValue = document.querySelector('select[name="studio"]').value;

                                var locationPrice = parseInt(document.querySelector('select[name="location"] option:checked').getAttribute("data-price"));
                                var studioPrice = parseInt(document.querySelector('select[name="studio"] option:checked').getAttribute("data-price"));

                                var totalPrice = locationPrice + studioPrice;
                                document.getElementById("totalPrice").value = totalPrice;
                            }

                            // Set default price on page load
//                            window.addEventListener("DOMContentLoaded", function () {
//                                var locationSelect = document.querySelector('select[name="location"]');
//                                var locationPrice = parseInt(locationSelect.options[locationSelect.selectedIndex].getAttribute("data-price"));
//
//                                var studioSelect = document.querySelector('select[name="studio"]');
//                                var studioPrice = parseInt(studioSelect.options[studioSelect.selectedIndex].getAttribute("data-price"));
//
//                                var totalPrice = locationPrice + studioPrice;
//                                document.getElementById("totalPrice").value = totalPrice;
//                            });
//
//                            // Set default value to current date and time
//                            window.addEventListener("DOMContentLoaded", function () {
//                                var timeRangeInput = document.getElementById("timeRangeInput");
//                                var currentDate = new Date();
//                                var currentDateString = currentDate.toISOString().slice(0, 16); // Format: "YYYY-MM-DDTHH:MM"
//
//                                timeRangeInput.min = currentDateString;
//                                timeRangeInput.value = currentDateString;
//                            });

                            document.addEventListener("DOMContentLoaded", function () {
                                var locationSelect = document.querySelector('select[name="location"]');
                                var locationPrice = 0;

                                if (locationSelect) {
                                    var selectedOption = locationSelect.options[locationSelect.selectedIndex];

                                    if (selectedOption) {
                                        locationPrice = parseInt(selectedOption.getAttribute("data-price")) || 0;
                                    }
                                }

                                var studioSelect = document.querySelector('select[name="studio"]');
                                var studioPrice = 0;

                                if (studioSelect) {
                                    var selectedStudioOption = studioSelect.options[studioSelect.selectedIndex];

                                    if (selectedStudioOption) {
                                        studioPrice = parseInt(selectedStudioOption.getAttribute("data-price")) || 0;
                                    }
                                }

                                var totalPrice = locationPrice + studioPrice;
                                var totalPriceElement = document.getElementById("totalPrice");

                                if (totalPriceElement) {
                                    totalPriceElement.value = totalPrice;
                                }

                                var timeRangeInput = document.getElementById("timeRangeInput");
                                if (timeRangeInput) {
                                    var currentDate = new Date();
                                    var currentDateString = currentDate.toISOString().slice(0, 16); // Format: "YYYY-MM-DDTHH:MM"

                                    timeRangeInput.min = currentDateString;
                                    timeRangeInput.value = currentDateString;
                                }
                            });


                            function openPopupUpdate(orderDetailID, itemId, itemType, orderId, orderStartDate, orderEndDate) {

                                document.getElementById('popupUpdate').style.display = 'block';
                                var typeArr = itemType.split('-');
                                var type = "";
                                if (typeArr.length > 1) {
                                    type = typeArr[1];
                                } else {
                                    type = itemType;
                                }

                                var items = document.getElementsByClassName("card");
                                for (var i = 0; i < items.length; i++) {
                                    var tmp = items[i].getAttribute('data-item-type');
                                    if (tmp !== type) {
                                        items[i].style.display = "none";
                                    } else {
                                        items[i].style.display = "flex"
                                    }


                                }

                                var detailInputs = document.getElementsByClassName("detail-input");
                                for (var i = 0; i < detailInputs.length; i++) {
                                    detailInputs[i].value = orderDetailID;
                                }

                                var itemIdInputs = document.getElementsByClassName("itemId-input");
                                for (var i = 0; i < itemIdInputs.length; i++) {
                                    itemIdInputs[i].value = itemId;
                                }

                                var itemTypeInputs = document.getElementsByClassName("itemType-input");
                                for (var i = 0; i < itemTypeInputs.length; i++) {
                                    itemTypeInputs[i].value = itemType;
                                }

                                var orderInputs = document.getElementsByClassName("orderId-input");
                                for (var i = 0; i < orderInputs.length; i++) {
                                    orderInputs[i].value = orderId;
                                }

                                var orderInputs = document.getElementsByClassName("timeRange-input");
                                for (var i = 0; i < orderInputs.length; i++) {
                                    orderInputs[i].value = orderStartDate;
                                }

                                var orderInputs = document.getElementsByClassName("timeRangeReturn-input");
                                for (var i = 0; i < orderInputs.length; i++) {
                                    orderInputs[i].value = orderEndDate;
                                }

                            }

                            if (${requestScope.BOOK_NOT_AVAILABLE != null}) {
                                swal("Opps!", "${requestScope.BOOK_NOT_AVAILABLE}", "warning");
                            }

            </script>
            <jsp:include page="footer.jsp"></jsp:include>
    </body>
</html>
