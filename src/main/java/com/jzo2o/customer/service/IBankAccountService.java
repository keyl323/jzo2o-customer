package com.jzo2o.customer.service;

import com.jzo2o.customer.model.domain.BankAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.customer.model.dto.request.BankAccountUpsertReqDTO;
import com.jzo2o.customer.model.dto.response.BankAccountResDTO;

/**
 * <p>
 * 个人银行账户 服务类
 * </p>
 *
 * @author yutsung
 * @since 2026-07-14
 */
public interface IBankAccountService extends IService<BankAccount> {

    /**
     * 新增或更新银行账户
     * @param bankAccountUpsertReqDTO
     */
    BankAccount addOrUpdate(BankAccountUpsertReqDTO bankAccountUpsertReqDTO ,Integer type);

    /**
     * 获取当前用户银行账号
     * @return
     */
    BankAccountResDTO getUserAccount(Integer type);

}
