package cn.netbuffer.ssodemo.ssoclient_system.controller;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.sso.processor.SaSsoClientProcessor;
import cn.dev33.satoken.sso.template.SaSsoClientTemplate;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sa-Token-SSO Client端 Controller
 */
@Slf4j
@RestController
public class SsoClientController {

    @Value("${spring.application.name}")
    private String applicationName;

    // 首页 
    @RequestMapping("/")
    public String index() {
        log.debug("get index");
        String str = "<h2>"+applicationName+" 应用端</h2>" +
                "<p>当前会话是否登录：" + StpUtil.isLogin() + "</p>" +
                "<p>tokeninfo：" + StpUtil.getTokenInfo() + "</p>" +
                "<p><a href=\"javascript:location.href='/sso/login?back=' + encodeURIComponent(location.href);\">登录</a> " +
                "<a href='/sso/logout?back=self'>注销</a></p>";
        return str;
    }

    /*
     * SSO-Client端：处理所有SSO相关请求
     *         http://{host}:{port}/sso/login            -- Client 端登录地址
     *         http://{host}:{port}/sso/logout            -- Client 端注销地址（isSlo=true时打开）
     *         http://{host}:{port}/sso/pushC            -- Client 端接收消息推送地址
     */
    @RequestMapping("/sso/*")
    public Object ssoRequest() {
        return SaSsoClientProcessor.instance.dister();
    }

    // 配置SSO Client相关参数
    @Autowired
    private void configSso(SaSsoClientTemplate ssoClientTemplate) {

    }

    // 当前应用独自注销 (不退出其它应用)
    @RequestMapping("/sso/logoutByAlone")
    public Object logoutByAlone() {
        StpUtil.logout();
        return SaSsoClientProcessor.instance._ssoLogoutBack(SaHolder.getRequest(), SaHolder.getResponse());
    }

}