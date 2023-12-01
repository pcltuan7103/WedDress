/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.LocationDAO;
import dto.Location;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
@WebServlet(name = "UpdateLocationServlet", urlPatterns = {"/UpdateLocationServlet"})
public class UpdateLocationServlet extends HttpServlet {

    public final String ERROR_PAGE = "error.jsp";
    public final String PHOTO_HOME_PAGE = "photoHome.jsp";

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
        String id = request.getParameter("txtLocationId");
        String name = request.getParameter("txtLocationName");
        String description = request.getParameter("txtLocationDescription");
        String price = request.getParameter("txtLocationPrice");
        String image = request.getParameter("txtLocationImage");

        LocationDAO dao = new LocationDAO();
        try {
            if (!id.isEmpty() && !price.isEmpty() && Utilities.isPositiveNumber(price)) {
                HttpSession session = request.getSession();
                boolean result = dao.updateLocation(new Location(Integer.parseInt(id), name, description, Double.parseDouble(price), image,true));
                if (result) {
                    List<Location> listLocation = dao.getAllLocation();
                    session.setAttribute("LOCATIONS", listLocation);
                    url = "admin.jsp";
                }
            }
        } catch (NamingException ex) {
            log("UpdatePhotoServlet_NamingException: " + ex.getMessage());
        } catch (SQLException ex) {
            log("UpdatePhotoServlet_SQLException " + ex.getMessage());
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
