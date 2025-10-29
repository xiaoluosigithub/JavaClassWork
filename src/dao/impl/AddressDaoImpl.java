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
        });

        return list;
    }
}
