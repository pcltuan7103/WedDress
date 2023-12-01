/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Location;
import dto.RentalProduct;
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
public class RentalProductDAO implements Serializable {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public List<RentalProduct> getAllRentalProduct() throws NamingException, SQLException {
        List<RentalProduct> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select product_id, product_name,description,price,image,is_active,stock\n"
                        + "from rental_products\n" + "where is_active = 1 and stock > 0";

                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("product_id");
                    String name = rs.getString("product_name");
                    String description = rs.getString("description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    int stock = rs.getInt("stock");
                    boolean active = rs.getBoolean("is_active");
                    list.add(new RentalProduct(id, name, description, price, image, stock, active));
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

    public List<RentalProduct> getSearchRentalProduct(String searchValue) throws NamingException, SQLException {
        List<RentalProduct> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select product_id, product_name,description,price,image,is_active\n"
                        + "from rental_products\n"
                        + "where product_name LIKE ? and is_active = 1";

                pst = conn.prepareStatement(sql);
                pst.setString(1, "%" + searchValue + "%");
                rs = pst.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("product_id");
                    String name = rs.getString("product_name");
                    String description = rs.getString("description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    list.add(new RentalProduct(id, name, description, price, image, active));
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

    public List<RentalProduct> getFilterRentalProduct(int min, int max) throws NamingException, SQLException {
        List<RentalProduct> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select product_id, product_name,description,price,image,is_active\n"
                        + "from rental_products\n"
                        + "where price > ? and price < ? and is_active = 1";

                pst = conn.prepareStatement(sql);

                pst.setInt(1, min);
                pst.setInt(2, max);

                rs = pst.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("product_id");
                    String name = rs.getString("product_name");
                    String description = rs.getString("description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    list.add(new RentalProduct(id, name, description, price, image, active));
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

    public RentalProduct getRentalProductById(int productId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select product_id, product_name,description,price,image,is_active, stock\n"
                        + "from rental_products\n"
                        + "where is_active = 1 and stock > 0 and product_id = ?";

                pst = conn.prepareStatement(sql);

                pst.setInt(1, productId);

                rs = pst.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("product_id");
                    int stock = rs.getInt("stock");
                    String name = rs.getString("product_name");
                    String description = rs.getString("description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    
                    return new RentalProduct(id, name, description, price, image, stock, active);
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

    public boolean insertRentalProduct(RentalProduct rentalProduct) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "insert into rental_products(product_name,description,price,image,stock,is_active)\n"
                        + "values (?,?,?,?,?,1)";

                pst = conn.prepareStatement(sql);
                pst.setString(1, rentalProduct.getName());
                pst.setString(2, rentalProduct.getDescription());
                pst.setFloat(3, (float) rentalProduct.getPrice());
                pst.setString(4, rentalProduct.getImage());
                pst.setInt(5, rentalProduct.getStock());
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

    public boolean updateRentalProduct(RentalProduct rentalProduct) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "update rental_products\n"
                        + "set product_name = ?, description = ?, price =?, image = ?\n"
                        + "where product_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setString(1, rentalProduct.getName());
                pst.setString(2, rentalProduct.getDescription());
                pst.setFloat(3, (float) rentalProduct.getPrice());
                pst.setString(4, rentalProduct.getImage());
                pst.setInt(5, rentalProduct.getId());
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
    
     public boolean setStockRentalProduct(int productId, int stock) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "update rental_products\n"
                        + "set stock = ?\n"
                        + "where product_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, stock);
                pst.setInt(2, productId);
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

    public boolean deleteRentalProduct(int productId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "update rental_products\n"
                        + "set is_active = 0\n"
                        + "where product_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, productId);
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
}
