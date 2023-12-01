<%-- 
    Document   : feedback
    Created on : Jun 10, 2023, 11:25:12 AM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đánh giá và phản hồi</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.0.1/css/bootstrap.min.css">
        <style>
            body {
                background-color: #f8f9fa;
            }

            .container {
                max-width: 500px;
                margin: 0 auto;
                padding: 20px;
                background-color: #ffffff;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                border-radius: 5px;
                margin-top: 50px;
            }

            h1 {
                text-align: center;
                margin-bottom: 30px;
                color: #007bff;
            }

            .rating {
                direction: rtl;
                unicode-bidi: bidi-override;
                text-align: center;
                margin-bottom: 20px;
            }

            .rating input {
                display: none;
            }

            .rating label {
                display: inline-block;
                padding: 10px;
                font-size: 24px;
                color: #dcdcdc;
                cursor: pointer;
            }

            .rating label:before {
                content: '\2605';
            }

            .rating input:checked ~ label,
            .rating input:checked ~ label ~ label {
                color: #ffc107;
            }

            .mb-3 {
                margin-bottom: 20px;
            }

            .form-label {
                font-weight: bold;
                color: #007bff;
            }

            .btn-primary {
                background-color: #007bff;
                border-color: #007bff;
            }

            .btn-primary:hover {
                background-color: #0069d9;
                border-color: #0062cc;
            }

            .comments {
                margin-top: 20px;
            }

            .comment {
                align-items: flex-start;
                margin-bottom: 10px;
            }

            .comment-avatar {
                width: 40px;
                height: 40px;
                border-radius: 50%;
                overflow: hidden;
                margin-right: 10px;
            }

            .comment-avatar img {
                width: 100%;
                height: 100%;
                object-fit: cover;
            }

            .comment-content {
                flex-grow: 1;
                border: 1px solid #ccc;
                padding: 10px;
                border-radius: 5px;
                display: flex;
            }

            .comment-name {
                font-weight: bold;
                margin-bottom: 5px;
            }

            .comment-rating {
                margin-bottom: 5px;
            }

            .comment-text {
                margin-bottom: 0;
            }

            .comment-reply {
                display: flex;
                margin-top: 10px;
                padding-left: 70px;
                justify-content: end;
            }

            .comment-reply-content {
                background-color: #f5f5f5;
                padding: 15px;
                border-radius: 15px;
                width: 380px;
            }

            .comment-reply-content .comment-name {
                font-weight: bold;
                margin-bottom: 5px;
            }

            .reply-text {
                margin: 0;
            }

        </style>
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
        <div class="container" style="margin-bottom: 50px; width: 600px">
            <h1>Đánh giá và phản hồi</h1>

            <form action="feedback" method="POST" id="feedback-form">

                <div class="mb-3">
                    <label for="rating" class="form-label">Đánh giá:</label>
                    <div class="rating">
                        <input type="radio" id="star5" name="rating" value="5" /><label for="star5"></label>
                        <input type="radio" id="star4" name="rating" value="4" /><label for="star4"></label>
                        <input type="radio" id="star3" name="rating" value="3" /><label for="star3"></label>
                        <input type="radio" id="star2" name="rating" value="2" /><label for="star2"></label>
                        <input type="radio" id="star1" name="rating" value="1" /><label for="star1"></label>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="feedback" class="form-label">Feedback</label>
                    <textarea class="form-control" id="feedback" rows="5" name="content"></textarea>
                </div>
                <button type="submit" class="btn btn-primary" id="submit-btn">Send</button>
            </form>

            <div class="comments">
                <h2>Các đánh giá khác:</h2>
                <c:forEach var="feedback" items="${sessionScope.LIST_EXIST_FEEDBACK}">
                    <div class="comment">

                        <div class="comment-content">
                            <div class="comment-avatar">
                                <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTA9jyeynlFwVGBRreQHauSuqrkhoKGk7ytIw8OpgZbNA&s" alt="Avatar">
                            </div>
                            <div>
                                <div class="comment-name">${feedback.accountName}</div>
                                <div class="comment-rating">
                                    <span class="badge bg-primary">${feedback.rating} sao</span>
                                </div>
                                <p class="comment-text">${feedback.content}</p>
                            </div>
                        </div>
                        <!-- Show reply comment if it exists -->
                       
                        <c:if test="${feedback.replyComment != null}">
                            <div class="comment-reply">
                                <div class="comment-reply-content">
                                    <div class="comment-name">Admin</div>
                                    <p class="reply-text">${feedback.replyComment}</p>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </c:forEach>

            </div>
        </div>               
        <jsp:include page="footer.jsp"></jsp:include>



        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.0.1/js/bootstrap.bundle.min.js"></script>
        <script>
            document.getElementById("submit-btn").addEventListener("click", function (event) {
                event.preventDefault(); // Prevent the form from submitting immediately

                var selectedRating = document.querySelector('input[name="rating"]:checked');
                if (selectedRating) {
                    var ratingValue = selectedRating.value;
                    // You can perform further actions with the rating value, such as sending it to the servlet via AJAX or modifying the form data before submission.
                    console.log("Selected Rating:", ratingValue);
                    document.getElementById("feedback-form").submit(); // Submit the form after capturing the rating value
                } else {
                    // Handle the case where no rating is selected
                    console.log("Please select a rating.");
                }
            });
        </script>
    </body>
</html>

