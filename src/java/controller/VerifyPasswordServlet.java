/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import dto.Profile;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ptd
 */
@WebServlet(name = "VerifyPasswordServlet", urlPatterns = {"/VerifyPasswordServlet"})
public class VerifyPasswordServlet extends HttpServlet {

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

        String url = "forgotPassword.jsp";
        String otp = request.getParameter("otp");
        HttpSession session = request.getSession();
        AccountDAO dao = new AccountDAO();
        Profile profile = (Profile) session.getAttribute("USER");

        String optSystem = (String) session.getAttribute("OTP");
        try {
            if (profile != null) {
                url = "profile.jsp";
                if (optSystem.equals(otp)) {
                    int userId = profile.getProfileId();
                    String password = (String) session.getAttribute("PASSWORD");

                    boolean result = dao.changePassword(userId, password);
                    if (result) {
                       
                        session.removeAttribute("USER_ID_CHANGE_PASSWORD");
                        url = "home.jsp";
                    }
                }
            } else {
                if (optSystem.equals(otp)) {
                    int userId = (int) session.getAttribute("USER_ID");
                    String password = (String) session.getAttribute("PASSWORD");
                    
                    boolean result = dao.changePassword(userId, password);
                    if (result) {
                         session.removeAttribute("USER_ID");
                        url = "home.jsp";
                    }
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
