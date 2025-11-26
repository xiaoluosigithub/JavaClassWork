package service;

import dao.Address;
import java.util.List;

public interface AddressService {

    // 新增地址
    boolean addAddress(Address address);

     //查询全部地址
    List<Address> getAllAddresses();

    // 根据关键字查询
    List<Address> searchAddresses(String keyword);

    int countAddresses(String keyword);

    List<Address> searchAddresses(String keyword, int page, int pageSize);
}
