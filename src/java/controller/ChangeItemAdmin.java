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
import util.Utilities;

/**
 *
 * @author ptd
 */
public class ChangeItemAdmin extends HttpServlet {

    public final String ERROR_PAGE = "error.jsp";
    public final String ADMIN_PAGE = "admin.jsp";
    public final String PHOTO_HOME_PAGE = "photoHome.jsp";
    public final String RENTAL_PAGE = "rentalpage.jsp";

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

        String url = "admin.jsp";

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
        String[] arr = itemType.split("-");
        try {
            HttpSession session = request.getSession();
            Profile profile = (Profile) session.getAttribute("USER");
            if (session != null && profile != null) {

                // add photo schedule
                Order orderExist = orderDAO.getOrderAdminById(Integer.parseInt(orderIdText));
                // get orderbyProfileId
                int orderId = 0;
                if (orderExist != null) {
                    orderId = orderExist.getOrderId();
                    if (orderId > 0) {
                        boolean isUpdated = false;

                        // there is photo schedule
                        if (arr.length > 1) {
                            PhotoSchedule photo = photoDAO.getPhotoScheduleByIdAdmin(Integer.parseInt(itemId));
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
                            OrderDetail detail = orderDetailDAO.getOrderDetailByIdAdmin(Integer.parseInt(orderDetailId));
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
                            // manage order
                            List<Order> listOrder = orderDAO.getAllOrder();

                            if (listOrder.size() > 0) {
                                List<OrderDetail> listProduct = new ArrayList<>();
                                Map<String, List<OrderDetail>> listSchedule = new HashMap<>();

                                for (Order order : listOrder) {
                                    List<PhotoSchedule> photoList = new ArrayList<>();

                                    List<OrderDetail> listDetail1 = "admin".equals(profile.getRoleName()) ? orderDetailDAO.getOrderDetailByOrderIdAdmin(order.getOrderId()) : orderDetailDAO.getOrderDetailByOrderId(order.getOrderId());
                                    int photoTmp = 0;

                                    for (OrderDetail orderDetail : listDetail1) {
                                        String arr1[] = orderDetail.getItemType().split("-");
                                        if (arr1.length > 1) {
                                            if (photoTmp != orderDetail.getItemId()) {
                                                PhotoSchedule photo = photoDAO.getPhotoScheduleByIdAdmin(orderDetail.getItemId());
                                                if (photo != null) {
                                                    photoTmp = photo.getScheduleId();
                                                    photoList.add(photo);
                                                }
                                            }
                                        }

                                        
                                    }
                                    Utilities.groupOrderDetailsAdminLoaded(listDetail1, listSchedule, photoList);
                                    for (OrderDetail detail : listDetail1) {
                                        //item_id and item_type --> add schedule photo
                                        if (!detail.getItemType().equals("photo_schedule-location") && !detail.getItemType().equals("photo_schedule-studio")) {
                                            detail.setStatus(order.getStatus());
                                            listProduct.add(detail);
                                        }
                                    }

                                }

                                session.setAttribute("LIST_CART_PRODUCT_ADMIN", listProduct);
                                session.setAttribute("LIST_CART_SCHEDULE_ADMIN", listSchedule);
                                url = profile.getRoleName().equals("admin") ? ADMIN_PAGE : profile.getRoleName().equals("staff") ? PHOTO_HOME_PAGE : RENTAL_PAGE;
                            }
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
