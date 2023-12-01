/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

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
public class LocationDAO implements Serializable {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public List<Location> getAllLocation() throws NamingException, SQLException {
        List<Location> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select location_id,location_name,description,price,image,is_active\n"
                        + "from locations\n"
                        + "where is_active = 1";

                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("location_id");
                    String name = rs.getString("location_name");
                    String description = rs.getString("description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    list.add(new Location(id, name, description, price, image, active));
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

    public List<Location> getSearchLocation(String searchValue) throws NamingException, SQLException {
        List<Location> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select location_id,location_name,description,price,image,is_active\n"
                        + "from locations\n"
                        + "where location_name LIKE ? and is_active = 1";

                pst = conn.prepareStatement(sql);
                pst.setString(1, "%" + searchValue + "%");
                rs = pst.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("location_id");
                    String name = rs.getString("location_name");
                    String description = rs.getString("description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    list.add(new Location(id, name, description, price, image, active));
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

    public List<Location> getFilterLocation(int min, int max) throws NamingException, SQLException {
        List<Location> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select location_id,location_name,description,price,image,is_active\n"
                        + "from locations\n"
                        + "where price > ? and price < ? and is_active = 1";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, min);
                pst.setInt(2, max);

                rs = pst.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("location_id");
                    String name = rs.getString("location_name");
                    String description = rs.getString("description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    list.add(new Location(id, name, description, price, image, active));
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

    public boolean updateLocation(Location location) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "UPDATE locations\n"
                        + "SET location_name = ?, description = ?, price = ?, image = ?\n"
                        + "WHERE location_id = ?;";

                pst = conn.prepareStatement(sql);
                pst.setString(1, location.getName());
                pst.setString(2, location.getDescription());
                pst.setFloat(3, (float) location.getPrice());
                pst.setString(4, location.getImage());
                pst.setInt(5, location.getId());
                int result = pst.executeUpdate();

                if (result > 0) {
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

    public boolean insertLocation(Location location) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "insert into locations(location_name,description,price,image,is_active)\n"
                        + "values (?,?,?,?,1)";

                pst = conn.prepareStatement(sql);
                pst.setString(1, location.getName());
                pst.setString(2, location.getDescription());
                pst.setFloat(3, (float) location.getPrice());
                pst.setString(4, location.getImage());
                int result = pst.executeUpdate();

                if (result > 0) {
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

    public boolean deleteLocation(int id) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "UPDATE locations\n"
                        + "SET is_active = 0\n"
                        + "WHERE location_id = ?;";

                pst = conn.prepareStatement(sql);

                pst.setInt(1, id);
                int result = pst.executeUpdate();

                if (result > 0) {
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

    public Location getLocationById(int locationId) throws NamingException, SQLException {
        Location location = null;
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select location_id,location_name,description,price,image,is_active\n"
                        + "from locations\n"
                        + "where is_active = 1 and location_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, locationId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("location_id");
                    String name = rs.getString("location_name");
                    String description = rs.getString("description");
                    double price = rs.getFloat("price");
                    String image = rs.getString("image");
                    boolean active = rs.getBoolean("is_active");
                    location = new Location(id, name, description, price, image, active);
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

        return location;
    }
    

}
