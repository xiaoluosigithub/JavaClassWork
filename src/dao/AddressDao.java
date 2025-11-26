package dao;

import java.util.List;

public interface AddressDao {
    int addAddress(Address address);
    List<Address> getAll();
    int count(String keyword);
    List<Address> findList(String keyword, int offset, int pageSize);
}
