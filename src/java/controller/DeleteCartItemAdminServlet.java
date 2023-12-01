/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DressPhotoComboDAO;
import dao.LocationDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.PhotoScheduleDAO;
import dao.PhotographyStudiosDAO;
import dao.RejectOrderDAO;
import dao.RentalProductDAO;
import dto.DressPhotoCombo;
import dto.Location;
import dto.Order;
import dto.OrderDetail;
import dto.OrderItem;
import dto.PhotoSchedule;
import dto.PhotographyStudio;
import dto.Profile;
import dto.RejectOrder;
import dto.RentalProduct;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.Contant;
import util.PaginationHelper;
import util.Utilities;

/**
 *
 * @author ptd
 */
public class DeleteCartItemAdminServlet extends HttpServlet {

    public final String ERROR_PAGE = "error.jsp";
    public final String ADMIN_PAGE = "admin.jsp";
    public final String PHOTO_HOME_PAGE = "photoHome.jsp";
    public final String RENTAL_PAGE = "rentalPage.jsp";

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
        String orderDetailId = request.getParameter("orderDetailId");
        String itemId = request.getParameter("itemId");
        String itemType = request.getParameter("itemType");
        String url = ERROR_PAGE;

        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        PhotoScheduleDAO scheduleDAO = new PhotoScheduleDAO();
        LocationDAO locationDAO = new LocationDAO();
        PhotographyStudiosDAO studioDAO = new PhotographyStudiosDAO();
        RentalProductDAO productDAO = new RentalProductDAO();
        DressPhotoComboDAO comboDAO = new DressPhotoComboDAO();
        RejectOrderDAO rejectDAO = new RejectOrderDAO();

        HttpSession session = request.getSession();
        Profile profile = (Profile) session.getAttribute("USER");

        try {
            if (session != null) {
                // get order Id and orderDetailRemove 
                Order existOrder = orderDAO.getOrderAdminById(Integer.parseInt(orderId));

                if (existOrder != null) {
                    OrderDetail orderDetail = orderDetailDAO.getOrderDetailByIdAdmin(Integer.parseInt(orderDetailId));
                    String arr[] = itemType.split("-");
                    if (arr.length > 1) {
                        List<OrderDetail> listRemove = orderDetailDAO.getOrderDetailByItemIdCart(Integer.parseInt(itemId));
                        boolean removeSchedule = scheduleDAO.deleteScheduleById(Integer.parseInt(itemId));

                        if (removeSchedule) {
                            for (OrderDetail orderDetail1 : listRemove) {
                                boolean removeDetail = orderDetailDAO.deleteOrderDetail(orderDetail1.getOrderDetailId());
                                if (removeDetail) {
                                    RejectOrder rejectOrder = new RejectOrder("reject", orderDetail.getName(), orderDetail.getDescription(), orderDetail.getPrice(),
                                            orderDetail.getOrderDate(), orderDetail.getOrderStartDate(), orderDetail.getOrderEndDate(), existOrder.getProfileId(), orderDetail.getItemType(), orderDetail.getItemId());
                                    boolean addReject = rejectDAO.saveRejectOrder(rejectOrder);
                                    if (addReject) {
                                        url = ADMIN_PAGE;
                                    }
                                }
                            }

                        }

                    } else {
                        if ("rental_product".equals(orderDetail.getItemType())) {
                            RentalProduct product = productDAO.getRentalProductById(orderDetail.getItemId());
                            if (product != null) {
                                int stock = product.getStock() + 1;
                                boolean resultProduct = productDAO.setStockRentalProduct(orderDetail.getItemId(), stock);

                                if (resultProduct) {

                                    // remove order detail
                                    boolean result = orderDetailDAO.deleteOrderDetail(orderDetail.getOrderDetailId());

                                    if (result) {
                                        RejectOrder rejectOrder = new RejectOrder("reject", orderDetail.getName(), orderDetail.getDescription(), orderDetail.getPrice(),
                                                orderDetail.getOrderDate(), orderDetail.getOrderStartDate(), orderDetail.getOrderEndDate(), existOrder.getProfileId(), orderDetail.getItemType(), orderDetail.getItemId());
                                        boolean addReject = rejectDAO.saveRejectOrder(rejectOrder);
                                        if (addReject) {
                                            url = ADMIN_PAGE;
                                        }
                                    }
                                }
                            }
                        } else if ("combo".equals(orderDetail.getItemType())) {
                            DressPhotoCombo combo = comboDAO.getComboByIdDelete(orderDetail.getItemId());
                            if (combo != null) {
                                int stock = combo.getStock() + 1;
                                boolean resultCombo = comboDAO.setStockCombo(orderDetail.getItemId(), stock);

                                if (resultCombo) {

                                    // remove order detail
                                    boolean result = orderDetailDAO.deleteOrderDetail(orderDetail.getOrderDetailId());

                                    if (result) {
                                        RejectOrder rejectOrder = new RejectOrder("reject", orderDetail.getName(), orderDetail.getDescription(), orderDetail.getPrice(),
                                                orderDetail.getOrderDate(), orderDetail.getOrderStartDate(), orderDetail.getOrderEndDate(), existOrder.getProfileId(), orderDetail.getItemType(), orderDetail.getItemId());
                                        boolean addReject = rejectDAO.saveRejectOrder(rejectOrder);
                                        if (addReject) {
                                            url = ADMIN_PAGE;
                                        }
                                    }
                                }
                            }

                        } else {
                            // remove order detail
                            boolean result = orderDetailDAO.deleteOrderDetail(orderDetail.getOrderDetailId());

                            if (result) {
                                RejectOrder rejectOrder = new RejectOrder("reject", orderDetail.getName(), orderDetail.getDescription(), orderDetail.getPrice(),
                                        orderDetail.getOrderDate(), orderDetail.getOrderStartDate(), orderDetail.getOrderEndDate(), existOrder.getProfileId(), orderDetail.getItemType(), orderDetail.getItemId());
                                boolean addReject = rejectDAO.saveRejectOrder(rejectOrder);
                                if (addReject) {
                                    url = ADMIN_PAGE;
                                }
                            }
                        }
                    }

                    // manage order
                    List<Order> listOrder = orderDAO.getAllOrder();

                    if (listOrder.size() > 0) {
                        List<OrderDetail> listProduct = new ArrayList<>();
                        Map<String, List<OrderDetail>> listSchedule = new HashMap<>();

                        for (Order order : listOrder) {
                            List<OrderDetail> listOrderDetail = orderDetailDAO.getOrderDetailByOrderIdAdmin(order.getOrderId());
                            int photoTmp = 0;
                            List<PhotoSchedule> photoList = new ArrayList<>();
                            for (OrderDetail od : listOrderDetail) {
                                String arr1[] = od.getItemType().split("-");
                                if (arr1.length > 1) {
                                    if (photoTmp != od.getItemId()) {
                                        PhotoSchedule photo = scheduleDAO.getPhotoScheduleByIdAdmin(od.getItemId());
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
                    } else {
                        // order is empty -> delete order
                        boolean result = orderDAO.removeOrderById(existOrder.getOrderId());
                        if (result) {
                            session.setAttribute("LIST_CART_PRODUCT", null);
                            session.setAttribute("LIST_CART_SCHEDULE", null);
                            url = ADMIN_PAGE;
                        }
                    }
                }

            }
        } catch (NamingException ex) {
            log("DeleteCartItemServlet_NamingException: " + ex.getMessage());
        } catch (SQLException ex) {
            log("DeleteCartItemServlet_SQLException " + ex.getMessage());
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
