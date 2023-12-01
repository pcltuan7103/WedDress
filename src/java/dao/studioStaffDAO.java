/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Role;
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
public class studioStaffDAO implements Serializable {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public int getStudioIdByProfileId(int profileId) throws NamingException, SQLException {

        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select studio_id\n"
                        + "from studio_staff\n"
                        + "where profile_id = ? and is_active = 1";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, profileId);
                
                rs = pst.executeQuery();

                if (rs.next()) {
                    int studioId = rs.getInt("studio_id");
                   
                    return studioId;
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
        return 0;
    }

}
