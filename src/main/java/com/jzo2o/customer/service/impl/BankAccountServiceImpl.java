package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
    /**
     * 新增或更新
     *
     * @param bankAccountUpsertReqDTO 银行账号新增或更新模型
     */
    @Override
    public void upsert(BankAccountUpsertReqDTO bankAccountUpsertReqDTO) {
        BankAccount bankAccount = BeanUtil.toBean(bankAccountUpsertReqDTO, BankAccount.class);
        super.saveOrUpdate(bankAccount);
    }




}
