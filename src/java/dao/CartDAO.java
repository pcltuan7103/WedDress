/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Cart;
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
public class CartDAO implements Serializable {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public boolean insertCart(Cart cart) throws NamingException, SQLException {
        List<Role> list = new ArrayList<>();
        try {
            conn = ConnectionConfig.getConnection();
            if (conn != null) {
                String sql = "insert cart(profile_id,order_id,is_active)\n"
                        + "values (?,?,?,?)";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, cart.getProfileId());
                pst.setInt(2, cart.getOrderId());
                pst.setBoolean(3, true);

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
