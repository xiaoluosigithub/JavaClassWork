package service.impl;

import dao.Address;
import dao.AddressDao;
import dao.impl.AddressDaoImpl;
import service.AddressService;

import java.util.ArrayList;
import java.util.List;

// 地址业务逻辑实现类（调用 DAO 层执行数据库操作）
public class AddressServiceImpl implements AddressService {

    // 使用接口 + 实现类，保证解耦，便于后期替换实现类
    private final AddressDao addressDao = new AddressDaoImpl();

    // 添加地址信息
    @Override
    public boolean addAddress(Address address) {
        // 参数校验：address 及其关键字段不能为 null
        if (address == null || address.getContact() == null || address.getAddressDesc() == null) {
            return false; // 无效数据，不执行数据库操作
        }

        // 调用 DAO 层执行插入操作，返回受影响行数
        int rows = addressDao.addAddress(address);

        // 如果插入成功（受影响行数大于 0），返回 true
        return rows > 0;
    }

    // 获取所有地址信息
    @Override
    public List<Address> getAllAddresses() {
        // 直接调用 DAO 层查询全部地址
        return addressDao.getAll();
    }

    // 搜索地址信息
    @Override
    public List<Address> searchAddresses(String keyword) {
        // 如果关键字为空或全是空格，则返回所有地址
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllAddresses();
        }

        // 结果列表
        List<Address> result = new ArrayList<Address>();

        // 获取所有地址
        List<Address> all = getAllAddresses();

        // 遍历每个地址，判断联系人或地址描述是否包含关键字
        for (int i = 0; i < all.size(); i++) {
            Address a = all.get(i);
            if ((a.getContact() != null && a.getContact().indexOf(keyword) >= 0) ||
                    (a.getAddressDesc() != null && a.getAddressDesc().indexOf(keyword) >= 0)) {
                // 如果匹配，则加入结果列表
                result.add(a);
            }
        }

        // 返回匹配结果
        return result;
    }
}
