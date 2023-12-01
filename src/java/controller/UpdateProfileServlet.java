/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.AccountDAO;
import dto.Profile;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import util.Utilities;

/**
 *
 * @author ptd
 */
@WebServlet(name = "UpdateProfileServlet", urlPatterns = {"/UpdateProfileServlet"})
public class UpdateProfileServlet extends HttpServlet {

    public final String PROFILE_PAGE = "profile.jsp";

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

        String url = PROFILE_PAGE;
        String profileId = request.getParameter("txtId");
        String firstName = request.getParameter("txtFirstName");
        String lastName = request.getParameter("txtLastname");
        String password = request.getParameter("txtPassword");
        String phoneNumber = request.getParameter("txtPhoneNumber");
        String address = request.getParameter("txtAddress");
        String email = request.getParameter("txtEmail");

        HttpSession session = request.getSession();
        AccountDAO dao = new AccountDAO();
        if (session != null) {
            Profile profile = (Profile) session.getAttribute("USER");
            if (Utilities.areValidStrings(20, firstName, lastName, password, address) || Utilities.isValidEmail(email) || Utilities.isValidPhone(phoneNumber)) {
                // All input strings are valid, proceed with the profile update
                if (password.equals(profile.getPassword())) {
                    if (profile != null) {
                        try {
                            firstName = request.getParameter("txtFirstName").isEmpty() ? profile.getFirstName() : request.getParameter("txtFirstName");
                            lastName = request.getParameter("txtLastname").isEmpty() ? profile.getLastName() : request.getParameter("txtLastname");
                            password = request.getParameter("txtPassword").isEmpty() ? profile.getPassword() : request.getParameter("txtPassword");
                            phoneNumber = request.getParameter("txtPhoneNumber").isEmpty() ? profile.getPhoneNumber() : request.getParameter("txtPhoneNumber");
                            address = request.getParameter("txtAddress").isEmpty() ? profile.getAddress() : request.getParameter("txtAddress");
                            email = request.getParameter("txtEmail").isEmpty() ? profile.getEmail() : request.getParameter("txtEmail");

                            Profile p = new Profile(Integer.parseInt(profileId), firstName, lastName, email, phoneNumber, address, profile.getRoleName(), true, profile.getUserName(), password);
                            boolean result = dao.updateProfile(p);
                            if (result) {
                                session.setAttribute("USER", p);
                            }
                        } catch (NamingException ex) {
                            log("UpdateProfileServlet_NamingException: " + ex.getMessage());
                        } catch (SQLException ex) {
                            log("UpdateProfileServlet_SQLException " + ex.getMessage());
                        } finally {
                            RequestDispatcher dispatcher = request.getRequestDispatcher(url);
                            dispatcher.forward(request, response);
                        }
                    }

                } else {
                    try {
                        String otp = generateOTP(6);
                        session.setAttribute("OTP", otp);
                        Profile profileTmp = new Profile(profile.getProfileId(), firstName, lastName, email, phoneNumber, address, profile.getUserName(), true, profile.getUserName(), password);
                        session.setAttribute("USER_TMP", profileTmp);
                        sendOTPEmail(profile.getEmail(), otp);
                        response.sendRedirect("otpConfirm.jsp");
                    } catch (MessagingException ex) {
                        Logger.getLogger(UpdateProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                request.setAttribute("INVALID_FIELD", "Pls double-check, the string should be character and digits, max length is 20");
                RequestDispatcher dispatcher = request.getRequestDispatcher(url);
                dispatcher.forward(request, response);
            }
        }

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
