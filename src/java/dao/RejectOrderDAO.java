/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.OrderDetail;
import dto.PhotographyStudio;
import dto.RejectOrder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import util.ConnectionConfig;

/**
 *
 * @author ptd
 */
public class RejectOrderDAO {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public boolean saveRejectOrder(RejectOrder rejectOrder) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "insert into dbo.reject_order(name, description, price, order_date, order_start_date, order_end_date, profile_id, status, item_type, item_id)\n"
                        + "values (?,?,?,?,?,?,?,?,?,?)";

                pst = conn.prepareStatement(sql);
                pst.setString(1, rejectOrder.getName());
                pst.setString(2, rejectOrder.getDescription());
                pst.setDouble(3, rejectOrder.getPrice());
                pst.setString(4, rejectOrder.getOrderDate());
                pst.setString(5, rejectOrder.getOrderStartDate());
                pst.setString(6, rejectOrder.getOrderEndDate());
                pst.setInt(7, rejectOrder.getProfileId());
                pst.setString(8, rejectOrder.getStatus());
                pst.setString(9, rejectOrder.getItemType());
                pst.setInt(10, rejectOrder.getItemId());

                int rs = pst.executeUpdate();
                if (rs > 0) {
                    return true;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return false;
    }

    public List<Integer> getListRejectByItemId(int itemId) throws NamingException, SQLException {
        List<Integer> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select reject_id\n"
                        + "from reject_order\n"
                        + "where item_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, itemId);

                rs = pst.executeQuery();
                while (rs.next()) {
                    int rejectId = rs.getInt("reject_id");
                    list.add(rejectId);

                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return list;
    }

    public boolean updateRejectOrder(int rejectId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "update reject_order set status = 'confirm' where reject_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, rejectId);

                int rs = pst.executeUpdate();
                if (rs > 0) {
                    return true;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return false;
    }

    public List<RejectOrder> getListRejectByProfileId(int profileId) throws NamingException, SQLException {
        List<RejectOrder> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select reject_id, name, description, price, order_date, order_start_date, order_end_date, status, profile_id, item_type, item_id\n"
                        + "from reject_order\n"
                        + "where profile_id = ? and status = 'reject'";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, profileId);

                rs = pst.executeQuery();
                while (rs.next()) {
                    String status = rs.getString("status");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String orderDate = rs.getString("order_date");
                    double price = rs.getDouble("price");
                    String orderStartDate = rs.getString("order_start_date");
                    String orderEndDate = rs.getString("order_end_date");
                    int rejectId = rs.getInt("reject_id");
                    int itemId = rs.getInt("item_id");
                    String itemType = rs.getString("item_type");
                    RejectOrder reject = new RejectOrder(rejectId, status, name, description, price, orderDate, orderStartDate, orderEndDate, profileId, itemType, itemId);
                    list.add(reject);

                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return list;
    }
}
