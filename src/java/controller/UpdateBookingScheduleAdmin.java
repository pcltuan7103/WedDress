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
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.Contant;
import util.PaginationHelper;

/**
 *
 * @author ptd
 */
public class UpdateBookingScheduleAdmin extends HttpServlet {

    public final String ERROR_PAGE = "error.jsp";
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
        String url = ERROR_PAGE;

        String locationId = request.getParameter("location");
        String studioId = request.getParameter("studio");
        String timeRange = request.getParameter("timeRange");
        String timeRangeReturn = request.getParameter("timeRangeReturn");
        String orderIdText = request.getParameter("txtOrderId");
        String itemTypeText = request.getParameter("txtItemId");

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
                // Get the current time
                LocalDateTime currentTime = LocalDateTime.now();
                // Define the desired date-time format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                // Format the current time using the formatter
                String formattedTime = currentTime.format(formatter);
                // get orderbyProfileId
                Order orderExist = orderDAO.getOrderAdminById(Integer.parseInt(orderIdText));
                int orderId = 0;
                if (orderExist != null) {
                    orderId = orderExist.getOrderId();
                    if (orderId > 0) {
                        boolean isUpdated = false;
                        // get orderDeetail by order id
                        List<OrderDetail> listOrderDetailByOrder = orderDetailDAO.getOrderDetailByOrderId(orderId);

                        // filter by photo schedule
                        for (OrderDetail detail : listOrderDetailByOrder) {
                            // find id and update photo schedule
                            if (detail.getItemType().equals("photo_schedule") && detail.getItemId() == Integer.parseInt(itemTypeText)) {
                                // update photo schedule
                                boolean updateRusult = photoDAO.updatePhotoScheduleById(Integer.parseInt(itemTypeText), Integer.parseInt(locationId), Integer.parseInt(studioId), formattedTime);
                                if (updateRusult) {
                                    isUpdated = true;
                                }
                            }
                        }

                        if (isUpdated) {
                            // manage order
                            List<Order> listOrder = orderDAO.getAllOrder();
                            List<OrderItem> listPhotoScheduleItem = new ArrayList<>();

                            if (listOrder.size() > 0) {
                                for (Order order : listOrder) {
                                    List<OrderDetail> listOrderDetail = orderDetailDAO.getOrderDetailByOrderId(order.getOrderId());

                                    for (OrderDetail detail : listOrderDetail) {
                                        //item_id and item_type --> add schedule photo
                                        if (detail.getItemType().equals("photo_schedule")) {
                                            PhotoSchedule photoSchedule = photoDAO.getPhotoScheduleById(detail.getItemId());

                                            // get item
                                            Location location = locationDAO.getLocationById(photoSchedule.getLocationId());
                                            PhotographyStudio studio = studioDAO.getStudioById(photoSchedule.getStudioId());

                                            // init photo schedule
                                            OrderItem photoScheduleItem = new OrderItem();
                                            List<OrderDetail> listScheduleOrderDetail = new ArrayList<>();

                                            // add item into list
                                            OrderDetail detailLocation = new OrderDetail(detail.getOrderDetailId(), location.getName(), location.getDescription(), location.getPrice(), photoSchedule.getScheduleDate(), order.getOrderId(), photoSchedule.getScheduleId(), "photo_schedule", timeRange, timeRangeReturn);
                                            detailLocation.setStatus(order.getStatus());
                                            listScheduleOrderDetail.add(detailLocation);

                                            OrderDetail detailStudio = new OrderDetail(detail.getOrderDetailId(), studio.getName(), studio.getDescription(), studio.getPrice(), photoSchedule.getScheduleDate(), order.getOrderId(), photoSchedule.getScheduleId(), "photo_schedule", timeRange, timeRangeReturn);
                                            detailStudio.setStatus(order.getStatus());
                                            listScheduleOrderDetail.add(detailStudio);

                                            // add list into item photo schedule    
                                            photoScheduleItem.setList(listScheduleOrderDetail);
                                            listPhotoScheduleItem.add(photoScheduleItem);
                                        } else {
                                            OrderItem photoScheduleItem = new OrderItem();
                                            List<OrderDetail> listScheduleOrderDetail = new ArrayList<>();
                                            detail.setStatus(order.getStatus());
                                            listScheduleOrderDetail.add(detail);
                                            // add list into item photo schedule    
                                            photoScheduleItem.setList(listScheduleOrderDetail);
                                            listPhotoScheduleItem.add(photoScheduleItem);
                                        }
                                    }

                                }
                                url = ADMIN_PAGE;
                                session.setAttribute("LIST_ORDER_ADMIN", listPhotoScheduleItem);
                            }
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
            log("BookScheduleServlet_NamingException: " + ex.getMessage());
        } catch (SQLException ex) {
            log("BookScheduleServlet_SQLException " + ex.getMessage());
        } finally {
//            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
//            dispatcher.forward(request, response);
            response.sendRedirect(url);
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
