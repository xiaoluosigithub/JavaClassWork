package dao;

import pojo.Address;
import java.util.List;

public interface AddressDao {
    // 添加地址
    int addAddress(Address address); 

    // 查询全部地址
    List<Address> getAll();

    // 统计符合条件的地址数量
    int count(Long id, String contact); 

    // 查询符合条件的地址列表
    List<Address> findList(Long id, String contact, int offset, int pageSize); 

    // 根据ID查询地址
    Address getById(long id); 

    // 更新地址
    int update(Address address); 
    
    // 删除地址
    int deleteById(long id); 
}
