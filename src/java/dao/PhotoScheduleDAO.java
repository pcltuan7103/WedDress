/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.PhotoSchedule;
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
public class PhotoScheduleDAO implements Serializable {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public boolean insertPhotoSchedule(PhotoSchedule ps) throws NamingException, SQLException {
        boolean rs = false;
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "insert dbo.photo_schedules(user_id,location_id,studio_id,schedule_date,status, order_start_date, order_end_date)\n"
                        + "values (?, ?, ?, ?, 'create',?,?)";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, ps.getUserId());
                pst.setInt(2, ps.getLocationId());
                pst.setInt(3, ps.getStudioId());
                pst.setString(4, ps.getScheduleDate());
                pst.setString(5, ps.getTimeRange());
                pst.setString(6, ps.getTimeRangeReturn());

                int result = pst.executeUpdate();

                if (result > 0) {
                    rs = true;
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

        return rs;
    }

    public List<String> checkScheduleAvailable(String startDate, String endDate, int locationId, int studioId) throws NamingException, SQLException {
        List<String> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "SELECT schedule_id, user_id, location_id, studio_id, schedule_date, status, order_start_date, order_end_date\n"
                        + "FROM photo_schedules\n"
                        + "WHERE \n"
                        + "  (location_id = ? and studio_id = ?) \n"
                        + "  AND (status = 'pending' or status = 'confirm')\n"
                        + "  AND (( order_start_date <= ? AND order_end_date >= ?) \n"
                        + "  OR ( order_start_date <= ? AND order_end_date >= ?))";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, locationId);
                pst.setInt(2, studioId);
                pst.setString(3, startDate);
                pst.setString(4, startDate);
                pst.setString(5, endDate);
                pst.setString(6, endDate);

                rs = pst.executeQuery();

                if (rs.next()) {
                    String orderStartDate = rs.getString("order_start_date");
                    String orderEndDate = rs.getString("order_end_date");
                    list.add(orderStartDate);
                    list.add(orderEndDate);
                }

