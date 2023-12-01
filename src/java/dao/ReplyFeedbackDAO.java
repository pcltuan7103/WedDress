/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.ReplyFeedback;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import util.ConnectionConfig;

/**
 *
 * @author ptd
 */
public class ReplyFeedbackDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public boolean insertReplyFeedback(ReplyFeedback replyFeedback) throws NamingException, SQLException {
        try {
             conn = ConnectionConfig.getConnection();

            String query = "insert ReplyFeedback(profile_id,feedback_id,ReplyContent)\n"
                    + "values (?,?,?)";

            ps = conn.prepareStatement(query);
            ps.setInt(1, replyFeedback.getAccountId());
            ps.setInt(2, replyFeedback.getFeedbackId());
            ps.setString(3, replyFeedback.getContent());
           

            int rs = ps.executeUpdate();
            if (rs > 0) {
                return true;

            }
        }finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return false;
    }

}
