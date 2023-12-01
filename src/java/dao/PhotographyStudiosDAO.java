/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.PhotographyStudio;
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
public class PhotographyStudiosDAO implements Serializable {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public List<PhotographyStudio> getAllPhotographyStudio() throws NamingException, SQLException {
        List<PhotographyStudio> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select studio_id,studio_name,description,price,image,is_active\n"
                        + "from photography_studios\n"
                        + "where is_active = 1";

                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("studio_id");
                    String name = rs.getString("studio_name");
                    String description = rs.getString("description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    list.add(new PhotographyStudio(id, name, description, price, image, active));
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

    public List<PhotographyStudio> getSearchPhotographyStudio(String searchValue) throws NamingException, SQLException {
        List<PhotographyStudio> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select studio_id,studio_name,description,price,image,is_active\n"
                        + "from photography_studios\n"
                        + "where studio_name LIKE ? and is_active = 1";

                pst = conn.prepareStatement(sql);
                pst.setString(1, "%" + searchValue + "%");
                rs = pst.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("studio_id");
                    String name = rs.getString("studio_name");
                    String description = rs.getString("description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    list.add(new PhotographyStudio(id, name, description, price, image, active));
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
    
    public List<PhotographyStudio> getFilterPhotographyStudio(int min, int max) throws NamingException, SQLException {
        List<PhotographyStudio> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select studio_id,studio_name,description,price,image,is_active\n"
                        + "from photography_studios\n"
                        + "where price > ? and price < ? and is_active = 1";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, min);
                pst.setInt(2, max);
                
                rs = pst.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("studio_id");
                    String name = rs.getString("studio_name");
                    String description = rs.getString("description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    list.add(new PhotographyStudio(id, name, description, price, image, active));
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
    
    public PhotographyStudio getStudioById(int studioId) throws NamingException, SQLException {
        PhotographyStudio studio = null;
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select studio_id,studio_name,description,price,image,is_active\n"
                        + "from photography_studios\n"
                        + "where is_active = 1 and studio_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, studioId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("studio_id");
                    String name = rs.getString("studio_name");
                    String description = rs.getString("description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    studio = new PhotographyStudio(id, name, description, price, image, active);
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

        return studio;
    }
}
