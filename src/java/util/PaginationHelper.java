/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

// PaginationHelper.java
import dto.DressPhotoCombo;
import dto.Location;
import dto.OrderDetail;
import dto.PhotographyStudio;
import dto.RentalProduct;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class PaginationHelper {

    public static List<OrderDetail> pagingList(List<Location> listLocation, List<RentalProduct> listProduct, List<PhotographyStudio> listStudio, List<DressPhotoCombo> listCombo) {
        List<OrderDetail> listOrder = new ArrayList<>();
        // add location into list order
        for (Location location : listLocation) {
            OrderDetail orderDetail = mapLoationToOrderDetail(location);
            listOrder.add(orderDetail);
        }

        // product
        for (PhotographyStudio studio : listStudio) {
            OrderDetail orderDetail = mapStudioToOrderDetail(studio);
            listOrder.add(orderDetail);
        }

        // studio               
        for (RentalProduct product : listProduct) {
            OrderDetail orderDetail = mapProductToOrderDetail(product);
            listOrder.add(orderDetail);
        }

        // combo               
        for (DressPhotoCombo combo : listCombo) {
            OrderDetail orderDetail = mapComboToOrderDetail(combo);
            listOrder.add(orderDetail);
        }

        return listOrder;
    }

    public static int getCurrentPage(HttpServletRequest request, String paramName, int totalPages) {
        int currentPage = 1; // Default to the first page
        String pageParam = request.getParameter(paramName);
        if (pageParam != null && !pageParam.isEmpty()) {
            currentPage = Integer.parseInt(pageParam);
            if (currentPage < 1) {
                currentPage = 1;
            } else if (currentPage > totalPages) {
                currentPage = totalPages;
            }
        }
        return currentPage;
    }

    public static <T> List<T> getPageEntities(List<T> entityList, int currentPage, int entitiesPerPage) {
        int startIndex = (currentPage - 1) * entitiesPerPage;
        int endIndex = Math.min(startIndex + entitiesPerPage, entityList.size());
        return entityList.subList(startIndex, endIndex);
    }

    public static OrderDetail mapLoationToOrderDetail(Location location) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setName(location.getName());
        orderDetail.setDescription(location.getDescription());
        orderDetail.setPrice(location.getPrice());
        orderDetail.setOrderDate(""); // Set the order date as per your requirements
        orderDetail.setActive(location.isActive());
        orderDetail.setOrderId(0); // Assuming orderId is not available for now
        orderDetail.setItemId(location.getId()); // Map locationId to itemId
        orderDetail.setItemType("location"); // Set the itemType as "location"
        orderDetail.setImage(location.getImage());

        return orderDetail;
    }

    public static OrderDetail mapProductToOrderDetail(RentalProduct product) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setName(product.getName());
        orderDetail.setDescription(product.getDescription());
        orderDetail.setPrice(product.getPrice());
        orderDetail.setOrderDate(""); // Set the order date as per your requirements
        orderDetail.setActive(product.isActive());
        orderDetail.setOrderId(0); // Assuming orderId is not available for now
        orderDetail.setItemId(product.getId()); // Map locationId to itemId
        orderDetail.setItemType("rental_product"); // Set the itemType as "location"
        orderDetail.setImage(product.getImage());

        return orderDetail;
    }

    public static OrderDetail mapStudioToOrderDetail(PhotographyStudio studio) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setName(studio.getName());
        orderDetail.setDescription(studio.getDescription());
        orderDetail.setPrice(studio.getPrice());
        orderDetail.setOrderDate(""); // Set the order date as per your requirements
        orderDetail.setActive(studio.isActive());
        orderDetail.setOrderId(0); // Assuming orderId is not available for now
        orderDetail.setItemId(studio.getId()); // Map locationId to itemId
        orderDetail.setItemType("studio"); // Set the itemType as "location"
        orderDetail.setImage(studio.getImage());

        return orderDetail;
    }

    public static OrderDetail mapComboToOrderDetail(DressPhotoCombo combo) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setName(combo.getComboName());
        orderDetail.setDescription(combo.getComboDescription());
        orderDetail.setPrice(combo.getPrice());
        orderDetail.setOrderDate(""); // Set the order date as per your requirements
        orderDetail.setActive(combo.isActive());
        orderDetail.setOrderId(0); // Assuming orderId is not available for now
        orderDetail.setItemId(combo.getId()); // Map locationId to itemId
        orderDetail.setItemType("combo"); // Set the itemType as "location"
        orderDetail.setImage(combo.getImage());

        return orderDetail;
    }

}
