/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.DressPhotoCombo;
import dto.Location;
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
public class DressPhotoComboDAO implements Serializable {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public List<DressPhotoCombo> getAllDressPhotoCombo() throws NamingException, SQLException {
        List<DressPhotoCombo> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select id, combo_name, combo_description,dress_id,photo_studio_id,price,image,is_active,stock\n"
                        + "from dress_and_photo_combo\n"
                        + "where is_active = 1 and stock > 0";

                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String comboName = rs.getString("combo_name");
                    String comboDescription = rs.getString("combo_description");
                    int dressId = rs.getInt("dress_id");
                    int stock = rs.getInt("stock");
                    int photoStudioId = rs.getInt("photo_studio_id");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    list.add(new DressPhotoCombo(id, comboName, comboDescription, dressId, photoStudioId, price, image, stock, active));
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

    public List<DressPhotoCombo> getSearchCombo(String searchValue) throws NamingException, SQLException {
        List<DressPhotoCombo> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select id, combo_name, combo_description,dress_id,photo_studio_id,price,image,is_active\n"
                        + "from dress_and_photo_combo\n"
                        + "where combo_name LIKE ? and is_active = 1";

                pst = conn.prepareStatement(sql);
                pst.setString(1, "%" + searchValue + "%");
                rs = pst.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int dressId = rs.getInt("dress_id");
                    int studioId = rs.getInt("photo_studio_id");
                    String name = rs.getString("combo_name");
                    String description = rs.getString("combo_description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    list.add(new DressPhotoCombo(id, name, description, dressId, studioId, price, image, active));
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

    public List<DressPhotoCombo> getFilterCombo(int min, int max) throws NamingException, SQLException {
        List<DressPhotoCombo> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select id, combo_name, combo_description,dress_id,photo_studio_id,price,image,is_active\n"
                        + "from dress_and_photo_combo\n"
                        + "where price > ? and price < ? and is_active = 1";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, min);
                pst.setInt(2, max);

                rs = pst.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int dressId = rs.getInt("dress_id");
                    int studioId = rs.getInt("photo_studio_id");
                    String name = rs.getString("combo_name");
                    String description = rs.getString("combo_description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    list.add(new DressPhotoCombo(id, name, description, dressId, studioId, price, image, active));
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

    public DressPhotoCombo getComboById(int comboId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select id, combo_name, combo_description,dress_id,photo_studio_id,price,image,is_active, stock\n"
                        + "from dress_and_photo_combo\n"
                        + "where is_active = 1 and stock > 0 and id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, comboId);

                rs = pst.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int dressId = rs.getInt("dress_id");
                    int studioId = rs.getInt("photo_studio_id");
                    int stock = rs.getInt("stock");
                    String name = rs.getString("combo_name");
                    String description = rs.getString("combo_description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    return new DressPhotoCombo(id, name, description, dressId, studioId, price, image, stock, active);
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
    
    public DressPhotoCombo getComboByIdDelete(int comboId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select id, combo_name, combo_description,dress_id,photo_studio_id,price,image,is_active, stock\n"
                        + "from dress_and_photo_combo\n"
                        + "where is_active = 1 and stock >= 0 and id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, comboId);

                rs = pst.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int dressId = rs.getInt("dress_id");
                    int studioId = rs.getInt("photo_studio_id");
                    int stock = rs.getInt("stock");
                    String name = rs.getString("combo_name");
                    String description = rs.getString("combo_description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    return new DressPhotoCombo(id, name, description, dressId, studioId, price, image, stock, active);
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

    public boolean setStockCombo(int comboId, int stock) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "update dress_and_photo_combo\n"
                        + "set stock = ?\n"
                        + "where id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, stock);
                pst.setInt(2, comboId);

                int rs = pst.executeUpdate();
                if (rs > 0) {
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
