package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.IPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.api.publics.MapApi;
import com.jzo2o.api.publics.dto.response.LocationResDTO;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.NumberUtils;
import com.jzo2o.common.utils.StringUtils;
import com.jzo2o.customer.mapper.AddressBookMapper;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.jzo2o.customer.model.dto.response.AddressResDto;
import com.jzo2o.customer.service.IAddressBookService;
import com.jzo2o.mvc.utils.UserContext;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.apache.tomcat.jni.Address;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.core.annotation.OrderUtils.getOrder;


/**
 * <p>
 * 地址薄 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-07-06
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {

    @Resource
    private MapApi mapApi;

    @Override
    public List<AddressBookResDTO> getByUserIdAndCity(Long userId, String city) {

        List<AddressBook> addressBooks = lambdaQuery()
                .eq(AddressBook::getUserId, userId)
                .eq(AddressBook::getCity, city)
                .list();
        if(CollUtils.isEmpty(addressBooks)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyToList(addressBooks, AddressBookResDTO.class);
    }

    /**
     * 新增地址簿
     * @param addressBookUpsertReqDTO
     */
    @Override
    public void addAddress(AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        //0.设置经纬度
        LocationResDTO locationByAddress = mapApi.getLocationByAddress(addressBookUpsertReqDTO.getAddress());
        String location=locationByAddress.getLocation();
        Double lon= Double.valueOf(location.split(",")[0]);
        Double lat= Double.valueOf(location.split(",")[1]);
        AddressBook addressBook= BeanUtil.toBean(addressBookUpsertReqDTO, AddressBook.class);
        addressBook.setLon(lon);
        addressBook.setLat(lat);

        //1.先从threadlocal中获取当前用户id
        Long userId = UserContext.currentUserId();
        addressBook.setUserId(userId);
        //2.默认地址处理
        if(addressBook.getIsDefault().equals(1)) {
            //2.1.查询当前用户的默认地址
            AddressBook defaultAddress = lambdaQuery()
                    .eq(AddressBook::getUserId, userId)
                    .eq(AddressBook::getIsDefault, "1")
                    .one();
            //2.2.如果有默认地址，将其改为非默认
            if(defaultAddress != null) {
                defaultAddress.setIsDefault(0);
                updateById(defaultAddress);
            }
        }
        //3.新增地址
        save(addressBook);

    }

    /**
     * 获取地址簿详情
     * @param id
     */
    @Override
    public AddressBook getAddress(Long id) {
        AddressBook addressBook = baseMapper.selectById(id);
        if (ObjectUtil.isNull(addressBook)) {
            throw new ForbiddenOperationException("地址簿不存在");
        }

        return addressBook;
    }

    /**
     * 修改地址簿
     * @param id
     */
    @Override
    public AddressBook updateAddress(Long id,AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        //1.设置经纬度
        LocationResDTO locationByAddress = mapApi.getLocationByAddress(addressBookUpsertReqDTO.getAddress());
        String location=locationByAddress.getLocation();
        Double lon= Double.valueOf(location.split(",")[0]);
        Double lat= Double.valueOf(location.split(",")[1]);
        AddressBook addressBook= BeanUtil.toBean(addressBookUpsertReqDTO, AddressBook.class);
        addressBook.setLon(lon);
        addressBook.setLat(lat);
        //2.判断是否修改默认地址
        if(addressBook.getIsDefault().equals(1)) {
            //2.1.查询当前用户的默认地址
            AddressBook defaultAddress = lambdaQuery()
                    .eq(AddressBook::getUserId, UserContext.currentUserId())
                    .eq(AddressBook::getIsDefault, "1")
                    .ne(AddressBook::getId, id)
                    .one();
            //2.2.如果有默认地址，将其改为非默认
            if(defaultAddress != null) {
                defaultAddress.setIsDefault(0);
                updateById(defaultAddress);
            }
        }
        //3.更新地址
        addressBook.setId(id);
        updateById(addressBook);
        return addressBook;

    }


    /**
     * 设置/取消默认地址
     * @param flag
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddress(Integer flag, Long id) {
        Long userId = UserContext.currentUserId();

        if (flag == 1) {
            // 1. 一条SQL：将该用户下所有地址的 is_default 设置为 0 (非默认)
            lambdaUpdate()
                    .set(AddressBook::getIsDefault, 0)
                    .eq(AddressBook::getUserId, userId)
                    .eq(AddressBook::getIsDeleted,0)
                    .update();

            // 2. 一条SQL：将指定 id 的地址 is_default 设置为 1 (默认)
            boolean success = lambdaUpdate()
                    .set(AddressBook::getIsDefault, 1)
                    .eq(AddressBook::getId, id)
                    .eq(AddressBook::getIsDefault,0)
                    .eq(AddressBook::getUserId, userId) // 加上 userId 校验，防止越权修改他人地址
                    .update();

            if (!success) {
                throw new ForbiddenOperationException("地址不存在或无权操作");
            }

        } else {
            // flag == 0 (取消默认)
            // 1. 查询该地址当前是否已经是默认地址
            AddressBook addressBook = lambdaQuery()
                    .eq(AddressBook::getId, id)
                    .eq(AddressBook::getUserId, userId)
                    .eq(AddressBook::getIsDeleted,0)
                    .eq(AddressBook::getIsDefault, 1)
                    .one();

            if (addressBook == null) {
                throw new ForbiddenOperationException("请先设置默认地址再取消哦~");
            }

            // 2. 取消默认：将其设置为 0
            lambdaUpdate()
                    .set(AddressBook::getIsDefault, 0)
                    .eq(AddressBook::getId, id)
                    .eq(AddressBook::getIsDeleted,0)
                    .update();
        }

    }

    /**
     * 获取默认地址
     * @return
     */
    @Override
    public AddressBook getDefaultAddress() {
        AddressBook addressBook = lambdaQuery().eq(AddressBook::getUserId, UserContext.currentUserId())
                .eq(AddressBook::getIsDefault, 1)
                .one();

        if (addressBook != null){
            return null;
        }
        //有默认地址才显示 没有默认地址不需要报错
        return addressBook;
    }

    /**
     * 地址簿分页查询
     * @param addressBookPageQueryReqDTO
     * @return
     */
    @Override
    public PageResult<AddressResDto> selectPage(AddressBookPageQueryReqDTO addressBookPageQueryReqDTO) {
        return PageHelperUtils.selectPage(addressBookPageQueryReqDTO,
                () -> baseMapper.queryAddressListByUserId(UserContext.currentUserId()));
    }

}
