package service;

import pojo.Address;
import java.util.List;

public interface AddressService {

    // 新增地址
    boolean addAddress(Address address);

     //查询全部地址
    List<Address> getAllAddresses();

    // 根据用户 ID 和联系人姓名查询地址数量
    int countAddresses(Long id, String contact);
    
    // 根据用户 ID 和联系人姓名分页查询地址列表
    List<Address> searchAddresses(Long id, String contact, int page, int pageSize);

    // 根据 ID 查询地址详情
    Address getById(long id);

    // 更新地址信息
    boolean update(Address address);

    // 根据 ID 删除地址
    boolean deleteById(long id);
}
