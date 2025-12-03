package dao.impl;

import dao.AddressDao;
import dao.BaseDao;
import dao.Address;
import java.sql.Timestamp;
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

        executeQuery(sql, rs -> {
            while (rs.next()) {
                list.add(map(rs));
            }
        });

        return list;
    }

    @Override
    public int count(String keyword) {
        String base = "SELECT COUNT(*) FROM smbms_address";
        String sql = (keyword == null || keyword.trim().isEmpty())
                ? base
                : base + " WHERE contact LIKE ? OR addressDesc LIKE ?";
        final int[] c = {0};
        if (sql.equals(base)) {
            executeQuery(sql, rs -> { if (rs.next()) c[0] = rs.getInt(1); });
        } else {
            String k = "%" + keyword + "%";
            executeQuery(sql, rs -> { if (rs.next()) c[0] = rs.getInt(1); }, k, k);
        }
        return c[0];
    }

    @Override
    public List<Address> findList(String keyword, int offset, int pageSize) {
        List<Address> list = new ArrayList<>();
        String base = "SELECT * FROM smbms_address";
        String where = (keyword == null || keyword.trim().isEmpty())
                ? ""
                : " WHERE contact LIKE ? OR addressDesc LIKE ?";
        String sql = base + where + " ORDER BY id DESC LIMIT ?, ?";
        if (where.isEmpty()) {
            executeQuery(sql, rs -> { while (rs.next()) list.add(map(rs)); }, offset, pageSize);
        } else {
            String k = "%" + keyword + "%";
            executeQuery(sql, rs -> { while (rs.next()) list.add(map(rs)); }, k, k, offset, pageSize);
        }
        return list;
    }

    @Override
    public Address getById(long id) {
        final Address[] res = {null};
        String sql = "SELECT * FROM smbms_address WHERE id = ?";
        executeQuery(sql, rs -> { if (rs.next()) res[0] = map(rs); }, id);
        return res[0];
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
