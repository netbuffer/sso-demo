package cn.netbuffer.ssodemo.ssoclient_system.controller;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.sso.SaSsoHandle;
import cn.dev33.satoken.stp.StpUtil;
import com.ejlchina.okhttps.OkHttps;
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
     *         http://{host}:{port}/sso/login          -- Client端登录地址，接受参数：back=登录后的跳转地址
     *         http://{host}:{port}/sso/logout         -- Client端单点注销地址（isSlo=true时打开），接受参数：back=注销后的跳转地址
     *         http://{host}:{port}/sso/logoutCall     -- Client端单点注销回调地址（isSlo=true时打开），此接口为框架回调，开发者无需关心
     */
    @RequestMapping("/sso/*")
    public Object ssoRequest() {
        return SaSsoHandle.clientRequest();
    }

    // 配置SSO相关参数
    @Autowired
    private void configSso(SaTokenConfig cfg) {
        cfg.sso
            // 配置 Http 请求处理器
            .setSendHttp(url -> {
                String result=OkHttps.sync(url).get().getBody().toString();
                log.debug("client url={} result={}",url,result);
                return result;
            })
        ;
    }
}