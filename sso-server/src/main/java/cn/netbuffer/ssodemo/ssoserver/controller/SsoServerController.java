package cn.netbuffer.ssodemo.ssoserver.controller;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.sso.SaSsoHandle;
import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Sa-Token-SSO Server端 Controller
 */
@Slf4j
@RestController
public class SsoServerController {

    @GetMapping
    public Object getTokenInfo() {
        return StpUtil.getTokenInfo();
    }

    /*
     * SSO-Server端：处理所有SSO相关请求
     *         http://{host}:{port}/sso/auth           -- 单点登录授权地址，接受参数：redirect=授权重定向地址
     *         http://{host}:{port}/sso/doLogin        -- 账号密码登录接口，接受参数：name、pwd
     *         http://{host}:{port}/sso/checkTicket    -- Ticket校验接口（isHttp=true时打开），接受参数：ticket=ticket码、ssoLogoutCall=单点注销回调地址 [可选]
     *         http://{host}:{port}/sso/logout         -- 单点注销地址（isSlo=true时打开），接受参数：loginId=账号id、secretkey=接口调用秘钥
     */
    @RequestMapping("/sso/*")
    public Object ssoRequest() {
        return SaSsoHandle.serverRequest();
    }

    // 配置SSO相关参数 
    @Autowired
    private void configSso(SaTokenConfig cfg) {
        cfg.sso
                // 配置：未登录时返回的View
                .setNotLoginView(() -> {
                    log.debug("goto sso server login page");
                    return new ModelAndView("sa-login.html");
                })
                // 配置：登录处理函数
                .setDoLoginHandle((name, pwd) -> {
                    // 此处仅做模拟登录，真实环境应该查询数据进行登录
                    if ("sa".equals(name) && "123456".equals(pwd)) {
                        SaHolder.getRequest().getParam("xxx");
                        StpUtil.login(10001,false);
                        return SaResult.ok("登录成功！");
                    }
                    return SaResult.error("登录失败！");
                })
        ;
    }

    // 自定义接口：获取userinfo
    @RequestMapping("/sso/userinfo")
    public Object userinfo(String loginId, String secretkey) {
        log.debug("---------------- 获取userinfo --------");

        // 校验调用秘钥
        SaSsoUtil.checkSecretkey(secretkey);

        // 自定义返回结果（模拟）
        return SaResult.ok()
                .set("id", loginId)
                .set("name", "linxiaoyu")
                .set("sex", "女")
                .set("age", 18);
    }

}