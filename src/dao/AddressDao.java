package dao;

import pojo.Address;
import java.util.List;

public interface AddressDao {
    int addAddress(Address address);
    List<Address> getAll();
    int count(Long id, String contact);
    List<Address> findList(Long id, String contact, int offset, int pageSize);
    Address getById(long id);
    int update(Address address);
    int deleteById(long id);
}
