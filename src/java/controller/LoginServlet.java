package controller;

import dao.AccountDAO;
import dao.DressPhotoComboDAO;
import dao.FeedbackDAO;
import dao.LocationDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.PhotoScheduleDAO;
import dao.PhotographyStudiosDAO;
import dao.RejectOrderDAO;
import dao.RentalProductDAO;
import dao.RoleDAO;
import dao.studioStaffDAO;
import dto.DressPhotoCombo;
import dto.Feedback;
import dto.Location;
import dto.Order;
import dto.OrderDetail;
import dto.PhotoSchedule;
import dto.OrderItem;
import dto.PhotographyStudio;
import dto.Profile;
import dto.RejectOrder;
import dto.RentalProduct;
import dto.Role;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.Utilities;

/**
 *
 * @author ptd
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    public final String HOME_PAGE = "DispatcherServlet?btAction=Home";
    public final String ADMIN_PAGE = "admin.jsp";
    public final String PHOTO_PAGE = "photoHome.jsp";
    public final String RENTAL_STAFF_PAGE = "rentalPage.jsp";
    public final String ERROR_PAGE = "error.jsp";

    public final String ADMIN_ROLE = "admin";
    public final String USER_ROLE = "user";
    public final String STAFF_ROLE = "staff";
    public final String RENTAL_STAFF_ROLE = "rental_staff";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = "login.jsp";
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        AccountDAO dao = new AccountDAO();
        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        PhotoScheduleDAO scheduleDAO = new PhotoScheduleDAO();
        LocationDAO locationDAO = new LocationDAO();
        PhotographyStudiosDAO studioDAO = new PhotographyStudiosDAO();
        RoleDAO roleDAO = new RoleDAO();
//        studioStaffDAO studioStaffDAO = new studioStaffDAO();
        RentalProductDAO rentalProductDAO = new RentalProductDAO();
        DressPhotoComboDAO comboDAO = new DressPhotoComboDAO();
        FeedbackDAO feedbackDAO = new FeedbackDAO();

        HttpSession session = request.getSession();

        try {
            if (Utilities.areValidStrings(20, userName)) {
                Profile result = dao.checkLogin(userName, password);
                if (result != null) {
                    if (ADMIN_ROLE.equals(result.getRoleName())) {
                        url = ADMIN_PAGE;

                        //get list staff role
                        List<Role> listRoleStaff = roleDAO.getStaffRole();

                        // manage user
                        List<Profile> listUser = dao.getAllProfile();

                        List<Profile> users = new ArrayList<>();
                        List<Profile> staff = new ArrayList<>();

                        for (Profile user : listUser) {
                            if (user.getRoleName().equals("user")) {
                                users.add(user);
                            } else {
                                staff.add(user);
                            }
                        }
                        session.setAttribute("LIST_USER", users);
                        session.setAttribute("LIST_STAFF", staff);

                        session.setAttribute("LIST_STAFF_ROLE", listRoleStaff);

                        // manage order
                        List<Order> listOrder = orderDAO.getAllOrder();

                        if (listOrder.size() > 0) {
                            List<OrderDetail> listProduct = new ArrayList<>();
                            Map<String, List<OrderDetail>> listSchedule = new HashMap<>();

                            for (Order order : listOrder) {
                                List<OrderDetail> listOrderDetail = orderDetailDAO.getOrderDetailByOrderIdAdmin(order.getOrderId());
                                int photoTmp = 0;
                                List<PhotoSchedule> photoList = new ArrayList<>();
                                for (OrderDetail orderDetail : listOrderDetail) {
                                    String arr[] = orderDetail.getItemType().split("-");
                                    if (arr.length > 1) {
                                        if (photoTmp != orderDetail.getItemId()) {
                                            PhotoSchedule photo = scheduleDAO.getPhotoScheduleByIdAdmin(orderDetail.getItemId());
                                            if (photo != null) {
                                                photoTmp = photo.getScheduleId();
                                                photoList.add(photo);
                                            }
                                        }
                                    }
                                }
                                Utilities.groupOrderDetailsAdminLoaded(listOrderDetail, listSchedule, photoList);
                                for (OrderDetail detail : listOrderDetail) {
                                    //item_id and item_type --> add schedule photo
                                    if (!detail.getItemType().equals("photo_schedule-location") && !detail.getItemType().equals("photo_schedule-studio")) {
                                        if (detail.getItemType().equals("confirm")) {
                                            detail.setStatus("confirm");
                                        } else {
                                            detail.setStatus(order.getStatus());
                                        }
                                        listProduct.add(detail);
                                    }
                                }

                            }
                            List<Location> listLocation = locationDAO.getAllLocation();
                            session.setAttribute("LOCATIONS", listLocation);
                            // get all rental to manage
                            List<RentalProduct> listProductManage = rentalProductDAO.getAllRentalProduct();
                            session.setAttribute("PRODUCTS", listProductManage);

                            session.setAttribute("LIST_CART_PRODUCT_ADMIN", listProduct);
                            session.setAttribute("LIST_CART_SCHEDULE_ADMIN", listSchedule);
                            url = ADMIN_PAGE;
                        }

                        List<Feedback> listFeedback = feedbackDAO.getAllFeedback();
                        session.setAttribute("LIST_ADMIN_FFEDBACK", listFeedback);

                        List<Location> listLocation = locationDAO.getAllLocation();
                        session.setAttribute("LIST_LOCATION", listLocation);

                        List<PhotographyStudio> listStudio = studioDAO.getAllPhotographyStudio();
                        session.setAttribute("LIST_STUDIO", listStudio);

                        List<RentalProduct> rentalProducts = rentalProductDAO.getAllRentalProduct();
                        session.setAttribute("LIST_PRODUCT", rentalProducts);

                        List<DressPhotoCombo> combos = comboDAO.getAllDressPhotoCombo();
                        session.setAttribute("LIST_COMBO", combos);
                    } else if (STAFF_ROLE.equals(result.getRoleName())) {
                        url = PHOTO_PAGE;

                        // flow: get order detail with type is rental_schedule and order is pending
                        List<OrderDetail> listSchedulePending = orderDetailDAO.getOrderDetailByItemTypeStaff("photo_schedule-location", "photo_schedule-studio");

                        Map<String, List<OrderDetail>> listSchedule = Utilities.groupOrderDetails(listSchedulePending);

                        session.setAttribute("LIST_CART_SCHEDULE_ADMIN", listSchedule);

                        List<Location> listLocation = locationDAO.getAllLocation();
                        session.setAttribute("LOCATIONS", listLocation);

                    } else if (USER_ROLE.equals(result.getRoleName())) {
                        url = HOME_PAGE;
                        // get orderbyProfileId
                        Order orderExist = orderDAO.getOrderByProfileId(result.getProfileId());
                        int orderId = 0;
                        if (orderExist != null) {
                            orderId = orderExist.getOrderId();

                            if (orderId > 0) {
                                List<OrderDetail> listOrderDetailByOrder = orderDetailDAO.getOrderDetailByOrderId(orderId);

                                List<OrderDetail> listProduct = new ArrayList<>();

                                Map<String, List<OrderDetail>> listSchedule = Utilities.groupOrderDetails(listOrderDetailByOrder);

                                for (OrderDetail detail : listOrderDetailByOrder) {
                                    //item_id and item_type --> add schedule photo
                                    if (!detail.getItemType().equals("photo_schedule-location") && !detail.getItemType().equals("photo_schedule-studio")) {
                                        listProduct.add(detail);
                                    }
                                }

                                url = HOME_PAGE;
                                session.setAttribute("LIST_CART_PRODUCT", listProduct);
                                session.setAttribute("LIST_CART_SCHEDULE", listSchedule);
                                session.setAttribute("CART_ITEM", (listSchedule.size() + listProduct.size()));
                            }
                        }

                        RejectOrderDAO rejectDAO = new RejectOrderDAO();
                        List<RejectOrder> listReject = rejectDAO.getListRejectByProfileId(result.getProfileId());
                        List<RejectOrder> listProduct = new ArrayList<>();

                        Map<String, List<RejectOrder>> listSchedule = Utilities.groupOrderReject(listReject);

                        for (RejectOrder detail : listReject) {
                            //item_id and item_type --> add schedule photo
                            if (!detail.getItemType().equals("photo_schedule-location") && !detail.getItemType().equals("photo_schedule-studio")) {
                                listProduct.add(detail);
                            }
                        }
                        session.setAttribute("LIST_REJECT_SCHEDULE", listSchedule);
                        session.setAttribute("LIST_REJECT_PRODUCT", listProduct);

                        List<Location> listLocation = locationDAO.getAllLocation();
                        session.setAttribute("LIST_LOCATION", listLocation);

                        List<PhotographyStudio> listStudio = studioDAO.getAllPhotographyStudio();
                        session.setAttribute("LIST_STUDIO", listStudio);

                        List<RentalProduct> rentalProducts = rentalProductDAO.getAllRentalProduct();
                        session.setAttribute("LIST_PRODUCT", rentalProducts);

                        List<DressPhotoCombo> combos = comboDAO.getAllDressPhotoCombo();
                        session.setAttribute("LIST_COMBO", combos);

                        List<Feedback> listFeedback = feedbackDAO.getLastFiveFeedback();
                        session.setAttribute("LIST_EXIST_FEEDBACK", listFeedback);

                    } else if (RENTAL_STAFF_ROLE.equals(result.getRoleName())) {
                        url = RENTAL_STAFF_PAGE;
                        // get orderdetail with itemtype = rental product
                        List<OrderDetail> listOrderRental = orderDetailDAO.getOrderDetailByItemType("rental_product");
                        session.setAttribute("LIST_CART_PRODUCT_ADMIN", listOrderRental);

                        // get all rental to manage
                        List<RentalProduct> listProduct = rentalProductDAO.getAllRentalProduct();
                        session.setAttribute("PRODUCTS", listProduct);
                    }
                    session.setAttribute("USER", result);
                }
            } else {
                request.setAttribute("INVALID_FIELD", "Pls double-check, the string should be character and digits, max length is 20");
            }
        } catch (NamingException ex) {
            log("LoginServlet_NamingException: " + ex.getMessage());
        } catch (SQLException ex) {
            log("LoginServlet_SQLException " + ex.getMessage());
        } finally {
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
