<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Product Management</title>
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
        <c:if test="${sessionScope.USER.roleName ne 'staff'}">
            <jsp:forward page="login.jsp"></jsp:forward>
        </c:if>
        <jsp:include page="header.jsp"></jsp:include>
        <c:set var="profile" value="${sessionScope.USER}" />
        <div class="container-parent">
            <div class="tab">
                <button class="tablinks active" onclick="openTab(event, 'Tab1')">Location</button>
                <button class="tablinks" onclick="openTab(event, 'Tab2')">Schedule Confirm</button>
                <button class="tablinks" onclick="openTab(event, 'Tab3')">Add Location</button>
            </div>


            <div id="Tab1" class="tabcontent" style="display: block;">
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
                                <button class="button" onclick="openPopup('${location.image}', ${location.id}, '${location.name}', '${location.description}', ${location.price})">Update Location</button>                     
                                <button class="button button-delete" onclick="openPopupDelete('${location.id}')">Delete Location</button>                                                                 
                            </td>
                        </tr>
                    </c:forEach>


                </table>
            </div>
            <div id="Tab2" class="tabcontent" >
                <h3>Booking Schedule Confirmation</h3>
                <table>
                    <tr>         
                        <th>Order</th>
                        <th>Name</th>
                        <th>Description</th>                      
                        <th>Photo Date</th>  
                        <th>Price</th>       
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
                                <td>${item.price}</td>
                                <td style="border-bottom: none; text-align: center"> 
                                    <button class="button" onclick="openPopupUpdate('${item.orderDetailId}', '${item.itemId}', '${item.itemType}', '${item.orderId}')">Change</button>
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>

                                <c:if test="${count.index == 1}">
                                    <td style="border-bottom: none; text-align: center; display: flex; justify-content: center">

                                        <form action="DispatcherServlet" method="GET">
                                            <input type="hidden" name="orderId" value="${item.orderId}" />
                                            <input type="hidden" name="itemId" value="${item.itemId}" />
                                            <input type="submit" name="btAction" value="Confirm Schedule" class="button btn-secondary" />
                                        </form>                                               

                                        <button class="ml-3 button button-delete btn-delete-all-item" onclick="popupDeleteSchedule('${item.orderId}', '${item.orderDetailId}', '${item.itemId}', '${item.itemType}')">Delete Item</button>  
                                    </td>   
                                </c:if>   
                            </tr>

                        </c:forEach>
                        <br/>
                        <tr style="border-bottom: 1px solid black"></tr>
                    </c:forEach >

                </table>
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

            <div id="popup" class="popup">
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

            <!-- pop-up item -->
            <div id="popupUpdate" class="popup">
                <div id="popupContent" class="popup-content" style="max-width: 900px">
                    <span class="close" onclick="closePopup()">&times;</span>

                    <h2>Update Item</h2>

                    <div class="card-container" >
                        <c:forEach items="${sessionScope.LIST_ORDER_ALL}" var="data">  
                            <div class="card" data-item-type="${data.itemType}" style="display: flex; flex-direction: row; margin: 20 0;">
                                <div class="card-image">
                                    <img src="${data.image}" alt="Image" style="height: 200px; width: 300px">
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
                        <form action="DispatcherServlet" method="POST">
                            <input type="hidden" id="locationDeleteId" name="txtDeleteId">
                            <input type="submit" value="DeleteLocation" name="btAction" class="button button-delete" />
                        </form>
                    </div>
                </div>
            </div>

            <div id="popupDeleteSchedule" class="popup">
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
                document.getElementById('locationId').value = id;
                document.getElementById('locationName').value = name;
                document.getElementById('locationDescription').value = description;
                document.getElementById('locationPrice').value = price;
                document.getElementById('locationImage').value = image;

                document.getElementById('popup').style.display = 'block';
            }

            function openPopupSchedule(id, name, description, price, orderDate) {
                document.getElementById('scheduleId').value = id;
                document.getElementById('scheduleName').value = name;
                document.getElementById('scheduleDescription').value = description;
                document.getElementById('schedulePrice').value = price;
                document.getElementById('scheduleOrderDate').value = orderDate;

                document.getElementById('popup').style.display = 'block';
            }

            function openPopupDelete(id) {
                document.getElementById('locationDeleteId').value = id;
                document.getElementById('popupDelete').style.display = 'block';
            }

            function closePopup() {
                document.getElementById('popup').style.display = 'none';
                document.getElementById('popupUpdate').style.display = 'none';
                document.getElementById('popupDelete').style.display = 'none';
                document.getElementById('popupDeleteSchedule').style.display = 'none';
            }

            function popupDeleteSchedule(orderId, orderDetailId, itemId, itemType) {
                document.getElementById('orderId').value = orderId;
                document.getElementById('orderDetailId').value = orderDetailId;
                document.getElementById('iId').value = itemId;
                document.getElementById('iType').value = itemType;

                document.getElementById('popupDeleteSchedule').style.display = 'block';
            }

            function openPopupUpdate(orderDetailID, itemId, itemType, orderId) {
                console.log(orderId)

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

            }
        </script>
    </body>

</html>
