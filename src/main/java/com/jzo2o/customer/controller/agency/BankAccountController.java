package com.jzo2o.customer.controller.agency;

import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.common.model.CurrentUserInfo;
import com.jzo2o.customer.model.domain.BankAccount;
import com.jzo2o.customer.model.dto.request.BankAccountUpsertReqDTO;
import com.jzo2o.customer.model.dto.response.BankAccountResDTO;
import com.jzo2o.customer.service.IBankAccountService;
import com.jzo2o.mvc.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("agencyBankAccountController")
@RequestMapping("/agency/bank-account")
@Api(tags = "机构端 - 银行账户相关接口")
public class BankAccountController {

    @Resource
    private IBankAccountService bankAccountService;

    @PostMapping
    @ApiOperation("新增或更新银行账号")
    public void saveOrUpdate(@RequestBody BankAccountUpsertReqDTO bankAccountUpsertReqDTO) {
        CurrentUserInfo currentUserInfo = UserContext.currentUser();
        bankAccountUpsertReqDTO.setId(currentUserInfo.getId());
        bankAccountUpsertReqDTO.setType(currentUserInfo.getUserType());
        bankAccountService.upsert(bankAccountUpsertReqDTO);
    }

    /**
     * 获取当前用户银行账户
     * @return
     */
    @GetMapping("/currentUserBankAccount")
    @ApiOperation("获取当前用户银行账户")
    public BankAccountResDTO currentUserBankAccount() {
        BankAccount bankAccount = bankAccountService.getById(UserContext.currentUserId());
        return BeanUtil.toBean(bankAccount, BankAccountResDTO.class);
    }

}
