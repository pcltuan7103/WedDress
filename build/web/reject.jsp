<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>reject Page</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
            }

            h1 {
                text-align: center;
            }

            .container-parent {
                max-width: 1400px;
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

        </style>
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
            <div class="container-parent">
            <c:if test="${sessionScope.LIST_REJECT_SCHEDULE.size() > 0 || sessionScope.LIST_REJECT_PRODUCT.size() > 0}">
                <h2>Reject Item</h2>
                <table>
                    <tr>                                       
                        <th>Name</th>
                        <th>Description</th>                      
                        <th>type</th>                      
                        <th>Photo Date</th>  
                        <th>Price</th>   
                        <th>Date Rent From</th>  
                        <th>Date Rent To</th>  
                        <th style="text-align: center">Actions</th>    
                    </tr>
                    <tbody>
                        <c:forEach var="entry" items="${sessionScope.LIST_REJECT_SCHEDULE}" varStatus="count">
                            <c:set var="key" value="${entry.key}" />
                            <c:set var="value" value="${entry.value}" />
                            <c:forEach var="item" items="${value}" varStatus="count">
                                <!-- Display the list item -->
                            <form method="POST" action="DispatcherServlet">
                                <input type="hidden" name="rejectId" value="${item.rejectId}"/>
                                <input type="hidden" name="itemId" value="${item.itemId}"/>
                                <tr>
                                    <td>${item.name}</td>
                                    <td style="width: 400px">${item.description}</td>
                                    <td >${item.itemType}</td>
                                    <td>${item.price}</td>
                                    <td style="width: 150px">${item.orderDate}</td>
                                    <td style="width: 150px">${item.orderStartDate}</td>
                                    <td style="width: 150px">${item.orderEndDate}</td>
                                    <!--                                    <td style="border-bottom: none; text-align: center"> 
                                                                            <input type="submit" class="button" name="btAction" value="Reject Order"/>
                                                                        </td>-->
                                </tr>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>   
                                    <c:if test="${count.index == 1}">
                                        <td style="border-bottom: none; text-align: center">
                                            <input type="submit" class="button" name="btAction" value="Reject Order"/>
                                        </td>   
                                    </c:if>   
                                </tr>
                            </form>
                        </c:forEach>
                    </c:forEach>
                    <c:forEach var="item" items="${sessionScope.LIST_REJECT_PRODUCT}" varStatus="count">
                        <!-- Display the list item -->
                        <form method="POST" action="DispatcherServlet">
                            <input type="hidden" name="rejectId" value="${item.rejectId}"/>
                            <input type="hidden" name="itemId" value="${item.itemId}"/>
                            <tr>
                                <td>${item.name}</td>
                                <td style="width: 400px">${item.description}</td>
                                <td >${item.itemType}</td>
                                <td>${item.price}</td>
                                <td style="width: 150px">${item.orderDate}</td>
                                <td style="width: 150px">${item.orderStartDate}</td>
                                <td style="width: 150px">${item.orderEndDate}</td>
                                <td style="border-bottom: none; text-align: center"> 
                                    <input type="submit" class="button" name="btAction" value="Reject Order"/>
                                </td>
                            </tr>
                        </form>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </body>
    <jsp:include page="footer.jsp"></jsp:include>
</html>
