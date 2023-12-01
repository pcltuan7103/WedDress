<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Transaction Details</title>
        <style>
            table {
                border-collapse: collapse;
                width: 100%;
            }

            th, td {
                border: 1px solid black;
                padding: 8px;
                text-align: left;
            }

            th {
                background-color: #f2f2f2;
            }
        </style>
    </head>
    <body>
        <h1>Transaction Details</h1>
        <form action="CompletePay" method="POST">
            <input type="hidden" name="amount" value="${requestScope.transaction.amount.total}"/>
            <input type="hidden" name="sku" value="${requestScope.transaction.itemList.items.get(0).sku}"/>

            <p><strong>Description:</strong> ${requestScope.transaction.description}</p>
            <p><strong>Amount:</strong> ${requestScope.transaction.amount.total} ${requestScope.transaction.amount.currency}</p>

            <h3>Items:</h3>
            <table>
                <tr>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Quantity</th>
                </tr>
                <c:forEach var="item" items="${requestScope.transaction.itemList.items}">
                    <tr>
                        <td>${item.name}</td>
                        <td>${item.price} ${item.currency}</td>
                        <td>${item.quantity}</td>
                    </tr>
                </c:forEach>
            </table>
            <hr>

            <input type="submit" value="Pay Now" class="btn btn-primary"/>
        </form>
    </body>
</html>
