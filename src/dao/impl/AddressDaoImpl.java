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
    // 添加地址
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
    // 查询全部地址
    @Override
    public List<Address> getAll() {
        List<Address> list = new ArrayList<>(); // 创建一个空列表，用于存储查询结果
        String sql = "SELECT * FROM smbms_address ORDER BY id ASC";
        try (Connection conn = DBUtil.getConnection(); // 获取数据库连接
             PreparedStatement ps = conn.prepareStatement(sql); // 预编译 SQL 语句
             ResultSet rs = ps.executeQuery()) { // 执行查询操作，返回结果集
            while (rs.next()) {
                list.add(map(rs)); // 映射结果集到 Address 对象，并添加到列表中
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 打印异常栈 trace
        }
        return list; // 返回查询结果列表    
    }
    // 统计符合条件的地址数量
    @Override
    public int count(Long id, String contact) {
        String base = "SELECT COUNT(*) FROM smbms_address"; // 基础 SQL 语句，用于统计地址数量
        StringBuilder where = new StringBuilder(); // 用于构建 WHERE 子句的 StringBuilder
        boolean hasId = id != null; // 检查 id 是否为 null
        boolean hasContact = contact != null && !contact.trim().isEmpty(); // 检查 contact 是否为 null 或空字符串
        if (hasId || hasContact) { // 检查是否有 id 或 contact 条件
            where.append(" WHERE "); // 构建 WHERE 子句的开始部分
            boolean first = true; // 用于判断是否是第一个条件，用于添加 "AND" 或 "WHERE"
            if (hasId) {
                where.append("id = ?"); // 如果有 id 条件，添加 "id = ?"
                first = false; // 标记 id 条件已处理
            }
            if (hasContact) {
                if (!first) where.append(" AND "); // 如果不是第一个条件，添加 "AND"
                where.append("contact LIKE ?"); // 如果有 contact 条件，添加 "contact LIKE ?"   
            }
        }
        String sql = base + where.toString(); // 构建完整的 SQL 语句
        int c = 0; // 用于存储查询结果的数量
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (hasId) {
                ps.setLong(idx++, id); // 如果有 id 条件，设置 id 参数
            }
            if (hasContact) {
                ps.setString(idx++, "%" + contact.trim() + "%"); // 如果有 contact 条件，设置 contact 参数
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) c = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }
    // 查询符合条件的地址列表
    @Override
    public List<Address> findList(Long id, String contact, int offset, int pageSize) {
        List<Address> list = new ArrayList<>(); // 创建一个空列表，用于存储查询结果
        String base = "SELECT * FROM smbms_address"; // 基础 SQL 语句，用于查询地址
        StringBuilder where = new StringBuilder(); // 用于构建 WHERE 子句的 StringBuilder
        boolean hasId = id != null; // 检查 id 是否为 null
        boolean hasContact = contact != null && !contact.trim().isEmpty(); // 检查 contact 是否为 null 或空字符串
        if (hasId || hasContact) { // 检查是否有 id 或 contact 条件
            where.append(" WHERE "); // 构建 WHERE 子句的开始部分
            boolean first = true; // 用于判断是否是第一个条件，用于添加 "AND" 或 "WHERE"    
            if (hasId) {
                where.append("id = ?"); // 如果有 id 条件，添加 "id = ?"
                first = false; // 标记 id 条件已处理
            }
            if (hasContact) {
                if (!first) where.append(" AND "); // 如果不是第一个条件，添加 "AND"
                where.append("contact LIKE ?"); // 如果有 contact 条件，添加 "contact LIKE ?"   
            }
        }
        String sql = base + where.toString() + " ORDER BY id ASC LIMIT ?, ?"; // 构建完整的 SQL 语句，添加分页子句
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (hasId) {
                ps.setLong(idx++, id); // 如果有 id 条件，设置 id 参数
            }
            if (hasContact) {
                ps.setString(idx++, "%" + contact.trim() + "%"); // 如果有 contact 条件，设置 contact 参数
            }
            ps.setInt(idx++, offset); // 设置分页参数 offset
            ps.setInt(idx, pageSize); // 设置分页参数 pageSize
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs)); // 映射结果集到 Address 对象，并添加到列表中 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list; // 返回查询到的 Address 对象列表，若未查询到则为空列表
    }
    // 根据ID查询地址
    @Override
    public Address getById(long id) {
        String sql = "SELECT * FROM smbms_address WHERE id = ?"; // 查询地址的 SQL 语句，根据 id 进行查询
        Address res = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id); // 设置 id 参数
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) res = map(rs); // 如果结果集有数据，映射到 Address 对象  
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res; // 返回查询到的 Address 对象，若未查询到则为 null
    }
    // 更新地址
    @Override
    public int update(Address address) {
        // 更新地址的 SQL 语句，根据 id 进行更新
        String sql = "UPDATE smbms_address SET contact=?, addressDesc=?, postCode=?, tel=?, userId=?, modifyBy=?, modifyDate=? WHERE id=?";
        // 处理修改日期，若为 null 则设置为当前时间戳
        Timestamp ts = address.getModifyDate() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(address.getModifyDate().getTime());
        // 执行更新操作，返回受影响行数
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
    // 删除地址
    @Override
    public int deleteById(long id) {
        // 删除地址的 SQL 语句，根据 id 进行删除
        String sql = "DELETE FROM smbms_address WHERE id = ?";
        return executeUpdate(sql, id); // 执行删除操作，返回受影响行数  
    }
    // 映射结果集到 Address 对象
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
