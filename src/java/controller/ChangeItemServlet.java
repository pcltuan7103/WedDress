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
import dto.Profile;
import dto.RentalProduct;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@WebServlet(name = "ChangeItemServlet", urlPatterns = {"/ChangeItemServlet"})
public class ChangeItemServlet extends HttpServlet {

    public final String ERROR_PAGE = "error.jsp";
    public final String CART_PAGE = "cart.jsp";

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

        String url = CART_PAGE;

        String itemId = request.getParameter("itemId");
        String itemType = request.getParameter("itemType");
        String orderDetailId = request.getParameter("detailId");
        String orderIdText = request.getParameter("orderId");
        String id = request.getParameter("id");
        String time = Utilities.getCurrentDateByFormat("yyyy-MM-dd HH:mm");
        String timeRange = request.getParameter("timeRange");
        String timeRangeReturn = request.getParameter("timeRangeReturn");

        // create booking schedule - create order for card - order detail
        PhotoScheduleDAO photoDAO = new PhotoScheduleDAO();
        LocationDAO locationDAO = new LocationDAO();
        PhotographyStudiosDAO studioDAO = new PhotographyStudiosDAO();
        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        RentalProductDAO productDAO = new RentalProductDAO();
        DressPhotoComboDAO comboDAO = new DressPhotoComboDAO();

