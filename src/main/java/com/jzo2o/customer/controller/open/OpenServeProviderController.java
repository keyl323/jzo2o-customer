package com.jzo2o.customer.controller.open;


import com.jzo2o.customer.model.dto.request.InstitutionRegisterReqDTO;
import com.jzo2o.customer.service.IServeProviderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("openServeProviderController")
@RequestMapping("open/serve-provider")
@Api(tags = "开放接口 - 服务人员、机构相关接口")
public class OpenServeProviderController {

    @Resource
    private IServeProviderService serveProviderService;

    @PostMapping("/institution/register")
    @ApiOperation("机构注册功能开发")
    public void register(@RequestBody @Validated InstitutionRegisterReqDTO institutionRegisterReqDTO){
        serveProviderService.register(institutionRegisterReqDTO);
    }

}
