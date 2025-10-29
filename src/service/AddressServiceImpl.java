package service;

import dao.Address;
import dao.AddressDao;
import service.AddressService;

import java.util.List;
import java.util.stream.Collectors;

// 地址业务逻辑实现类
public class AddressServiceImpl implements AddressService {
    // 注入service层
    private final AddressDao addressDao = new AddressDao();

    @Override
    public boolean addAddress(Address address) {
        // 参数校验（防止空数据）
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

        // 直接用 DAO 查询全部，然后在内存中过滤（简单版本）
        List<Address> all = getAllAddresses();
        return all.stream()
                .filter(a -> keyword.equals(a.getContact()) || keyword.equals(a.getAddressDesc()))
                .collect(Collectors.toList());
    }
}
