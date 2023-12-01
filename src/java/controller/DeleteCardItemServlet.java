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
import dao.RentalProductDAO;
import dto.DressPhotoCombo;
import dto.Location;
import dto.Order;
import dto.OrderDetail;
import dto.OrderItem;
import dto.PhotoSchedule;
import dto.PhotographyStudio;
import dto.RentalProduct;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
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
import util.Contant;
import util.PaginationHelper;
import util.Utilities;

/**
 *
 * @author ptd
 */
@WebServlet(name = "DeleteCardItemServlet", urlPatterns = {"/DeleteCardItemServlet"})
public class DeleteCardItemServlet extends HttpServlet {

    public final String CART_PAGE = "cart.jsp";
    public final String HOME_PAGE = "DispatcherServlet?btAction=Home";
    public final String ERROR_PAGE = "error.jsp";

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

        HttpSession session = request.getSession();

        try {
            if (session != null) {
                // get order Id and orderDetailRemove 
                Order existOrder = orderDAO.getOrderById(Integer.parseInt(orderId));

                if (existOrder != null) {
                    OrderDetail orderDetail = orderDetailDAO.getOrderDetailById(Integer.parseInt(orderDetailId));
                    String arr[] = itemType.split("-");
                    if (arr.length > 1) {
                        List<OrderDetail> listRemove = orderDetailDAO.getOrderDetailByItemIdCartClient(Integer.parseInt(itemId));
                        boolean removeSchedule = scheduleDAO.deleteScheduleById(Integer.parseInt(itemId));

                        if (removeSchedule) {
                            for (OrderDetail orderDetail1 : listRemove) {
                                boolean removeDetail = orderDetailDAO.deleteOrderDetail(orderDetail1.getOrderDetailId());
                                if (removeDetail) {
                                    url = CART_PAGE;
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
                                        url = CART_PAGE;
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
                                        url = CART_PAGE;
                                    }
                                }
                            }

                        } else {
                            // remove order detail
                            boolean result = orderDetailDAO.deleteOrderDetail(orderDetail.getOrderDetailId());

                            if (result) {
                                url = CART_PAGE;
                            }
                        }
                    }

                    // get list again
                    List<OrderDetail> listDetail = orderDetailDAO.getOrderDetailByOrderId(existOrder.getOrderId());
                    if (listDetail.size() > 0) {
                        List<OrderDetail> listProduct = new ArrayList<>();

                        Map<String, List<OrderDetail>> listSchedule = Utilities.groupOrderDetails(listDetail);

                        for (OrderDetail detail : listDetail) {
                            //item_id and item_type --> add schedule photo
                            if (!detail.getItemType().equals("photo_schedule-location") && !detail.getItemType().equals("photo_schedule-studio")) {
                                listProduct.add(detail);
                            }
                        }

                        url = CART_PAGE;
                        session.setAttribute("LIST_CART_PRODUCT", listProduct);
                        session.setAttribute("LIST_CART_SCHEDULE", listSchedule);
                        session.setAttribute("CART_ITEM", (listSchedule.size() + listProduct.size()));
                    } else {
                        // order is empty -> delete order
                        boolean result = orderDAO.removeOrderById(existOrder.getOrderId());
                        if (result) {
                            session.setAttribute("LIST_CART_PRODUCT", null);
                            session.setAttribute("LIST_CART_SCHEDULE", null);
                            session.setAttribute("CART_ITEM", null);
                            url = HOME_PAGE;
                        }
                    }
                }
                List<Location> listLocation = locationDAO.getAllLocation();
                List<RentalProduct> listProduct = productDAO.getAllRentalProduct();
                List<PhotographyStudio> listStudio = studioDAO.getAllPhotographyStudio();
                List<DressPhotoCombo> listCombo = comboDAO.getAllDressPhotoCombo();

                List<OrderDetail> listOrder = PaginationHelper.pagingList(listLocation, listProduct, listStudio, listCombo);

                // Set the number of entities per page
                int entitiesPerPage = Contant.PAGE_SIZE;

                // Calculate the total pages for all lists combined
                int totalEntities = listOrder.size();
                int totalPages = (int) Math.ceil((double) totalEntities / entitiesPerPage);

                // Retrieve the current page number from the request parameters
                int currentPage = PaginationHelper.getCurrentPage(request, "page", totalPages);

                List<OrderDetail> listOrderDetailPage = PaginationHelper.getPageEntities(listOrder, currentPage, entitiesPerPage);
                session.setAttribute("LIST_ORDER_PAGING", listOrderDetailPage);
                session.setAttribute("LIST_ORDER_ALL", listOrder);

                request.setAttribute("totalPages", totalPages);
                request.setAttribute("currentPage", currentPage);
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
