package app.dao;

import app.Database;
import app.HashUtil;
import java.sql.*;

public class NGODao {
    public boolean create(String userid, String name, String email, String passwordPlain) throws SQLException {
        String sql = "INSERT INTO ngos (userid, password, name, email) VALUES (?,?,?,?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, userid);
            ps.setString(2, HashUtil.sha256(passwordPlain));
            ps.setString(3, name);
            ps.setString(4, email);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean validate(String userid, String passwordPlain) throws SQLException {
        String sql = "SELECT 1 FROM ngos WHERE userid=? AND password=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, userid);
            ps.setString(2, HashUtil.sha256(passwordPlain));
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
}