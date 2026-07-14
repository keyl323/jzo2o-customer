package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.customer.model.domain.BankAccount;
import com.jzo2o.customer.mapper.BankAccountMapper;
import com.jzo2o.customer.model.dto.request.BankAccountUpsertReqDTO;
import com.jzo2o.customer.model.dto.response.BankAccountResDTO;
import com.jzo2o.customer.service.IBankAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.mvc.utils.UserContext;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 个人银行账户 服务实现类
 * </p>
 *
 * @author yutsung
 * @since 2026-07-14
 */
@Service
public class BankAccountServiceImpl extends ServiceImpl<BankAccountMapper, BankAccount> implements IBankAccountService {

    /**
     * 新增或更新银行账户
     * @param bankAccountUpsertReqDTO
     */
    @Override
    public BankAccount addOrUpdate(BankAccountUpsertReqDTO bankAccountUpsertReqDTO,Integer type) {
        BankAccount bankAccount = BeanUtil.toBean(bankAccountUpsertReqDTO, BankAccount.class);
        bankAccount.setUserId(UserContext.currentUserId());
        bankAccount.setUserType(type);
        saveOrUpdate(bankAccount);

        return bankAccount;

    }

    /**
     * 获取当前用户银行账号
     * @return
     */
    @Override
    public BankAccountResDTO getUserAccount(Integer type) {
        Long userId = UserContext.currentUserId();
        BankAccount bankAccount = lambdaQuery().eq(BankAccount::getUserId, userId)
                .eq(BankAccount::getIsDeleted, 0)
                .one();
        BankAccountResDTO bankAccountResDTO = BeanUtils.toBean(bankAccount, BankAccountResDTO.class);
        bankAccountResDTO.setId(userId);
        bankAccountResDTO.setType(type);
        return bankAccountResDTO;
    }


}
