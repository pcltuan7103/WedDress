/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dto.OrderDetail;
import dto.PhotoSchedule;
import dto.RejectOrder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author ptd
 */
public class Utilities {

    public static boolean isPositiveNumber(String str) {
        Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String calculateHMac(String data, String algorithm, String key) throws Exception {
        Mac Hmac = Mac.getInstance(algorithm);
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
        Hmac.init(secret_key);

        return byteArrayToHex(Hmac.doFinal(data.getBytes("UTF-8")));
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static String getCurrentDateByFormat(String format) {
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String vnp_CreateDate = formatter.format(cld.getTime());

        return vnp_CreateDate;
    }

    public static String getExpireDate(String format) {
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        cld.add(Calendar.MINUTE, 15);
        return formatter.format(cld.getTime());
    }

    public static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    public static Map<String, List<OrderDetail>> groupOrderDetails(List<OrderDetail> orderDetails) {
        Map<String, List<OrderDetail>> groupedMap = new HashMap<>();

        orderDetails.forEach((orderDetail) -> {
            String[] arr = orderDetail.getItemType().split("-");
            if (arr.length > 1) {
                String key = arr[0] + "-" + orderDetail.getItemId();

                if (groupedMap.containsKey(key)) {
                    groupedMap.get(key).add(orderDetail);
                } else {
                    List<OrderDetail> groupedList = new ArrayList<>();
                    groupedList.add(orderDetail);
                    groupedMap.put(key, groupedList);
                }
            }
        });

        return groupedMap;
    }

    public static Map<String, List<RejectOrder>> groupOrderReject(List<RejectOrder> orderDetails) {
        Map<String, List<RejectOrder>> groupedMap = new HashMap<>();

        orderDetails.forEach((orderDetail) -> {
            String[] arr = orderDetail.getItemType().split("-");
            if (arr.length > 1) {
                String key = arr[0] + "-" + orderDetail.getItemId();

                if (groupedMap.containsKey(key)) {
                    groupedMap.get(key).add(orderDetail);
                } else {
                    List<RejectOrder> groupedList = new ArrayList<>();
                    groupedList.add(orderDetail);
                    groupedMap.put(key, groupedList);
                }
            }
        });

        return groupedMap;
    }

    public static void groupOrderDetails(List<OrderDetail> orderDetails, Map<String, List<OrderDetail>> groupedMap, String orderStatus) {

        orderDetails.forEach((orderDetail) -> {
            String[] arr = orderDetail.getItemType().split("-");
            if (arr.length > 1) {
                String key = arr[0] + "-" + orderDetail.getItemId();

                if (groupedMap.containsKey(key)) {
                    orderDetail.setStatus(orderStatus);
                    groupedMap.get(key).add(orderDetail);
                } else {
                    List<OrderDetail> groupedList = new ArrayList<>();
                    orderDetail.setStatus(orderStatus);
                    groupedList.add(orderDetail);
                    groupedMap.put(key, groupedList);
                }
            }
        });
    }

    public static void groupOrderDetailsAdmin(List<OrderDetail> orderDetails, Map<String, List<OrderDetail>> groupedMap, String orderStatus, int itemId) {
        orderDetails.forEach((orderDetail) -> {
            String[] arr = orderDetail.getItemType().split("-");
            if (arr.length > 1) {
                String key = arr[0] + "-" + orderDetail.getItemId();

                if (groupedMap.containsKey(key)) {
                    if (itemId == orderDetail.getItemId()) {
                        orderDetail.setStatus("confirm");
                    } else {
                        orderDetail.setStatus(orderStatus);
                    }
                    groupedMap.get(key).add(orderDetail);
                } else {
                    List<OrderDetail> groupedList = new ArrayList<>();
                    groupedList.add(orderDetail);
                    groupedMap.put(key, groupedList);
                    if (itemId == orderDetail.getItemId()) {
                        orderDetail.setStatus("confirm");
                    } else {
                        orderDetail.setStatus(orderStatus);
                    }
                }
            }
        });
    }

    public static void groupOrderDetailsAdminLoaded(List<OrderDetail> orderDetails, Map<String, List<OrderDetail>> groupedMap, List<PhotoSchedule> photoIdList) {
        orderDetails.forEach((orderDetail) -> {
            String[] arr = orderDetail.getItemType().split("-");
            if (arr.length > 1) {
                String key = arr[0] + "-" + orderDetail.getItemId();
                photoIdList.forEach((p -> {
                    if (p.getScheduleId() == orderDetail.getItemId()) {
                        if (groupedMap.containsKey(key)) {
                            groupedMap.get(key).add(orderDetail);
                            orderDetail.setStatus(p.getStatus());
                        } else {
                            List<OrderDetail> groupedList = new ArrayList<>();
                            groupedList.add(orderDetail);
                            groupedMap.put(key, groupedList);
                            orderDetail.setStatus(p.getStatus());
                        }
                    }
                }));

            }
        });
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidPhone(String email) {
        String emailRegex = "^[-0-9]$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    

    public static String convertTimeZoneToISO120(String time) {
        return time.replace("T", " ");
    }

    public static boolean areValidStrings(int maxLength, String... strings) {
        // Define a regular expression pattern for alphanumeric characters and a maximum length
        String regexPattern = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9\\s]{1," +maxLength+"}$|^[a-zA-Z\\s]{1,"+maxLength+"}$";
        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile(regexPattern);
        for (String str : strings) {
            // Create a Matcher to match the input string against the pattern
            Matcher matcher = pattern.matcher(str);
            // Check if the input string matches the pattern and its length is within the specified maximum
            if (!matcher.matches() || str.length() > maxLength) {
                return false; // Return false if any string is invalid
            }
        }

        return true; // All strings are valid
    }

}
