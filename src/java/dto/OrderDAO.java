/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import dao.*;
import dto.Location;
import dto.Order;
import java.io.Serializable;
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
public class OrderDAO implements Serializable {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public boolean insertOrder(Order order) throws NamingException, SQLException {

        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "insert dbo.orders(profile_id,order_date,status)\n"
                        + "values (?, ?, 'create')";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, order.getProfileId());
                pst.setString(2, order.getOrderDate());

                int result = pst.executeUpdate();

                if (result > 0) {
                    return true;
                }

            }
        } finally {

            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return false;
    }

    public Order getLastOrder() throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select top 1 order_id,profile_id,order_date,status\n"
                        + "from orders\n"
                        + "order by order_id desc";

                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                if (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    int profileId = rs.getInt("profile_id");
                    String orderDate = rs.getString("order_date");
                    String status = rs.getString("status");

                    return new Order(orderId, profileId, orderDate, status);
                }

            }
        } finally {

            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return null;
    }

    public Order getOrderByUserId(int userId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select order_id,profile_id,order_date,status\n"
                        + "from orders\n"
                        + "where profile_id = ? and status = 'create'";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, userId);

                rs = pst.executeQuery();

                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    int profileId = rs.getInt("profile_id");
                    String orderDate = rs.getString("order_date");
                    String status = rs.getString("status");

                    return new Order(orderId, profileId, orderDate, status);
                }

            }
        } finally {

            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return null;
    }

    public List<Order> getAllOrder() throws NamingException, SQLException {
        List<Order> list = new ArrayList();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select order_id,profile_id,order_date,status\n"
                        + "from orders\n"
                        + "where status = 'pending'";

                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();

                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    int profileId = rs.getInt("profile_id");
                    String orderDate = rs.getString("order_date");
                    String status = rs.getString("status");

                    list.add(new Order(orderId, profileId, orderDate, status));
                }

            }
        } finally {

            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return list;
    }

    public boolean deleteOrderById(int orderId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "update orders\n"
                        + "set status = 'cancel'\n"
                        + "where order_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, orderId);

                int result = pst.executeUpdate();

                if (result > 0) {
                    return true;
                }
            }
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return false;
    }

    public boolean removeOrderById(int orderId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "delete orders\n"
                        + "where order_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, orderId);

                int result = pst.executeUpdate();

                if (result > 0) {
                    return true;
                }
            }
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return false;
    }

    public boolean confirmOrderById(int orderId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "update orders\n"
                        + "set status = 'confirm'\n"
                        + "where order_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, orderId);

                int result = pst.executeUpdate();

                if (result > 0) {
                    return true;
                }
            }
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return false;
    }
    
    public boolean paymentOrder(Order order) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "update orders\n"
                        + "set amount = ?, order_date = ?, status = 'pending'\n"
                        + "where order_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setFloat(1, (float) order.getAmount());
                pst.setString(2, order.getOrderDate());
                pst.setInt(3, order.getOrderId());

                int result = pst.executeUpdate();

                if (result > 0) {
                    return true;
                }
            }
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return false;
    }

    public Order getOrderByProfileId(int profileId) throws NamingException, SQLException {

        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select order_id,profile_id,order_date,status\n"
                        + "from orders\n"
                        + "where status = 'create' and profile_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, profileId);
                rs = pst.executeQuery();

                if (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    String orderDate = rs.getString("order_date");
                    String status = rs.getString("status");

                    return new Order(orderId, profileId, orderDate, status);
                }

            }
        } finally {

            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return null;
    }

    public Order getOrderById(int orderId) throws NamingException, SQLException {

        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select order_id,profile_id,order_date,status\n"
                        + "from orders\n"
                        + "where status = 'create' and order_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, orderId);
                rs = pst.executeQuery();

                if (rs.next()) {
                    String orderDate = rs.getString("order_date");
                    String status = rs.getString("status");
                    int profileId = rs.getInt("profile_id");

                    return new Order(orderId, profileId, orderDate, status);
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

        return null;
    }

}