                if (!list.isEmpty()) {
                    return list;
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

    public List<String> checkScheduleAvailableAdmin(String startDate, String endDate, String itemType, int id, PhotoSchedule photo) throws NamingException, SQLException {
        List<String> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "";
                if ("location".equals(itemType)) {
                    sql = "SELECT schedule_id, user_id, location_id, studio_id, schedule_date, status, order_start_date, order_end_date\n"
                            + "FROM photo_schedules\n"
                            + "WHERE \n"
                            + "  (location_id = " + id + " and studio_id = " + photo.getStudioId() + " )\n"
                            + "  AND (status = 'pending' or status = 'confirm')\n"
                            + "  AND (( order_start_date <= ? AND order_end_date >= ?) \n"
                            + "  OR ( order_start_date <= ? AND order_end_date >= ?))";
                } else {
                    sql = "SELECT schedule_id, user_id, location_id, studio_id, schedule_date, status, order_start_date, order_end_date\n"
                            + "FROM photo_schedules\n"
                            + "WHERE \n"
                            + "  (location_id = " + photo.getLocationId()+ " and studio_id = " + id + " )\n"
                            + "  AND (status = 'pending' or status = 'confirm')\n"
                            + "  AND (( order_start_date <= ? AND order_end_date >= ?) \n"
                            + "  OR ( order_start_date <= ? AND order_end_date >= ?))";
                }

                pst = conn.prepareStatement(sql);
                pst.setString(1, startDate);
                pst.setString(2, startDate);
                pst.setString(3, endDate);
                pst.setString(4, endDate);

                rs = pst.executeQuery();

                if (rs.next()) {
                    String orderStartDate = rs.getString("order_start_date");
                    String orderEndDate = rs.getString("order_end_date");
                    list.add(orderStartDate);
                    list.add(orderEndDate);
                }

                if (!list.isEmpty()) {
                    return list;
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

    public List<PhotoSchedule> getPhotoScheduleByUserId(int userId) throws NamingException, SQLException {
        List<PhotoSchedule> list = new ArrayList();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select schedule_id, user_id, location_id, studio_id,schedule_date,status,order_start_date, order_end_date\n"
                        + "from photo_schedules\n"
                        + "where user_id = ? and status = 'pending'";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, userId);

                rs = pst.executeQuery();

                while (rs.next()) {
                    int scheduleId = rs.getInt("schedule_id");
                    int userID = rs.getInt("user_id");
                    int locationId = rs.getInt("location_id");
                    int studioId = rs.getInt("studio_id");
                    String scheduleDate = rs.getString("schedule_date");
                    String status = rs.getString("status");
                    String timeRange = rs.getString("order_start_date");
                    String timeRangeReturn = rs.getString("order_end_date");

                    list.add(new PhotoSchedule(scheduleId, userID, locationId, studioId, scheduleDate, status, timeRange, timeRangeReturn));
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

    public List<PhotoSchedule> getPhotoScheduleByStudioId(int studioId) throws NamingException, SQLException {
        List<PhotoSchedule> list = new ArrayList();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select schedule_id,user_id,location_id,schedule_date,status,order_start_date, order_end_date\n"
                        + "from photo_schedules\n"
                        + "where studio_id = ? and status = 'pending'";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, studioId);

                rs = pst.executeQuery();

                while (rs.next()) {
                    int scheduleId = rs.getInt("schedule_id");
                    int userID = rs.getInt("user_id");
                    int locationId = rs.getInt("location_id");
                    String scheduleDate = rs.getString("schedule_date");
                    String status = rs.getString("status");
                    String timeRange = rs.getString("order_start_date");
                    String timeRangeReturn = rs.getString("order_end_date");
                    list.add(new PhotoSchedule(scheduleId, userID, locationId, studioId, scheduleDate, status, timeRange, timeRangeReturn));
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

    public int getLastIdOfPhotoSchedule() throws NamingException, SQLException {

        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select top 1 schedule_id\n"
                        + "from photo_schedules\n"
                        + "order by schedule_id desc";

                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();

                if (rs.next()) {
                    int scheduleId = rs.getInt("schedule_id");

                    return scheduleId;
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
        return 0;

    }

    public boolean confirmScheduleByScheduleId(int scheduleId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "update photo_schedules\n"
                        + "set status = 'confirm'\n"
                        + "where schedule_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, scheduleId);

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

    public boolean pendingScheduleByScheduleId(int scheduleId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "update photo_schedules\n"
                        + "set status = 'pending'\n"
                        + "where schedule_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, scheduleId);

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

    public boolean updatePhotoScheduleById(int scheduleId, int locationId, int studioId, String orderDate) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "update photo_schedules\n"
                        + "set location_id = ?, studio_id = ?, schedule_date = ?\n"
                        + "where schedule_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, locationId);
                pst.setInt(2, studioId);
                pst.setString(3, orderDate);
                pst.setInt(4, scheduleId);

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

    public boolean updateStudioByScheduleId(int scheduleId, int studioId, String orderDate) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "update photo_schedules\n"
                        + "set studio_id = ?, schedule_date = ?\n"
                        + "where schedule_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, studioId);
                pst.setString(2, orderDate);
                pst.setInt(3, scheduleId);

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

    public boolean updateLocationByScheduleId(int scheduleId, int locationId, String orderDate) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "update photo_schedules\n"
                        + "set location_id = ?, schedule_date = ?\n"
                        + "where schedule_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, locationId);
                pst.setString(2, orderDate);
                pst.setInt(3, scheduleId);

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

    public boolean deleteScheduleById(int scheduleId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "delete photo_schedules\n"
                        + "where schedule_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, scheduleId);

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

    public PhotoSchedule getPhotoScheduleById(int scheduleId) throws NamingException, SQLException {

        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select schedule_id,user_id,location_id,studio_id,schedule_date,status,order_start_date, order_end_date\n"
                        + "from photo_schedules\n"
                        + "where schedule_id = ? and status = 'pending'";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, scheduleId);

                rs = pst.executeQuery();

                if (rs.next()) {
                    int userID = rs.getInt("user_id");
                    int locationId = rs.getInt("location_id");
                    int studioId = rs.getInt("studio_id");
                    String scheduleDate = rs.getString("schedule_date");
                    String status = rs.getString("status");
                    String timeRange = rs.getString("order_start_date");
                    String timeRangeReturn = rs.getString("order_end_date");
                    return new PhotoSchedule(scheduleId, userID, locationId, studioId, scheduleDate, status, timeRange, timeRangeReturn);
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
    
    public PhotoSchedule getPhotoScheduleByIdAdmin(int scheduleId) throws NamingException, SQLException {

        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select schedule_id,user_id,location_id,studio_id,schedule_date,status,order_start_date, order_end_date\n"
                        + "from photo_schedules\n"
                        + "where schedule_id = ? and (status = 'confirm' or status = 'pending')";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, scheduleId);

                rs = pst.executeQuery();

                if (rs.next()) {
                    int userID = rs.getInt("user_id");
                    int locationId = rs.getInt("location_id");
                    int studioId = rs.getInt("studio_id");
                    String scheduleDate = rs.getString("schedule_date");
                    String status = rs.getString("status");
                    String timeRange = rs.getString("order_start_date");
                    String timeRangeReturn = rs.getString("order_end_date");
                    return new PhotoSchedule(scheduleId, userID, locationId, studioId, scheduleDate, status, timeRange, timeRangeReturn);
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

    public PhotoSchedule getPhotoScheduleByIdCreate(int scheduleId) throws NamingException, SQLException {

        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select schedule_id,user_id,location_id,studio_id,schedule_date,status,order_start_date, order_end_date\n"
                        + "from photo_schedules\n"
                        + "where schedule_id = ? and status = 'create'";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, scheduleId);

                rs = pst.executeQuery();

                if (rs.next()) {
                    int userID = rs.getInt("user_id");
                    int locationId = rs.getInt("location_id");
                    int studioId = rs.getInt("studio_id");
                    String scheduleDate = rs.getString("schedule_date");
                    String status = rs.getString("status");
                    String timeRange = rs.getString("order_start_date");
                    String timeRangeReturn = rs.getString("order_end_date");
                    return new PhotoSchedule(scheduleId, userID, locationId, studioId, scheduleDate, status, timeRange, timeRangeReturn);
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

    public PhotoSchedule getLastPhotoSchedule() throws NamingException, SQLException {

        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "select top 1 schedule_id,user_id,location_id,studio_id,schedule_date,status, order_start_date, order_end_date\n"
                        + "from photo_schedules\n"
                        + "where status = 'create'\n"
                        + "order by schedule_id desc";

                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();

                if (rs.next()) {
                    int scheduleId = rs.getInt("schedule_id");
                    int userID = rs.getInt("user_id");
                    int locationId = rs.getInt("location_id");
                    int studioId = rs.getInt("studio_id");
                    String scheduleDate = rs.getString("schedule_date");
                    String status = rs.getString("status");
                    String timeRange = rs.getString("order_start_date");
                    String timeRangeReturn = rs.getString("order_end_date");
                    return new PhotoSchedule(scheduleId, userID, locationId, studioId, scheduleDate, status, timeRange, timeRangeReturn);
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
    
    public boolean confirmScheduleById(int scheduleId) throws NamingException, SQLException {
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "update photo_schedules\n"
                        + "set status = 'confirm'\n"
                        + "where schedule_id = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, scheduleId);

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
