/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Feedback;
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
public class FeedbackDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public List<Feedback> getLastFiveFeedback() throws SQLException, NamingException {

        List<Feedback> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();

            String query = "select top 5 f.feedback_id, f.profile_id, TimeFeedBack, ReplyContent, Rating, user_name, f.Content\n"
                    + "from Feedback f join profiles a on f.profile_id = a.profile_id\n"
                    + "left join ReplyFeedback rl on f.feedback_id = rl.feedback_id\n"
                    + "order by f.feedback_id desc";

            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedback_id");
                int accountId = rs.getInt("profile_id");
                String feedbackDate = rs.getString("TimeFeedBack");
                String content = rs.getString("Content");
                String rating = rs.getString("Rating");
                String accountName = rs.getString("user_name");
                String replyContent = rs.getString("ReplyContent");
                String authorReply = rs.getString("user_name");

                list.add(new Feedback(feedbackId, accountId, accountName, feedbackDate, content, rating, replyContent, authorReply));
            }
        } finally {
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
        return list;
    }

    public List<Feedback> getAllFeedback() throws NamingException, SQLException {

        List<Feedback> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();

            String query = "select feedback_id, f.profile_id, TimeFeedBack, Content, Rating, user_name, Active\n"
                    + "from Feedback f join profiles a on f.profile_id = a.profile_id\n"
                    + "where Active = 1";

            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedback_id");
                int accountId = rs.getInt("profile_id");
                String feedbackDate = rs.getString("TimeFeedBack");
                String content = rs.getString("Content");
                String rating = rs.getString("Rating");
                String accountName = rs.getString("user_name");
                boolean active = rs.getBoolean("Active");

                list.add(new Feedback(feedbackId, accountId, accountName, feedbackDate, content, rating, active));
            }
        } finally {
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
        return list;
    }

    public boolean insertFeedback(Feedback feedback) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();

            String query = "insert Feedback(profile_id,Content,TimeFeedBack,Rating,Active)\n"
                    + "values (?,?,?,?,1)";

            ps = conn.prepareStatement(query);
            ps.setInt(1, feedback.getAccountId());
            ps.setString(2, feedback.getContent());
            ps.setString(3, feedback.getFeedbackDate());
            ps.setString(4, feedback.getRating());

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

    public List<Feedback> searchFeedbackByUser(String txtSearch) throws NamingException, SQLException {

        List<Feedback> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();

            String query = "select feedback_id, f.profile_id, TimeFeedBack, Content, Rating, user_name\n"
                    + "from Feedback f join profiles a on f.profile_id = a.profile_id\n"
                    + "where user_name like ?";

            ps = conn.prepareStatement(query);
            ps.setString(1, "%" + txtSearch + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedback_id");
                int accountId = rs.getInt("profile_id");
                String feedbackDate = rs.getString("TimeFeedBack");
                String content = rs.getString("Content");
                String rating = rs.getString("Rating");
                String accountName = rs.getString("user_name");

                list.add(new Feedback(feedbackId, accountId, accountName, feedbackDate, content, rating));
            }
        } finally {
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
        return list;
    }

    public List<Feedback> filterFeedbackByRating(String rating) throws NamingException, SQLException {

        List<Feedback> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();

            String query = "select feedback_id, f.profile_id, TimeFeedBack, Content, Rating, user_name\n"
                    + "from Feedback f join profiles a on f.profile_id = a.profile_id\n"
                    + "where Rating = ?";

            ps = conn.prepareStatement(query);
            ps.setString(1, rating);
            rs = ps.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedback_id");
                int accountId = rs.getInt("profile_id");
                String feedbackDate = rs.getString("TimeFeedBack");
                String content = rs.getString("Content");
                String accountName = rs.getString("user_name");

                list.add(new Feedback(feedbackId, accountId, accountName, feedbackDate, content, rating));
            }
        } finally {
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
        return list;
    }

    public boolean deleteFeedback(int feedbackId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();

            String query = "update Feedback\n"
                    + "set Active = 0\n"
                    + "where feedback_id = ?";

            ps = conn.prepareStatement(query);
            ps.setInt(1, feedbackId);

            int rs = ps.executeUpdate();
            if (rs > 0) {
                return true;

            }
        } finally {
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
