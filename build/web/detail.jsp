<%-- 
    Document   : detail
    Created on : Jun 20, 2023, 10:33:52 PM
    Author     : ptd
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Detail Page</title>
        <style>
            .container{
                margin: 60px 0;
                justify-content: center;
                align-content: center;
                display: flex;

            }

            .image-left{
                border: 1px solid gainsboro;
            }

            img {
                height: 500px;
            }
            .content-right{
                padding: 0 30px;
                border: 1px solid gainsboro;
                width: 400px;
            }

            .title{
                padding-bottom: 20px;
                font-size: 40px;
                font-weight: bold;
            }

            .description{
                font-size: 20px;
                padding-bottom: 20px;
            }

            .price{
                font-size: 20px;
                padding-bottom: 20px;
                color: red;
            }

            .button {
                display: inline-block;
                padding: 10px 20px;
                background-color: #4CAF50;
                color: #fff;
                font-size: 16px;
                text-decoration: none;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }

            .button:hover {
                background-color: #45a049;
            }

            .button:focus {
                outline: none;
            }

            .button:active {
                background-color: #3e8e41;
                transform: translateY(1px);
            }


        </style>
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
            <form action="DispatcherServlet" method="POST">
                <input type="hidden" name="itemId" value="${param.itemId}"/>
            <input type="hidden" name="itemType" value="${param.itemType}"/>
            <input type="hidden" name="name" value="${param.name}"/>
            <input type="hidden" name="description" value="${param.description}"/>
            <input type="hidden" name="price" value="${param.price}"/>
            <div class="container">
                <div class="image-left">
                    <img src="${param.image}" alt="img" />
                </div>

                <div class="content-right">
                    <div class="title">${param.name}</div>
                    <hr/>
                    <div class="description">Description: ${param.description}</div>
                    <div class="price">Price For Rent: $ ${param.price} </div>
                    <input type="submit" value="Add To Card" name="btAction" class="button" />
                </div>
            </div>

        </form>

        <jsp:include page="footer.jsp"></jsp:include>
    </body>
</html>
