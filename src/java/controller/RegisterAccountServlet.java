/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.AccountDAO;
import dto.Profile;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
@WebServlet(name = "RegisterAccountServlet", urlPatterns = {"/RegisterAccountServlet"})
public class RegisterAccountServlet extends HttpServlet {

    public final String ERROR_PAGE = "error.jsp";
    public final String HOME_PAGE = "DispatcherServlet?btAction=Home";

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

    }

    // Method to generate a random OTP
    private static String generateOTP(int length) {
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(numbers.length());
            otp.append(numbers.charAt(index));
        }

        return otp.toString();
    }

    // Method to send the OTP via email
    private void sendOTPEmail(String recipientEmail, String otp) throws MessagingException {
        // SMTP server configuration
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // SMTP server authentication credentials
        String username = "cunplong.1@gmail.com";
        String password = "jioywrlndbjmfhda";

        // Create a session with SMTP server
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Create an email message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("awnsshop@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP for password reset is: " + otp);

        // Send the email
        Transport.send(message);
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
        String otp = request.getParameter("otp");
        String url = "otpConfirm.jsp";
        try {
            HttpSession session = request.getSession();
            String otpSystem = (String) session.getAttribute("OTP");
            if (otp.equals(otpSystem)) {
                Profile profile = (Profile) session.getAttribute("USER_REGISTER");
                AccountDAO dao = new AccountDAO();
                boolean result = dao.insertProfifle(profile);
                if (result) {
                    session.removeAttribute("OTP");
                    session.removeAttribute("USER_REGISTER");
                    url = HOME_PAGE;
                }
            }

        } catch (NamingException ex) {
            Logger.getLogger(RegisterAccountServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RegisterAccountServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
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
        String url = ERROR_PAGE;

        String userName = request.getParameter("txtUserName");
//        String firstName = request.getParameter("txtFirstName");
//        String lastName = request.getParameter("txtLastName");
        String email = request.getParameter("txtEmail");
//        String phone = request.getParameter("txtPhone");
//        String address = request.getParameter("txtAddress");
        String roleId = request.getParameter("roleId");

        String passwordDefault = request.getParameter("txtPassword");
        AccountDAO dao = new AccountDAO();
        HttpSession session = request.getSession();

        try {
            boolean checkUserName = dao.checkValidUsername(userName, email);
            boolean checkValidEmail = Utilities.isValidEmail(email);
            if (!checkUserName && checkValidEmail) {
                String otp = generateOTP(6);
                session.setAttribute("OTP", otp);

                Profile profileTmp = new Profile("", "", email, "", "", userName, 2, passwordDefault);
                session.setAttribute("USER_REGISTER", profileTmp);
                sendOTPEmail(profileTmp.getEmail(), otp);
                url = "otpConfirm.jsp";
//                request.getRequestDispatcher("otpConfirm.jsp").forward(request, response);

            } else {
                url = "register.jsp";
                request.setAttribute("ERROR_USER_NAME", "error user name");
            }

        } catch (NamingException ex) {
            log("DispatcherServlet_NamingException: " + ex.getMessage());
        } catch (SQLException ex) {
            log("DispatcherServlet_SQLException " + ex.getMessage());
        } catch (MessagingException ex) {
            Logger.getLogger(RegisterAccountServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
//            response.sendRedirect(url);
        }
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
