package com.lijincan.config;

import com.lijincan.pojo.User;
import com.lijincan.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author: lijincan
 * @date: 2020年02月26日 13:19
 * @Description: TODO
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了=>授权doGetAuthorizationInfo");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        info.addStringPermission("user:add");
        //获取当前登录的对象
        Subject subject = SecurityUtils.getSubject();

        User currentUser = (User) subject.getPrincipal();
//        info.addStringPermission(currentUser.getPrams());
        return info;
    }

    /**
     * 用户认证的方法，在需要认证时自动被调用
     * @param AuthenticationToken
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了=>认证doGetAuthenticationInfo");

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;

        //加密userToken的密码
//        HashedCredentialsMatcher credentialsMatcher=new HashedCredentialsMatcher();
//        credentialsMatcher.setHashAlgorithmName("MD5");
//        credentialsMatcher.setHashIterations(2);
//        this.setCredentialsMatcher(credentialsMatcher);
//        //对数据库中的密码进行加密，参数1：加密算法，参数2：需要加密的数据，参数3：盐值，参数4：加密次数
//        Object md5 = new SimpleHash("MD5", "123456", "", 3);

        //从token中取到用户名再去查用户密码
        System.out.println("用户名"+userToken.getUsername());
        System.out.println("用户名密码"+new String(userToken.getPassword()));
        User user = userService.queryUserByName(userToken.getUsername(),new String(userToken.getPassword()));
        if (user==null){
            return null;
        }

        Subject currentSubject = SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        session.setAttribute("loginUser",user);

        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }

    public static void main(String[] args) {
        //使用shiro对数据进行加密，不同的加密算法，加密次数，盐值都会造成加密的结果不同
        //对数据库中的密码进行加密，参数1：加密算法，参数2：需要加密的数据，参数3：盐值，参数4：加密次数
       Object md5 =new SimpleHash("MD5", "123456", "", 3);
        System.out.println(md5);
    }
}
