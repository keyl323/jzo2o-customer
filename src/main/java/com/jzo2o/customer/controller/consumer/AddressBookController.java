package com.jzo2o.customer.controller.consumer;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.jzo2o.customer.model.dto.response.AddressResDto;
import com.jzo2o.customer.service.IAddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController("addressBookController")
@RequestMapping("/consumer/address-book")
@Api("消费者端 - 地址相关接口")
public class AddressBookController {

    @Resource
    private IAddressBookService addressBookService;

    @PostMapping("")
    @ApiOperation("新增地址簿")
    public void addAddress(@RequestBody AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        addressBookService.addAddress(addressBookUpsertReqDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取地址簿详情")
    public AddressBook getAddress(@PathVariable Long id) {
        return addressBookService.getAddress(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("修改地址簿")
    public AddressBook updateAddress(@PathVariable Long id,@RequestBody AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        return addressBookService.updateAddress(id,addressBookUpsertReqDTO);
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除地址簿")
    public void deleteAddressBatch(@RequestBody List<Long> ids) {
        addressBookService.removeByIds(ids);
    }

    @PutMapping("/default")
    @ApiOperation("设置/取消默认地址")
    public void setDefaultAddress(@RequestParam Integer flag,Long id) {
        addressBookService.setDefaultAddress(flag,id);
    }

    @GetMapping("/defaultAddress")
    @ApiOperation("获取默认地址")
    public AddressBook getDefaultAddress() {
        return addressBookService.getDefaultAddress();
    }

    @GetMapping("/page")
    @ApiOperation("地址簿分页查询")
    public PageResult<AddressResDto> selectPage(AddressBookPageQueryReqDTO addressBookPageQueryReqDTO){
        return addressBookService.selectPage(addressBookPageQueryReqDTO);
    }




}
