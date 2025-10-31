package cn.netbuffer.ssodemo.ssoserver;

import cn.dev33.satoken.sso.SaSsoManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SsoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoServerApplication.class, args);
        log.info("");
        log.info("---------------------- Sa-Token SSO 统一认证中心启动成功 ----------------------");
        log.info("配置信息：" + SaSsoManager.getServerConfig());
        log.info("统一认证登录地址：http://sso-server.com:17000/sso/auth");
        log.info("测试前需要根据官网文档修改 hosts 文件，测试账号密码：sa / 123456");
        log.info("");
    }

}