
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>

        <style>
            .search-results {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 20px;
            }

            .search-results th,
            .search-results td {
                padding: 10px;
                border: 1px solid #ccc;
            }

            .search-results th {
                background-color: #f1f1f1;
            }

            .search-results tbody tr:nth-child(even) {
                background-color: #f9f9f9;
            }

            .card-container{
                display: flex;
                flex-wrap: wrap;
                width: 1500px;
                justify-content: center;
                align-items: center;
            }

            .card {
                display: flex;
                flex-direction: column;
                background-color: #f9f9f9;
                padding: 20px;
                text-align: center;
                border-radius: 5px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                width: 25%;
                margin: 30px;
            }

            .card:hover{
                border: 1px solid black;
            }


            .card-image {
                flex: 1;
            }

            .card-image img {
                width: 100%;
                height: 250px;
                object-fit: cover;
                border-radius: 5px;
            }

            .card-content {
                flex: 1;
            }

            .card-content a {
                text-decoration: none;
            }
            .card-content a:hover {
                text-decoration: none;
            }


            .card h3 {
                margin-top: 0;
                margin-bottom: 10px;
                color: #333;
            }

            .card p {
                margin: 10px;
                color: #333;
            }

            h1 {
                text-align: center;
            }

            a{
                color: #333;
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

            .button:hover {
                background-color: lightskyblue;
            }

            .button:focus {
                outline: none;
            }

            .button:active {
                background-color: #3e8e41;
                transform: translateY(1px);
            }

            .parent-container{
                display: flex;
            }

            .filter-container{
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
            }

            .btn-booking{
                margin-top: 15px;
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

            .pagination{
                justify-content: center;
                padding: 10px;
            }

            .pagination a {
                margin: 0 10px;
            }

            .pagination span {
                margin: 0 10px;
            }

            .truncate-text {
                display: -webkit-box;
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 1;
                overflow: hidden;
                text-overflow: ellipsis;
            }


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
        <jsp:include page="/header.jsp"></jsp:include>

            <div class="parent-container">
                <div class="filter-container">
                    <div class="dropdown show">
                        <a class="btn btn-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Category
                        </a>
                        <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                            <a class="dropdown-item" href="DispatcherServlet?btAction=CategoryFilter&category=all">All</a>
                            <a class="dropdown-item" href="DispatcherServlet?btAction=CategoryFilter&category=location">Locations</a>
                            <a class="dropdown-item" href="DispatcherServlet?btAction=CategoryFilter&category=studio">Studios</a>
                            <a class="dropdown-item" href="DispatcherServlet?btAction=CategoryFilter&category=product">Products</a>
                            <a class="dropdown-item" href="DispatcherServlet?btAction=CategoryFilter&category=combo">Combos</a>
                        </div>
                    <c:if test="${sessionScope.USER != null}">
                        <button class="btn btn-secondary btn-booking" onclick="openPopup()" >Booking Photography Schedule</button>
                    </c:if>
                </div>

            </div>

            <div class="card-container">

                <c:forEach items="${sessionScope.LIST_ORDER_PAGING}" var="data">
                    <c:url var="detail" value="detail.jsp">
                        <c:param name="itemId" value="${data.itemId}" />
                        <c:param name="itemType" value="${data.itemType}" />
                        <c:param name="name" value="${data.name}" />
                        <c:param name="description" value="${data.description}" />
                        <c:param name="price" value="${data.price}" />
                        <c:param name="image" value="${data.image}" />
                    </c:url>
                    <a href="${detail}" >
                        <div class="card">
                            <div class="card-image">
                                <img src="${data.image}" alt="Image">
                            </div>
                            <div class="card-content">
                                <h3>${data.name}</h3>
                                <p class="truncate-text">${data.description}</p>
                                <p>Price For Rent: $ ${data.price}</p>
                                <div class="d-flex justify-content-center">
                                    <p><b>${data.itemType}</b></p>
                                    <p><a href="ownerContact.jsp" class="contact-link">Contact</a></p>
                                </div>
                                <input type="submit" value="Add To Cart" name="btAction" class="button" onclick="openPopupDate('${data.itemId}', '${data.itemType}', '${data.name}', '${data.description}', '${data.price}')"/>

                            </div>
                        </div>
                    </a>
                </c:forEach>
            </div>  
        </div>
        <!-- date selection pop-up-->
        <div id="datePopup" class="popup">
            <div class="popup-content">
                <span class="close" onclick="closePopup()">&times;</span>
                <h2>Select Dates</h2>
                <form action="DispatcherServlet" method="POST">
                    <input type="hidden" id="itemId" name="itemId" value="">
                    <input type="hidden" id="itemType" name="itemType" value="">
                    <input type="hidden" id="name" name="name" value="">
                    <input type="hidden" id="description" name="description" value="">
                    <input type="hidden" id="price" name="price" value="">
                    <label for="startDate">Start Date:</label>
                    <input type="datetime-local" name="timeRange" required id="timeRangeInput">
                    <br>
                    <label for="endDate">End Date:</label>
                    <input type="datetime-local" name="timeRangeReturn" required id="timeRangeReturnInput">
                    <br>
                    <input type="submit" value="Add To Cart" name="btAction">
                </form>
            </div>
        </div>

        <!-- Paging navigation -->
        <c:if test="${requestScope.totalPages > 1}">
            <div class="pagination">
                <c:choose>
                    <c:when test="${requestScope.currentPage > 1}">
                        <a href="?page=${requestScope.currentPage - 1}">Previous</a>
                    </c:when>
                    <c:otherwise>
                        <span class="disabled">Previous</span>
                    </c:otherwise>
                </c:choose>

                <c:forEach begin="1" end="${requestScope.totalPages}" var="page">
                    <c:choose>
                        <c:when test="${requestScope.currentPage eq page}">
                            <span class="current">${page}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="?page=${page}">${page}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:choose>
                    <c:when test="${requestScope.currentPage < requestScope.totalPages}">
                        <a href="?page=${requestScope.currentPage + 1}">Next</a>
                    </c:when>
                    <c:otherwise>
                        <span class="disabled">Next</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
        <!-- pop-up booking photo schedule -->
        <div id="popup" class="popup">
            <c:set var="totalPrice" value="0" />
            <div id="popupContent" class="popup-content">
                <span class="close" onclick="closePopup()">&times;</span>
                <h2>Booking Photography Schedule</h2>
                <form action="DispatcherServlet" method="POST">
                    <label for="studioName">Select Studio:</label>
                    <select name="studio" class="form-select" aria-label="Default select example" onchange="updateTotalPrice(this)" required="true" id="studioSelect">
                        <c:forEach items="${sessionScope.LIST_STUDIO}" var="s">
                            <option value="${s.id}" data-price="${s.price}">${s.name}</option>
                        </c:forEach>
                    </select>
                    <br/> 
                    <label for="locationImage">Select location:</label>
                    <select name="location" class="form-select" aria-label="Default select example" onchange="updateTotalPrice(this)" required="true" id="locationSelect">
                        <c:forEach items="${sessionScope.LIST_LOCATION}" var="l">
                            <option value="${l.id}" data-price="${l.price}">${l.name}</option>
                        </c:forEach>
                    </select>
                    <br/> 
                    <label for="locationDescription">Select Time booking</label>
                    <input type="datetime-local" name="timeRange" class="ml-5" required="true" id="timeRangeBooking">
                    <br/> 
                    <label for="locationDescription">Select Time return</label>
                    <input type="datetime-local" name="timeRangeReturn" class="ml-5" required="true" id="timeRangeReturnBooking">
                    <br/> 
                    <br/> 
                    <label for="locationPrice">Price:</label>
                    <input type="number" name="price" class="ml-5" id="totalPrice" readonly="true"/>

                    <br />
                    <input type="submit" value="Book Schedule" name="btAction" class="button button-update mt-3" id="buttonSchedule"/>
                </form>
            </div>
        </div>
        <jsp:include page="/footer.jsp"></jsp:include>

            <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
            <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
            <script>
                        function openPopupDate(itemId, itemType, name, description, price) {
                            document.getElementById("datePopup").style.display = "block";

                            // Populate hidden fields in the dateForm with item details
                            document.getElementById("itemId").value = itemId;
                            document.getElementById("itemType").value = itemType;
                            document.getElementById("name").value = name;
                            document.getElementById("description").value = description;
                            document.getElementById("price").value = price;
                        }
                        $(document).ready(function () {
                            $('.dropdown-toggle').click(function () {
                                $(this).next('.dropdown-content').toggle();
                            });
                        });

                        function openPopup() {
                            document.getElementById('popup').style.display = 'block';
                        }

                        function closePopup() {
                            document.getElementById('popup').style.display = 'none';
                            document.getElementById('datePopup').style.display = 'none';
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
                        window.addEventListener("DOMContentLoaded", function () {
                            var locationSelect = document.querySelector('select[name="location"]');
                            var locationPrice = parseInt(locationSelect.options[locationSelect.selectedIndex].getAttribute("data-price"));

                            var studioSelect = document.querySelector('select[name="studio"]');
                            var studioPrice = parseInt(studioSelect.options[studioSelect.selectedIndex].getAttribute("data-price"));

                            var totalPrice = locationPrice + studioPrice;
                            document.getElementById("totalPrice").value = totalPrice;
                        });

                        // Set default value to current date and time
                        window.addEventListener("DOMContentLoaded", function () {
                            var timeRangeInput = document.getElementById("timeRangeInput");
                            var timeRangeReturnInput = document.getElementById("timeRangeReturnInput");

                            var timeRangeBooking = document.getElementById("timeRangeBooking");
                            var timeRangeReturnBooking = document.getElementById("timeRangeReturnBooking");

                            var currentDate = new Date();
                            var nextDate = new Date(currentDate);
                            nextDate.setDate(currentDate.getDate() + 1);
                            var currentDateString = currentDate.toISOString().slice(0, 16); // Format: "YYYY-MM-DDTHH:MM"
                            var nextDateString = nextDate.toISOString().slice(0, 16);

                            timeRangeInput.min = currentDateString;
                            timeRangeInput.value = currentDateString;

                            timeRangeBooking.min = currentDateString;
                            timeRangeBooking.value = currentDateString;

                            timeRangeReturnInput.min = nextDateString;
                            timeRangeReturnInput.value = nextDateString;

                            timeRangeReturnBooking.min = nextDateString;
                            timeRangeReturnBooking.value = nextDateString;
                        });
                        if (${requestScope.BOOK_NOT_AVAILABLE != null}) {
                            swal("Opps!", '${requestScope.BOOK_NOT_AVAILABLE}', "warning");
                        }

                        if (${requestScope.PAYMENT_SUCCESS != null && requestScope.LIST_ITEM_ERROR != null}) {
                            swal("Congratulation!", '${requestScope.LIST_ITEM_ERROR}', "success");
                        } else if (${requestScope.PAYMENT_SUCCESS != null}) {
                            swal("Congratulation!", '${requestScope.PAYMENT_SUCCESS}', "success");
                        }

                        document.addEventListener("DOMContentLoaded", function () {
                            const studioSelect = document.getElementById("studioSelect");
                            const locationSelect = document.getElementById("locationSelect");
                            const timeRangeInput = document.getElementById("timeRangeBooking");
                            const timeRangeReturn = document.getElementById("timeRangeReturnBooking");
                            const button = document.getElementById("buttonSchedule");

                            // Disable the location and date inputs initially
                            locationSelect.disabled = true;
                            timeRangeInput.disabled = true;
                            timeRangeReturn.disabled = true;
                            button.style.display = 'none';

                            // Add an event listener to the studio select element
                            studioSelect.addEventListener("change", function () {
                                // Disable the location and date inputs when the studio selection changes
                                locationSelect.disabled = true;
                                timeRangeInput.disabled = true;
                                timeRangeReturn.disabled = true;


                                // Enable the location select when a studio is selected
                                if (studioSelect.value !== "") {
                                    locationSelect.disabled = false;
                                }
                            });

                            // Add an event listener to the location select element
                            locationSelect.addEventListener("change", function () {
                                // Enable the date input when a location is selected
                                timeRangeInput.disabled = false;
                                timeRangeReturn.disabled = false;
                                button.style.display = 'block';
                            });
                        });


        </script>


    </body>
</html>
