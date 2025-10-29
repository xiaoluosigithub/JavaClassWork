package service.impl;

import dao.Address;
import dao.AddressDao;
import dao.impl.AddressDaoImpl;
import service.AddressService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 地址业务逻辑实现类（调用 DAO 层执行数据库操作）
 */
public class AddressServiceImpl implements AddressService {

    // 使用接口 + 实现类（解耦）
    private final AddressDao addressDao = new AddressDaoImpl();

    @Override
    public boolean addAddress(Address address) {
        // 参数校验（防止空数据写入数据库）
        if (address == null || address.getContact() == null || address.getAddressDesc() == null) {
            return false;
        }
        int rows = addressDao.addAddress(address);
        return rows > 0;
    }

    @Override
    public List<Address> getAllAddresses() {
        return addressDao.getAll();
    }

    @Override
    public List<Address> searchAddresses(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllAddresses();
        }

        // 先取出所有，再在内存中过滤（简单版本）
        List<Address> all = getAllAddresses();
        return all.stream()
                .filter(a ->
                        (a.getContact() != null && a.getContact().contains(keyword)) ||
                                (a.getAddressDesc() != null && a.getAddressDesc().contains(keyword))
                )
                .collect(Collectors.toList());
    }
}
