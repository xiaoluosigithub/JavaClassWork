package dao;

import java.util.List;

public interface AddressDao {
    int addAddress(Address address);
    List<Address> getAll();
}
