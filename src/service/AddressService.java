package service;

import pojo.Address;
import java.util.List;

public interface AddressService {

    // 新增地址
    boolean addAddress(Address address);

     //查询全部地址
    List<Address> getAllAddresses();

    // 根据关键字查询
    // 删除旧的关键字查询接口，仅保留按 id + contact 的组合查询

    int countAddresses(Long id, String contact);
    
    List<Address> searchAddresses(Long id, String contact, int page, int pageSize);

    Address getById(long id);

    boolean update(Address address);

    boolean deleteById(long id);
}
