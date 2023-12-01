/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DressPhotoComboDAO;
import dao.LocationDAO;
import dao.PhotographyStudiosDAO;
import dao.RentalProductDAO;
import dto.DressPhotoCombo;
import dto.Location;
import dto.OrderDetail;
import dto.PhotographyStudio;
import dto.Profile;
import dto.RentalProduct;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
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
public class DispatcherServlet extends HttpServlet {

    public final String LOGIN_SERVLET = "LoginServlet";
    public final String LOGOUT_SERVLET = "LogoutServlet";
    public final String SEARCH_SERVLET = "SearchServlet";
    public final String FILTER_SERVLET = "FilterServlet";
    public final String UPDATE_PROFILE_SERVLET = "UpdateProfileServlet";
    public final String DELETE_PROFILE_SERVLET = "DeleteProfileServlet";
    public final String DELETE_ACCOUNT_SERVLET = "DeleteAccountServlet";
    public final String CATEGORY_SERVLET = "CategoryServlet";
    public final String CATEGORY_SERVLET_ADMIN = "CategoryAdminServlet";
    public final String BOOK_SCHEDULE = "BookScheduleServlet";
    public final String DELETE_CART_ITEM = "DeleteCardItemServlet";
    public final String EDIT_ACCOUNT_SERVLET = "EditAccountServlet";
    public final String UPDATE_ORDER_ADMIN_SERVLET = "UpdateOrderAdminServlet";
    public final String DELETE_ORDER_SERVLET = "DeleteOrderServlet";

    public final String COMFIRM_SCHEDULE_SERVLET = "ConfirmScheduleServlet";
    public final String COMFIRM_PRODUCT_SERVLET = "ConfirmProductServlet";

    public final String UPDATE_LOCATION_SERVLET = "UpdateLocationServlet";
    public final String DELETE_LOCATION_SERVLET = "DeleteLocationServlet";

    public final String UPDATE_PRODUCT_SERVLET = "UpdateProductServlet";
    public final String DELETE_PRODUCT_SERVLET = "DeleteProductServlet";

    public final String ADD_ACCOUNT_SERVLET = "AddAccountServlet";
    public final String REGISTER_ACCOUNT_SERVLET = "RegisterAccountServlet";

    public final String UPDATE_SCHEDULE_SERVLET = "UpdateScheduleServlet";
    public final String PAYMENT_SERVLET = "PaymentServlet";

    public final String ADD_TO_CART_SERVLET = "AddToCartServlet";
    public final String POPUP_UPDATE_SERVLET = "PopupUpdateServlet";

    public final String ADD_LOCATION_SERVLET = "AddLocationServlet";
    public final String ADD_PRODUCT_SERVLET = "AddProductServlet";
    public final String VERIFY_PASSWORD_SERVLET = "VerifyPasswordServlet";

    public final String CHANGE_ITEM_SERVLET = "ChangeItemServlet";
    public final String FORGOT_PASSWORD_SERVLET = "ForgotPasswordServlet";
    
    public final String REJECT_CONFIRM_SERVLET = "RejectConfirmServlet";

    public final String HOME_PAGE = "home.jsp";

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

        String action = request.getParameter("btAction");
        String url = HOME_PAGE;

        LocationDAO locationDAO = new LocationDAO();
        RentalProductDAO rentalDAO = new RentalProductDAO();
        PhotographyStudiosDAO photoDAO = new PhotographyStudiosDAO();
        DressPhotoComboDAO dressPhotoComboDAO = new DressPhotoComboDAO();
        HttpSession session = request.getSession();

        try {
            if (action == null || "Logout".equals(action) || "Home".equals(action)) {

                List<Location> listLocation = locationDAO.getAllLocation();
                List<RentalProduct> listProduct = rentalDAO.getAllRentalProduct();
                List<PhotographyStudio> listStudio = photoDAO.getAllPhotographyStudio();
                List<DressPhotoCombo> listCombo = dressPhotoComboDAO.getAllDressPhotoCombo();

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
            if ("Login".equals(action)) {
                url = LOGIN_SERVLET;
            } else if ("Logout".equals(action)) {
                url = LOGOUT_SERVLET;
            } else if ("Search".equals(action)) {
                url = SEARCH_SERVLET;
            } else if ("UpdateLocation".equals(action)) {
                url = UPDATE_LOCATION_SERVLET;
            } else if ("DeleteLocation".equals(action)) {
                url = DELETE_LOCATION_SERVLET;
            } else if ("Filter".equals(action)) {
                url = FILTER_SERVLET;
            } else if ("Save Profile".equals(action)) {
                url = UPDATE_PROFILE_SERVLET;
            } else if ("Delete Profile".equals(action)) {
                url = DELETE_PROFILE_SERVLET;
            } else if ("Delete Account".equals(action)) {
                url = DELETE_ACCOUNT_SERVLET;
            } else if ("CategoryFilter".equals(action)) {
                url = CATEGORY_SERVLET;
            } else if ("CategoryFilterAdmin".equals(action)) {
                url = CATEGORY_SERVLET_ADMIN;
            } else if ("Book Schedule".equals(action)) {
                url = BOOK_SCHEDULE;
            } else if ("DeleteItem".equals(action)) {
                url = DELETE_CART_ITEM;
            } else if ("EditAccount".equals(action)) {
                url = EDIT_ACCOUNT_SERVLET;
            } else if ("Update Order".equals(action)) {
                url = UPDATE_ORDER_ADMIN_SERVLET;
            } else if ("Delete Order".equals(action)) {
                url = DELETE_ORDER_SERVLET;
            } else if ("Confirm Schedule".equals(action)) {
                url = COMFIRM_SCHEDULE_SERVLET;
            } else if ("UpdateProduct".equals(action)) {
                url = UPDATE_PRODUCT_SERVLET;
            } else if ("DeleteProduct".equals(action)) {
                url = DELETE_PRODUCT_SERVLET;
            } else if ("Confirm Rent".equals(action)) {
                url = COMFIRM_PRODUCT_SERVLET;
            } else if ("Add Account".equals(action)) {
                url = ADD_ACCOUNT_SERVLET;
            } else if ("Register".equals(action)) {
                url = REGISTER_ACCOUNT_SERVLET;
            } else if ("Update Booking Schedule".equals(action)) {
                url = UPDATE_SCHEDULE_SERVLET;
            } else if ("Payment".equals(action)) {
                url = PAYMENT_SERVLET;
            } else if ("Add To Cart".equals(action)) {
                url = ADD_TO_CART_SERVLET;
            } else if ("popupUpdate".equals(action)) {
                url = POPUP_UPDATE_SERVLET;
            } else if ("Add Location".equals(action)) {
                url = ADD_LOCATION_SERVLET;
            } else if ("Add Product".equals(action)) {
                url = ADD_PRODUCT_SERVLET;
            } else if ("Change Item".equals(action)) {
                url = CHANGE_ITEM_SERVLET;
            } else if ("ForgotPassword".equals(action)) {
                url = FORGOT_PASSWORD_SERVLET;
            } else if ("Verify".equals(action)) {
                url = VERIFY_PASSWORD_SERVLET;
            } else if ("changePassword".equals(action)) {
                url = FORGOT_PASSWORD_SERVLET;
            }else if("Reject Order".equals(action)){
                url = REJECT_CONFIRM_SERVLET;
            }
        } catch (NamingException ex) {
            log("DispatcherServlet_NamingException: " + ex.getMessage());
        } catch (SQLException ex) {
            log("DispatcherServlet_SQLException " + ex.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
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
