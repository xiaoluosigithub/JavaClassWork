package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDao {

    // 增加记录
    public int addAddress(Address address) {
        String sql = "INSERT INTO smbms_address(contact, addressDesc, postCode, tel, createdBy, creationDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, address.getContact());
            ps.setString(2, address.getAddressDesc());
            ps.setString(3, address.getPostCode());
            ps.setString(4, address.getTel());
            ps.setLong(5, address.getCreatedBy());
            ps.setTimestamp(6, new Timestamp(address.getCreationDate().getTime()));
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 查询全部
    public List<Address> getAll() {
        List<Address> list = new ArrayList<>();
        String sql = "SELECT * FROM smbms_address";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Address a = new Address();
                a.setId(rs.getLong("id"));
                a.setContact(rs.getString("contact"));
                a.setAddressDesc(rs.getString("addressDesc"));
                a.setPostCode(rs.getString("postCode"));
                a.setTel(rs.getString("tel"));
                a.setCreatedBy(rs.getLong("createdBy"));
                a.setCreationDate(rs.getTimestamp("creationDate"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
