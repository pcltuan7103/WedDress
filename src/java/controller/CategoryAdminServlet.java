/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.DressPhotoComboDAO;
import dao.FeedbackDAO;
import dao.LocationDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.PhotoScheduleDAO;
import dao.PhotographyStudiosDAO;
import dao.RentalProductDAO;
import dao.RoleDAO;
import dto.DressPhotoCombo;
import dto.Location;
import dto.Order;
import dto.OrderDetail;
import dto.PhotoSchedule;
import dto.PhotographyStudio;
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
@WebServlet(name = "CategoryAdminServlet", urlPatterns = {"/CategoryAdminServlet"})
public class CategoryAdminServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        String url = "admin.jsp";

        HttpSession session = request.getSession();
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
        try {

            String value = request.getParameter("category");

            List<Order> listOrder = orderDAO.getAllOrder();

            if (!listOrder.isEmpty()) {
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

//                if("location".equals(value)){
//                    
//                }
//              
                // define list of category
                List<OrderDetail> list = new ArrayList<>();

                // filter by item type
                listProduct.stream().forEach(od -> {
                    if (od.getItemType().equals(value)) {
                        list.add(od);
                    }
                });

                // filter list of item-type = confirm
                List<OrderDetail> confirmList = listProduct.stream().filter(od -> od.getItemType().equals("confirm")).toList();
                if ("location".equals(value)) {
                    List<Location> locations = locationDAO.getAllLocation();

                    confirmList.forEach(c -> {
                        locations.forEach(l -> {
                            if (c.getName().equals(l.getName())) {
                                list.add(c);
                            }
                        });
                    });
                    session.setAttribute("LIST_CART_PRODUCT_ADMIN", list);
                    session.setAttribute("LIST_CART_SCHEDULE_ADMIN", null);
                } else if ("studio".equals(value)) {
                    List<PhotographyStudio> studios = studioDAO.getAllPhotographyStudio();

                    confirmList.forEach(c -> {
                        studios.forEach(l -> {
                            if (c.getName().equals(l.getName())) {
                                list.add(c);
                            }
                        });
                    });
                    session.setAttribute("LIST_CART_PRODUCT_ADMIN", list);
                    session.setAttribute("LIST_CART_SCHEDULE_ADMIN", null);
                } else if ("combo".equals(value)) {
                    List<DressPhotoCombo> combos = comboDAO.getAllDressPhotoCombo();

                    confirmList.forEach(c -> {
                        combos.forEach(l -> {
                            if (c.getName().equals(l.getComboName())) {
                                list.add(c);
                            }
                        });
                    });
                    session.setAttribute("LIST_CART_PRODUCT_ADMIN", list);
                    session.setAttribute("LIST_CART_SCHEDULE_ADMIN", null);
                } else if ("product".equals(value)) {
                    List<RentalProduct> products = rentalProductDAO.getAllRentalProduct();

                    confirmList.forEach(c -> {
                        products.forEach(l -> {
                            if (c.getName().equals(l.getName())) {
                                list.add(c);
                            }
                        });
                    });
                    session.setAttribute("LIST_CART_PRODUCT_ADMIN", list);
                    session.setAttribute("LIST_CART_SCHEDULE_ADMIN", null);
                } else if ("schedule".equals(value)) {
                    session.setAttribute("LIST_CART_SCHEDULE_ADMIN", listSchedule);
                    session.setAttribute("LIST_CART_PRODUCT_ADMIN", null);

                } else {
                    session.setAttribute("LIST_CART_SCHEDULE_ADMIN", listSchedule);
                    session.setAttribute("LIST_CART_PRODUCT_ADMIN", listProduct);
                }

                url = "admin.jsp";
            }
        } catch (NamingException ex) {
            log("DispatcherServlet_NamingException: " + ex.getMessage());
        } catch (SQLException ex) {
            log("DispatcherServlet_SQLException " + ex.getMessage());
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
