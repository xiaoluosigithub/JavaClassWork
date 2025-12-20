package service.impl;

import pojo.Address;
import dao.AddressDao;
import dao.impl.AddressDaoImpl;
import service.AddressService;

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

    // 统计符合条件的地址数量
    @Override
    public int countAddresses(Long id, String contact) {
        return addressDao.count(id, contact);
    }

    // 根据用户 ID 和联系人姓名分页查询地址列表
    @Override
    public List<Address> searchAddresses(Long id, String contact, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return addressDao.findList(id, contact, offset, pageSize);
    }

    // 根据 ID 查询地址详情 
    @Override
    public Address getById(long id) {
        return addressDao.getById(id);
    }

    // 更新地址信息 
    @Override
    public boolean update(Address address) {
        if (address == null || address.getId() == null) return false;
        return addressDao.update(address) > 0;
    }

    // 根据 ID 删除地址 
    @Override
    public boolean deleteById(long id) {
        return addressDao.deleteById(id) > 0;
    }
}
