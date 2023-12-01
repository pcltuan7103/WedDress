/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Profile;
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
public class AccountDAO implements Serializable {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public Profile checkLogin(String userName, String password) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select p.profile_id, p.first_name,p.last_name,p.email,p.phone_number,p.address,p.user_name,r.role_name,p.is_active,p.password\n"
                        + "from profiles p\n"
                        + "inner join roles r on p.user_id = r.role_id\n"
                        + "where user_name = ? and password = ? and is_active = 1";

                pst = conn.prepareStatement(sql);
                pst.setString(1, userName);
                pst.setString(2, password);

                rs = pst.executeQuery();
                if (rs.next()) {
                    int profileId = rs.getInt("profile_id");
                    String firstName = rs.getString("first_name");
                    String lastname = rs.getString("last_name");
                    String email = rs.getString("email");
                    String phoneNumber = rs.getString("phone_number");
                    String address = rs.getString("address");
                    String roleName = rs.getString("role_name");
                    String username = rs.getString("user_name");
                    String pw = rs.getString("password");
                    boolean active = rs.getBoolean("is_active");
                    return new Profile(profileId, firstName, lastname, email, phoneNumber, address, roleName, active, username, pw);
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

    public boolean checkValidUsername(String userName, String email) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select profile_id from profiles where user_name = ? or email = ?";

                pst = conn.prepareStatement(sql);
                pst.setString(1, userName);
                pst.setString(2, email);

                rs = pst.executeQuery();
                if (rs.next()) {
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
    
     public int checkValidEmail(String email) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select profile_id from profiles where email = ?";

                pst = conn.prepareStatement(sql);
                pst.setString(1, email);

                rs = pst.executeQuery();
                if (rs.next()) {
                    return rs.getInt("profile_id");
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

        return -1;
    }

    public boolean updateProfile(Profile profile) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "UPDATE profiles\n"
                        + "SET first_name = ?, last_name = ?, phone_number = ?, address = ?, email = ?, password = ?\n"
                        + "where profile_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setString(1, profile.getFirstName());
                pst.setString(2, profile.getLastName());
                pst.setString(3, profile.getPhoneNumber());
                pst.setString(4, profile.getAddress());
                pst.setString(5, profile.getEmail());
                pst.setString(6, profile.getPassword());
                pst.setInt(7, profile.getProfileId());

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
    
    public boolean changePassword(int profileId, String password) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "UPDATE profiles\n"
                        + "SET password = ?\n"
                        + "where profile_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setString(1, password);
                pst.setInt(2, profileId);

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

    public boolean updateProfileByAdmin(Profile profile, int roleId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "UPDATE profiles\n"
                        + "SET first_name = ?, last_name = ?, phone_number = ?, address = ?, email = ?, user_name = ?, user_id = ?\n"
                        + "where profile_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setString(1, profile.getFirstName());
                pst.setString(2, profile.getLastName());
                pst.setString(3, profile.getPhoneNumber());
                pst.setString(4, profile.getAddress());
                pst.setString(5, profile.getEmail());
                pst.setString(6, profile.getUserName());
                pst.setInt(7, roleId);
                pst.setInt(8, profile.getProfileId());

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

    public boolean deleteProfile(int id) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "UPDATE profiles\n"
                        + "SET is_active = 0\n"
                        + "where profile_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, id);

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

    public List<Profile> getAllProfile() throws NamingException, SQLException {
        List<Profile> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select p.profile_id, p.first_name,p.last_name,p.email,p.phone_number,p.address,p.user_name,r.role_name,p.is_active,p.user_id\n"
                        + "from profiles p\n"
                        + "inner join roles r on p.user_id = r.role_id\n"
                        + "where is_active = 1 and user_id != 1";

                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()) {
                    int profileId = rs.getInt("profile_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    String phoneNumber = rs.getString("phone_number");
                    String address = rs.getString("address");
                    int userId = rs.getInt("user_id");
                    String userName = rs.getString("user_name");
                    boolean active = rs.getBoolean("is_active");
                    String roleName = rs.getString("role_name");

                    list.add(new Profile(profileId, firstName, lastName, email, phoneNumber, address, roleName, active, userName, userId));
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

    public boolean insertProfifle(Profile profile) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "insert profiles(address,email,first_name,is_active,last_name,password,phone_number,user_id,user_name)\n"
                        + "values (?,?,?,1,?,?,?,?,?)";

                pst = conn.prepareStatement(sql);
                pst.setString(1, profile.getAddress());
                pst.setString(2, profile.getEmail());
                pst.setString(3, profile.getFirstName());
                pst.setString(4, profile.getLastName());
                pst.setString(5, profile.getPassword());
                pst.setString(6, profile.getPhoneNumber());
                pst.setInt(7, profile.getUserId());
                pst.setString(8, profile.getUserName());

                int result = pst.executeUpdate();

                if (result > 0) {
//                    int profileId = rs.getInt("profile_id");
//                    String firstName = rs.getString("first_name");
//                    String lastName = rs.getString("last_name");
//                    String email = rs.getString("email");
//                    String phoneNumber = rs.getString("phone_number");
//                    String address = rs.getString("address");
//                    int userId = rs.getInt("user_id");
//                    String userName = rs.getString("user_name");
//                    boolean active = rs.getBoolean("is_active");
//                    String roleName = rs.getString("role_name");
//
//                    list.add(new Profile(profileId, firstName, lastName, email, phoneNumber, address, roleName, active, userName, userId));
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
}