        try {
            HttpSession session = request.getSession();
            Profile profile = (Profile) session.getAttribute("USER");
            if (session != null && profile != null) {

                // add photo schedule
                Order orderExist = orderDAO.getOrderById(Integer.parseInt(orderIdText));
                // get orderbyProfileId
                int orderId = 0;
                if (orderExist != null) {
                    orderId = orderExist.getOrderId();
                    if (orderId > 0) {
                        boolean isUpdated = false;

                        String[] arr = itemType.split("-");
                        // there is photo schedule
                        if (arr.length > 1) {
                            PhotoSchedule photo = photoDAO.getPhotoScheduleByIdCreate(Integer.parseInt(itemId));
                            List<String> listScheduleAvailable = photoDAO.checkScheduleAvailableAdmin(timeRange, timeRangeReturn, arr[1], Integer.parseInt(id), photo);
                            if (listScheduleAvailable == null) {
                                // update photoschedule
                                String type = arr[1];
                                if ("location".equals(type)) {
                                    Location location = locationDAO.getLocationById(Integer.parseInt(id));
                                    boolean updateSchedule = photoDAO.updateLocationByScheduleId(Integer.parseInt(itemId), Integer.parseInt(id), time);

                                    if (updateSchedule) {
                                        // update order detail 
                                        OrderDetail orderDetail = new OrderDetail(Integer.parseInt(orderDetailId), location.getName(), location.getDescription(), location.getPrice(), time);
                                        boolean updateDetail = orderDetailDAO.updateOrderDetailById(orderDetail);

                                        if (updateDetail) {
                                            isUpdated = true;
                                        }
                                    }

                                } else {
                                    PhotographyStudio studio = studioDAO.getStudioById(Integer.parseInt(id));
                                    boolean updateSchedule = photoDAO.updateStudioByScheduleId(Integer.parseInt(itemId), Integer.parseInt(id), time);

                                    if (updateSchedule) {
                                        // update order detail 
                                        OrderDetail orderDetail = new OrderDetail(Integer.parseInt(orderDetailId), studio.getName(), studio.getDescription(), studio.getPrice(), time);
                                        boolean updateDetail = orderDetailDAO.updateOrderDetailById(orderDetail);

                                        if (updateDetail) {
                                            isUpdated = true;
                                        }
                                    }
                                }
                            } else {
                                String errMessage = "Already has a booking from " + listScheduleAvailable.get(0) + " to " + listScheduleAvailable.get(1) + " pls change location or studio or time-range";
                                request.setAttribute("BOOK_NOT_AVAILABLE", errMessage);
                            }
                        } else {
                            OrderDetail detail = orderDetailDAO.getOrderDetailById(Integer.parseInt(orderDetailId));
                            if ("rental_product".equals(itemType)) {
                                RentalProduct product = productDAO.getRentalProductById(detail.getItemId());
                                if (product != null) {
                                    int stock = product.getStock() + 1;
                                    boolean changeStock = productDAO.setStockRentalProduct(product.getId(), stock);

                                    if (changeStock) {
                                        // change orderdetail item
                                        RentalProduct itemChange = productDAO.getRentalProductById(Integer.parseInt(id));

                                        //set value of detail for update order detail
                                        detail.setName(itemChange.getName());
                                        detail.setDescription(itemChange.getDescription());
                                        detail.setPrice(itemChange.getPrice());
                                        detail.setItemId(itemChange.getId());
                                        detail.setItemType(itemType);

                                        boolean changeOrderDetail = orderDetailDAO.changeOrderDetail(detail);
                                        if (changeOrderDetail) {
                                            isUpdated = true;
                                        }
                                    }
                                }
                            } else if ("combo".equals(detail.getItemType())) {
                                DressPhotoCombo combo = comboDAO.getComboById(detail.getItemId());
                                if (combo != null) {
                                    int stock = combo.getStock() + 1;
                                    boolean resultCombo = comboDAO.setStockCombo(detail.getItemId(), stock);

                                    if (resultCombo) {
                                        DressPhotoCombo itemChange = comboDAO.getComboById(Integer.parseInt(id));

                                        //set value of detail for update order detail
                                        detail.setName(itemChange.getComboName());
                                        detail.setDescription(itemChange.getComboDescription());
                                        detail.setPrice(itemChange.getPrice());
                                        detail.setItemId(itemChange.getId());
                                        detail.setItemType(itemType);

                                        boolean changeOrderDetail = orderDetailDAO.changeOrderDetail(detail);
                                        if (changeOrderDetail) {
                                            isUpdated = true;
                                        }
                                    }
                                }
                            } else if ("location".equals(detail.getItemType())) {
                                Location itemChange = locationDAO.getLocationById(Integer.parseInt(id));

                                //set value of detail for update order detail
                                detail.setName(itemChange.getName());
                                detail.setDescription(itemChange.getDescription());
                                detail.setPrice(itemChange.getPrice());
                                detail.setItemId(itemChange.getId());
                                detail.setItemType(itemType);

                                boolean changeOrderDetail = orderDetailDAO.changeOrderDetail(detail);
                                if (changeOrderDetail) {
                                    isUpdated = true;
                                }
                            } else if ("studio".equals(detail.getItemType())) {
                                PhotographyStudio itemChange = studioDAO.getStudioById(Integer.parseInt(id));

                                //set value of detail for update order detail
                                detail.setName(itemChange.getName());
                                detail.setDescription(itemChange.getDescription());
                                detail.setPrice(itemChange.getPrice());
                                detail.setItemId(itemChange.getId());
                                detail.setItemType(itemType);

                                boolean changeOrderDetail = orderDetailDAO.changeOrderDetail(detail);
                                if (changeOrderDetail) {
                                    isUpdated = true;
                                }
                            }
                        }

                        if (isUpdated) {
                            List<OrderDetail> listOrderDetail = orderDetailDAO.getOrderDetailByOrderId(orderId);

                            List<OrderDetail> listProduct = new ArrayList<>();

                            Map<String, List<OrderDetail>> listSchedule = Utilities.groupOrderDetails(listOrderDetail);

                            for (OrderDetail detail : listOrderDetail) {
                                //item_id and item_type --> add schedule photo
                                if (!detail.getItemType().equals("photo_schedule-location") && !detail.getItemType().equals("photo_schedule-studio")) {
                                    listProduct.add(detail);
                                }
                            }

                            url = CART_PAGE;
                            session.setAttribute("LIST_CART_PRODUCT", listProduct);
                            session.setAttribute("LIST_CART_SCHEDULE", listSchedule);
                            session.setAttribute("CART_ITEM", (listSchedule.size() + listProduct.size()));
                        }
                    }
                }

            }
        } catch (NamingException ex) {
            log("BookScheduleServlet_NamingException: " + ex.getMessage());
        } catch (SQLException ex) {
            log("BookScheduleServlet_SQLException " + ex.getMessage());
        } finally {
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
//            response.sendRedirect(url);
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
