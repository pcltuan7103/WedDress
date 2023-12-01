/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.CartDAO;
import dao.DressPhotoComboDAO;
import dao.LocationDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.PhotoScheduleDAO;
import dao.PhotographyStudiosDAO;
import dao.RentalProductDAO;
import dto.Cart;
import dto.DressPhotoCombo;
import dto.Location;
import dto.Order;
import dto.OrderDetail;
import dto.PhotoSchedule;
import dto.OrderItem;
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
import java.util.Map;
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
@WebServlet(name = "BookScheduleServlet", urlPatterns = {"/BookScheduleServlet"})
public class BookScheduleServlet extends HttpServlet {

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

        String locationId = request.getParameter("location");
        String studioId = request.getParameter("studio");
        String timeRange = Utilities.convertTimeZoneToISO120(request.getParameter("timeRange"));
        String timeRangeReturn = Utilities.convertTimeZoneToISO120(request.getParameter("timeRangeReturn"));

        // create booking schedule - create order for card - order detail
        PhotoScheduleDAO photoDAO = new PhotoScheduleDAO();
        LocationDAO locationDAO = new LocationDAO();
        PhotographyStudiosDAO studioDAO = new PhotographyStudiosDAO();
        RentalProductDAO productDAO = new RentalProductDAO();
        DressPhotoComboDAO comboDAO = new DressPhotoComboDAO();
        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

        try {
            HttpSession session = request.getSession();
            Profile profile = (Profile) session.getAttribute("USER");
            List<String> listScheduleAvailable = photoDAO.checkScheduleAvailable(timeRange, timeRangeReturn, Integer.parseInt(locationId), Integer.parseInt(studioId));
            if (listScheduleAvailable == null) {
                if (session != null && profile != null) {
                    // Get the current time
                    LocalDateTime currentTime = LocalDateTime.now();
                    // Define the desired date-time format
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    // Format the current time using the formatter
                    String formattedTime = currentTime.format(formatter);
                    // add photo schedule
                    boolean photoResult = photoDAO.insertPhotoSchedule(new PhotoSchedule(1, profile.getProfileId(), Integer.parseInt(locationId), Integer.parseInt(studioId), formattedTime, "create", timeRange, timeRangeReturn));

                    if (photoResult) {
//                    //get last photo schedule Id
//                    int photoScheduleId = photoDAO.getLastIdOfPhotoSchedule();

                        // get orderbyProfileId
                        Order orderExist = orderDAO.getOrderByProfileId(profile.getProfileId());
                        int orderId = 0;
                        if (orderExist != null) {
                            orderId = orderExist.getOrderId();
                        } else {
                            // add order and order Detail
                            boolean orderResult = orderDAO.insertOrder(new Order(profile.getProfileId(), formattedTime, "create")); // see it

                            // get last order to add order detail
                            if (orderResult) {
                                Order order = orderDAO.getLastOrder();
                                orderId = order.getOrderId();
                            }
                        }

                        if (orderId > 0) {
                            PhotoSchedule lastPhoto = photoDAO.getLastPhotoSchedule();

                            // add insert order detail
                            // get item
                            Location location = locationDAO.getLocationById(lastPhoto.getLocationId());
                            PhotographyStudio studio = studioDAO.getStudioById(lastPhoto.getStudioId());

                            OrderDetail orderDetailL = new OrderDetail(0, location.getName(), location.getDescription(), location.getPrice(), formattedTime, orderId, lastPhoto.getScheduleId(), "photo_schedule-location", timeRange, timeRangeReturn);
                            OrderDetail orderDetailS = new OrderDetail(0, studio.getName(), studio.getDescription(), studio.getPrice(), formattedTime, orderId, lastPhoto.getScheduleId(), "photo_schedule-studio", timeRange, timeRangeReturn);

                            boolean resultOrderDetailL = orderDetailDAO.insertOrderDetail(orderDetailL);
                            boolean resultOrderDetailS = orderDetailDAO.insertOrderDetail(orderDetailS);

                            if (resultOrderDetailL && resultOrderDetailS) {
                                List<OrderDetail> listOrderDetailByOrder = orderDetailDAO.getOrderDetailByOrderId(orderId);

                                List<OrderDetail> listProduct = new ArrayList<>();

                                Map<String, List<OrderDetail>> listSchedule = Utilities.groupOrderDetails(listOrderDetailByOrder);

                                for (OrderDetail detail : listOrderDetailByOrder) {
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
            } else {
                url = "home.jsp";
                String errMessage = "Already has a booking from " + listScheduleAvailable.get(0) + " to " + listScheduleAvailable.get(1) + " pls change location or studio or time-range";
                request.setAttribute("BOOK_NOT_AVAILABLE", errMessage);
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
