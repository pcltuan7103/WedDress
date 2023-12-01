/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.LocationDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.PhotoScheduleDAO;
import dao.PhotographyStudiosDAO;
import dao.studioStaffDAO;
import dto.Location;
import dto.Order;
import dto.OrderDetail;
import dto.OrderItem;
import dto.PhotoSchedule;
import dto.PhotographyStudio;
import dto.Profile;
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
@WebServlet(name = "ConfirmScheduleServlet", urlPatterns = {"/ConfirmScheduleServlet"})
public class ConfirmScheduleServlet extends HttpServlet {

    public final String ERROR_PAGE = "error.jsp";
    public final String PHOTO_HOME_PAGE = "photoHome.jsp";
    public final String ADMIN_PAGE = "admin.jsp";

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

        String orderId = request.getParameter("orderId");
        String itemId = request.getParameter("itemId");

        String url = "admin.jsx";

        LocationDAO locationDAO = new LocationDAO();
        HttpSession session = request.getSession();
        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        PhotoScheduleDAO scheduleDAO = new PhotoScheduleDAO();
        Profile profile = (Profile) session.getAttribute("USER");

        try {
            if (!orderId.isEmpty() && session != null) {
                boolean isUpdated = false;

                boolean confirmSchedule = scheduleDAO.confirmScheduleById(Integer.parseInt(itemId));
                if (confirmSchedule) {
                    isUpdated = true;
                }

                if (isUpdated) {
                    // get otderDetail to look any order detail left in database then set status of order is confirm
//                    List<OrderDetail> listOrderDetail = orderDetailDAO.getOrderDetailByOrderId(Integer.parseInt(orderId));
//                    if (!listOrderDetail.isEmpty()) {
//
//                        // manage order
//                        List<Order> listOrder = "admin".equals(profile.getRoleName()) ? orderDAO.getAllOrder() : orderDAO.getAllOrderStaff();
//
//                        if (!listOrder.isEmpty()) {
//                            Map<String, List<OrderDetail>> listSchedule = new HashMap<>();
//
//                            List<PhotoSchedule> photoList = new ArrayList<>();
//                            for (Order order : listOrder) {
//                                List<OrderDetail> listDetail1 = "admin".equals(profile.getRoleName()) ? orderDetailDAO.getOrderDetailByOrderIdAdmin(order.getOrderId()) : orderDetailDAO.getOrderDetailByOrderId(order.getOrderId());
//                                int photoTmp = 0;
//
//                                for (OrderDetail orderDetail : listDetail1) {
//                                    String arr[] = orderDetail.getItemType().split("-");
//                                    if (arr.length > 1) {
//                                        if (photoTmp != orderDetail.getItemId()) {
//                                            PhotoSchedule photo = photoDAO.getPhotoScheduleByIdAdmin(orderDetail.getItemId());
//                                            if (photo != null) {
//                                                photoTmp = photo.getScheduleId();
//                                                photoList.add(photo);
//                                            }
//                                        }
//                                    }
//                                }
//                                Utilities.groupOrderDetailsAdminLoaded(listDetail1, listSchedule, photoList);
//                            }
//
//                            session.setAttribute("LIST_CART_SCHEDULE_ADMIN", listSchedule);
//                        }
//                    } else {
//                        // set order is confirm;
//                        boolean orderResult = orderDAO.setStatusOrderById(Integer.parseInt(orderId), "confirm");
//                        if (orderResult) {
//                            List<Order> listOrder = "admin".equals(profile.getRoleName()) ? orderDAO.getAllOrder() : orderDAO.getAllOrderStaff();
//
//                            if (!listOrder.isEmpty()) {
//                                List<OrderDetail> listProduct = new ArrayList<>();
//                                Map<String, List<OrderDetail>> listSchedule = new HashMap<>();
//
//                                for (Order order : listOrder) {
//                                    List<OrderDetail> listOrderDetail2 = "admin".equals(profile.getRoleName()) ? orderDetailDAO.getOrderDetailByOrderIdAdmin(order.getOrderId()) : orderDetailDAO.getOrderDetailByOrderId(order.getOrderId());
//                                    Utilities.groupOrderDetails(listOrderDetail2, listSchedule, order.getStatus());
//                                    for (OrderDetail detail : listOrderDetail) {
//                                        //item_id and item_type --> add schedule photo
//                                        if (!detail.getItemType().equals("photo_schedule-location") && !detail.getItemType().equals("photo_schedule-studio")) {
//                                            detail.setStatus(order.getStatus());
//                                            listProduct.add(detail);
//                                        }
//                                    }
//                                }
//
//                                session.setAttribute("LIST_CART_PRODUCT_ADMIN", listProduct);
//                                session.setAttribute("LIST_CART_SCHEDULE_ADMIN", listSchedule);
//                            } else {
//                                Map<String, List<OrderDetail>> listSchedule = new HashMap<>();
//                                session.setAttribute("LIST_CART_SCHEDULE_ADMIN", listSchedule);
//                            }
//                        }
//                    }

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

                        session.setAttribute("LIST_CART_PRODUCT_ADMIN", listProduct);
                        session.setAttribute("LIST_CART_SCHEDULE_ADMIN", listSchedule);
                        url = ADMIN_PAGE;
                    }

                    // get location for manage
                    List<Location> listLocation = locationDAO.getAllLocation();
                    session.setAttribute("LOCATIONS", listLocation);

                    url = profile.getRoleName().equals("admin") ? ADMIN_PAGE : PHOTO_HOME_PAGE;
                }

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
