package dao.impl;

import dao.AddressDao;
import dao.BaseDao;
import dao.DBUtil;
import pojo.Address;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class AddressDaoImpl extends BaseDao implements AddressDao {

    @Override
    public int addAddress(Address address) {
        String sql = "INSERT INTO smbms_address(contact, addressDesc, postCode, tel, createdBy, creationDate) VALUES (?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql,
                address.getContact(),
                address.getAddressDesc(),
                address.getPostCode(),
                address.getTel(),
                address.getCreatedBy(),
                new Timestamp(address.getCreationDate().getTime())
        );
    }

    @Override
    public List<Address> getAll() {
        List<Address> list = new ArrayList<>();
        String sql = "SELECT * FROM smbms_address";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int count(String keyword) {
        String base = "SELECT COUNT(*) FROM smbms_address";
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        String sql = hasKeyword ? base + " WHERE contact LIKE ? OR addressDesc LIKE ?" : base;
        int c = 0;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (hasKeyword) {
                String k = "%" + keyword + "%";
                ps.setString(1, k);
                ps.setString(2, k);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) c = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public List<Address> findList(String keyword, int offset, int pageSize) {
        List<Address> list = new ArrayList<>();
        String base = "SELECT * FROM smbms_address";
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        String where = hasKeyword ? " WHERE contact LIKE ? OR addressDesc LIKE ?" : "";
        String sql = base + where + " ORDER BY id DESC LIMIT ?, ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (hasKeyword) {
                String k = "%" + keyword + "%";
                ps.setString(idx++, k);
                ps.setString(idx++, k);
            }
            ps.setInt(idx++, offset);
            ps.setInt(idx, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Address getById(long id) {
        String sql = "SELECT * FROM smbms_address WHERE id = ?";
        Address res = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) res = map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int update(Address address) {
        String sql = "UPDATE smbms_address SET contact=?, addressDesc=?, postCode=?, tel=?, userId=?, modifyBy=?, modifyDate=? WHERE id=?";
        Timestamp ts = address.getModifyDate() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(address.getModifyDate().getTime());
        return executeUpdate(sql,
                address.getContact(),
                address.getAddressDesc(),
                address.getPostCode(),
                address.getTel(),
                address.getUserId(),
                address.getModifyBy(),
                ts,
                address.getId()
        );
    }

    @Override
    public int deleteById(long id) {
        String sql = "DELETE FROM smbms_address WHERE id = ?";
        return executeUpdate(sql, id);
    }

    private Address map(java.sql.ResultSet rs) throws java.sql.SQLException {
        Address a = new Address();
        a.setId(rs.getLong("id"));
        a.setContact(rs.getString("contact"));
        a.setAddressDesc(rs.getString("addressDesc"));
        a.setPostCode(rs.getString("postCode"));
        a.setTel(rs.getString("tel"));
        a.setCreatedBy(rs.getLong("createdBy"));
        a.setCreationDate(rs.getTimestamp("creationDate"));
        a.setUserId(rs.getLong("userId"));
        return a;
    }
}
