/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.PhotoScheduleDAO;
import dto.OrderDetail;
import dto.OrderItem;
import dto.PhotoSchedule;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ptd
 */
public class CompletePay extends HttpServlet {

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

        String orderId = request.getParameter("sku");
        String amount = request.getParameter("amount");
        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        PhotoScheduleDAO scheduleDAO = new PhotoScheduleDAO();
        String url = "error.jsp";
        HttpSession session = request.getSession();

        try {
            boolean result = orderDAO.confirmOrderById(Integer.parseInt(orderId), "pending", Double.parseDouble(amount));
            List<OrderDetail> list = orderDetailDAO.getOrderDetailByOrderId(Integer.parseInt(orderId));
            List<String> errList = new ArrayList<>();
            int tmp = 0;
            for (OrderDetail orderDetail : list) {
                String arr[] = orderDetail.getItemType().split("-");
                if (arr.length > 1) {
                    PhotoSchedule photo = scheduleDAO.getPhotoScheduleByIdCreate(orderDetail.getItemId());
                    if (photo != null) {
                        List<String> listAvailable = scheduleDAO.checkScheduleAvailable(orderDetail.getOrderStartDate(), orderDetail.getOrderEndDate(), photo.getLocationId(), photo.getStudioId());
                        if (listAvailable == null) {
                            boolean rs = scheduleDAO.pendingScheduleByScheduleId(orderDetail.getItemId());
                            if(rs){
                                tmp = orderDetail.getItemId();
                            }
                            boolean resul = orderDetailDAO.deleteOrderDetailById(orderDetail.getOrderDetailId());
                        } else {
                            errList.add(orderDetail.getItemType());
                        }
                    }
                    if(tmp == orderDetail.getItemId()){
                        boolean resul = orderDetailDAO.deleteOrderDetailById(orderDetail.getOrderDetailId());
                    }
                }else{
                    boolean resul = orderDetailDAO.deleteOrderDetailById(orderDetail.getOrderDetailId());
                }

            }
            if (result) {
                url = "DispatcherServlet?btAction=Home";
                session.setAttribute("LIST_CARR_ITEM", null);
                session.setAttribute("CART_ITEM", null);
                request.setAttribute("PAYMENT_SUCCESS", "Payment Success");
                if (!errList.isEmpty()) {
                    String errMessage = "Some of order is not complete because it was booked: ";
                    String err = "";
                    for (String string : errList) {
                        err += ("failed to book " + string + " - ");
                    }
                    request.setAttribute("LIST_ITEM_ERROR", errMessage + err);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
