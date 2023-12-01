/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.RejectOrderDAO;
import dto.Profile;
import dto.RejectOrder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@WebServlet(name = "RejectConfirmServlet", urlPatterns = {"/RejectConfirmServlet"})
public class RejectConfirmServlet extends HttpServlet {

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

        String rejectId = request.getParameter("rejectId");
        String itemId = request.getParameter("itemId");
        String url = "reject.jsp";
        RejectOrderDAO rejectDAO = new RejectOrderDAO();

        HttpSession session = request.getSession();
        Profile profile = (Profile) session.getAttribute("USER");

        try {
            boolean check = false;
            List<Integer> listInt = rejectDAO.getListRejectByItemId(Integer.parseInt(itemId));
            for (Integer i : listInt) {
                boolean result = rejectDAO.updateRejectOrder(i);
                if (result) {
                    check = true;
                }
            }

            if (check) {
                List<RejectOrder> listReject = rejectDAO.getListRejectByProfileId(profile.getProfileId());
                List<RejectOrder> listProduct = new ArrayList<>();

                Map<String, List<RejectOrder>> listSchedule = Utilities.groupOrderReject(listReject);

                for (RejectOrder detail : listReject) {
                    //item_id and item_type --> add schedule photo
                    if (!detail.getItemType().equals("photo_schedule-location") && !detail.getItemType().equals("photo_schedule-studio")) {
                        listProduct.add(detail);
                    }
                }
                session.setAttribute("LIST_REJECT_SCHEDULE", listSchedule);
                session.setAttribute("LIST_REJECT_PRODUCT", listProduct);

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
