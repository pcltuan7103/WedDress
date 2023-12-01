/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.LocationDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dto.Location;
import dto.Order;
import dto.OrderDetail;
import dto.OrderItem;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
@WebServlet(name = "UpdateOrderAdminServlet", urlPatterns = {"/UpdateOrderAdminServlet"})
public class UpdateOrderAdminServlet extends HttpServlet {

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
        String id = request.getParameter("orderDetailId");
        String name = request.getParameter("orderDetailName");
        String description = request.getParameter("orderDetailDescription");
        String price = request.getParameter("orderDetailPrice");
        String orderDate = request.getParameter("timeRange");

        OrderDetailDAO dao = new OrderDetailDAO();
        OrderDAO orderDAO = new OrderDAO();
        try {
            HttpSession session = request.getSession();
            if (!id.isEmpty() && !price.isEmpty() && Utilities.isPositiveNumber(price) && session != null) {
                boolean result = dao.updateOrderDetailById(new OrderDetail(Integer.parseInt(id), name, description, Double.parseDouble(price), orderDate));
                if (result) {
                    url = ADMIN_PAGE;
                    // manage order
                    List<Order> listOrder = orderDAO.getAllOrder();
                    List<OrderItem> orderItem = new ArrayList<>();

                    if (listOrder.size() > 0) {
                        for (Order order : listOrder) {
                            List<OrderDetail> listOrderDetail = dao.getOrderDetailByOrderId(order.getOrderId());
                            if (listOrderDetail.size() > 0) {
                                OrderItem orderItemDetail = new OrderItem();
                                orderItemDetail.setList(listOrderDetail);
                                orderItem.add(orderItemDetail);
                            }
                        }
                        session.setAttribute("LIST_ORDER_ADMIN", orderItem);
                    }
                }
            }
        } catch (NamingException ex) {
            log("UpdatePhotoServlet_NamingException: " + ex.getMessage());
        } catch (SQLException ex) {
            log("UpdatePhotoServlet_SQLException " + ex.getMessage());
        } catch (NullPointerException ex) {
            log("UpdatePhotoServlet_NullPointerException " + ex.getMessage());
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
