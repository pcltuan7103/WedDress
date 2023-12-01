/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.Profile;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.paypal.api.payments.*;
import com.paypal.base.rest.*;
import dao.DressPhotoComboDAO;
import dao.LocationDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.PhotoScheduleDAO;
import dao.PhotographyStudiosDAO;
import dto.Location;
import dto.OrderDetail;
import dto.PhotoSchedule;
import dto.PhotographyStudio;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import util.Contant;

/**
 *
 * @author ptd
 */
@WebServlet(name = "PaymentServlet", urlPatterns = {"/PaymentServlet"})
public class PaymentServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

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
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        processRequest(req, res);

        HttpSession session = req.getSession();
        Profile profile = (Profile) session.getAttribute("USER");
        String orderId = req.getParameter("idOrder");
        String amountS = req.getParameter("amount");

        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        LocationDAO locationDAO = new LocationDAO();
        DressPhotoComboDAO comboDAO = new DressPhotoComboDAO();
        PhotographyStudiosDAO studioDAO = new PhotographyStudiosDAO();
        PhotoScheduleDAO scheduleDAO = new PhotoScheduleDAO();

        if (profile != null) {
            try {
                //            String vnp_Version = "2.1.0";
//            String vnp_Command = "pay";
//
//            // Remove any commas and convert to long
//            String bankCode = req.getParameter("bankCode");
//
//            String vnp_TxnRef = Config.getRandomNumber(8);
//            String vnp_IpAddr = Config.getIpAddress(req);
//            String vnp_TmnCode = Config.vnp_TmnCode;
//
//            Map<String, String> vnp_Params = new HashMap<>();
//            vnp_Params.put("vnp_Version", vnp_Version);
//            vnp_Params.put("vnp_Command", vnp_Command);
//            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
////            vnp_Params.put("vnp_BankCode", "NCB");
//            vnp_Params.put("vnp_OrderType", "topup");
//
//            Double price = Double.parseDouble(amountS);
//            vnp_Params.put("vnp_Amount", String.valueOf(price.longValue() * 1000));
//            vnp_Params.put("vnp_CurrCode", "VND");
//            if (bankCode != null && !bankCode.isEmpty()) {
//                vnp_Params.put("vnp_BankCode", bankCode);
//            }
//
//            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//            vnp_Params.put("vnp_OrderInfo", String.valueOf(profile.getProfileId()));
//            String locate = req.getParameter("language");
//            if (locate != null && !locate.isEmpty()) {
//                vnp_Params.put("vnp_Locale", locate);
//            } else {
//                vnp_Params.put("vnp_Locale", "vn");
//            }
//
//            vnp_Params.put("vnp_ReturnUrl", Config.vnp_Returnurl);
//            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//
//            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//            String vnp_CreateDate = formatter.format(cld.getTime());
//            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//
//            cld.add(Calendar.MINUTE, 15);
//            String vnp_ExpireDate = formatter.format(cld.getTime());
//            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//
//            List fieldNames = new ArrayList(vnp_Params.keySet());
//            Collections.sort(fieldNames);
//            StringBuilder hashData = new StringBuilder();
//            StringBuilder query = new StringBuilder();
//            Iterator itr = fieldNames.iterator();
//            while (itr.hasNext()) {
//                String fieldName = (String) itr.next();
//                String fieldValue = (String) vnp_Params.get(fieldName);
//                if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                    //Build hash data
//                    hashData.append(fieldName);
//                    hashData.append('=');
//                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                    //Build query
//                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
//                    query.append('=');
//                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                    if (itr.hasNext()) {
//                        query.append('&');
//                        hashData.append('&');
//                    }
//                }
//            }
//            String queryUrl = query.toString();
//            String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
//            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//            String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
//            System.out.println(paymentUrl);
//            res.sendRedirect(paymentUrl);

                // Lấy tất cả order-detail trong cart của user hiện tại
                List<OrderDetail> listOrder = orderDetailDAO.getOrderDetailByOrderId(Integer.parseInt(orderId));

                // bỏ item vào transaction để paypal biết những item nào và id nào
                Transaction listTransaction = getTransactionInformation(listOrder);

                // api của paypal bao gồm key và secret 
                APIContext apiContext = new APIContext(Contant.CLIENT_ID, Contant.CLIENT_SECRET, Contant.CLIENT_MODE);

                // tạo payment trên paypal
                Payment payment = new Payment();
                // intent này bắt buộc phải có nhưng k nhất thiết phải là sale -> a để sale cho vui thoy
                payment.setIntent("sale");

                // thông tin của ng thanh toán 
                // thường cái này sẽ hiển thị lúc e thanh toán nhưng mà e đang xài account test của a nên nó sẽ hiển thị tên của a thay vì payer 
                Payer payer = getPayerInformation(profile);
                payment.setPayer(payer);

                // redirect url để paypal biết sau khi cancel thì về page nào của mình or sau khi thanh toán thành công nó sẽ return lại page nào của mình
                // ctrl + click vào hàm getRedirectUrl để biết thêm chi tiết
                RedirectUrls redirectUrls = getRedirectURLs();
                
                // set redirect url và transaction bên trên vào payment
                payment.setRedirectUrls(redirectUrls);
                payment.setTransactions(Collections.singletonList(listTransaction)); // Set a single transaction

                // tạo payment bằng thông qua api của paypal
                Payment approvedPayment = payment.create(apiContext);

                //trong approvedPayment sau khi khởi tạo sẽ có link để redirect ( nó là trang paypal mà mấy e thao tác )
                String link = getApprovalLink(approvedPayment);
                
                // redirect đi thoy
                res.sendRedirect(link);

            } catch (PayPalRESTException ex) {
                log("PaymentServlet_Paypal_error: " + ex.getMessage());
            } catch (NamingException ex) {
                log("PaymentServlet_NamingException_error: " + ex.getMessage());
            } catch (SQLException ex) {
                log("PaymentServlet_SQLException_error: " + ex.getMessage());
            }

        }
    }

    // bỏ tất cả item trong cart vào transaction
    private Transaction getTransactionInformation(List<OrderDetail> orderDetails) {
        LocationDAO locationDAO = new LocationDAO();
        DressPhotoComboDAO comboDAO = new DressPhotoComboDAO();
        PhotographyStudiosDAO studioDAO = new PhotographyStudiosDAO();
        PhotoScheduleDAO scheduleDAO = new PhotoScheduleDAO();
        List<Item> items = new ArrayList<>();
        ItemList itemList = new ItemList();
        Transaction transaction = new Transaction();
        Amount amount = new Amount();
        
        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetail.getItemType().equals("photo_schedule")) {
                try {
                    PhotoSchedule schedule = scheduleDAO.getPhotoScheduleById(orderDetail.getItemId());
                    PhotographyStudio studio = studioDAO.getStudioById(schedule.getStudioId());
                    Location location = locationDAO.getLocationById(schedule.getLocationId());

                    transaction.setAmount(amount);
                    transaction.setDescription(location.getName() + studio.getName());

                    Item item1 = new Item();
                    item1.setCurrency("USD");
                    item1.setName(location.getName());
                    item1.setPrice(location.getPrice() + "");
                    item1.setSku(orderDetail.getOrderId() + "");
                    item1.setQuantity("1");

                    Item item2 = new Item();
                    item2.setCurrency("USD");
                    item2.setName(studio.getName());
                    item2.setPrice(studio.getPrice() + "");
                    item2.setSku(orderDetail.getOrderId() + "");
                    item2.setQuantity("1");

                    items.add(item1);
                    items.add(item2);
                } catch (NamingException ex) {
                    Logger.getLogger(PaymentServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(PaymentServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {

                transaction.setAmount(amount);
                transaction.setDescription(orderDetail.getName());

                Item item = new Item();
                item.setCurrency("USD");
                item.setName(orderDetail.getName());
                item.setPrice(orderDetail.getPrice() + "");
                item.setSku(orderDetail.getOrderId() + "");
                item.setQuantity("1");

                items.add(item);
            }
        }

        itemList.setItems(items);
        transaction.setItemList(itemList);

// Calculate the total amount from the items
        double totalAmount = items.stream()
                .mapToDouble(item -> Double.parseDouble(item.getPrice()))
                .sum();
        amount.setCurrency("USD");
        amount.setTotal(String.valueOf(totalAmount));

        transaction.setAmount(amount);

        return transaction;
    }

    private Payer getPayerInformation(Profile p) {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName(p.getFirstName())
                .setLastName(p.getLastName())
                .setEmail(p.getEmail());

        payer.setPayerInfo(payerInfo);

        return payer;
    }

    private RedirectUrls getRedirectURLs() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/wedding_photography/cancel.html");
        redirectUrls.setReturnUrl("http://localhost:8080/wedding_photography/paymentConfirm");

        return redirectUrls;
    }

    private String getApprovalLink(Payment approvedPayment) {
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;

        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }

        return approvalLink;
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
