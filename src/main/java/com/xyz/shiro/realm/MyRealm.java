package com.xyz.shiro.realm;

import com.xyz.model.User;
import com.xyz.utils.JdbcHelper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(MyRealm.class);
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upt = (UsernamePasswordToken) token;
        String loginUsername = upt.getUsername();
        String sql = "select * from shiro_user where username = ?";
        User user = JdbcHelper.querySingle(User.class, sql, loginUsername);
        int status = user.getStatus();
        if(status == 0)
            throw new LockedAccountException("账户锁定");
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(),
                                    ByteSource.Util.bytes(user.getUsername() + user.getPassword()), getName());
        return info;
    }
}
