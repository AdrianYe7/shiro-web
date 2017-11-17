package com.xyz.shiro.realm;

import com.xyz.model.Role;
import com.xyz.model.User;
import com.xyz.utils.JdbcHelper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

public class MyRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(MyRealm.class);
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Collection cl = principals.fromRealm(getName());
        logger.info("MyRealm principals : " + cl);
        SimpleAuthorizationInfo authoInfo = null;
        if(cl.size() > 0) {
            User user = (User) cl.iterator().next();
            if(user != null) {
                String curUsername = user.getUsername();
                String roleSql = "select id, role_name from shiro_role where id = (select role_id from shiro_user where username = ?)";
                Role role = JdbcHelper.querySingle(Role.class, roleSql, curUsername);

                authoInfo = new SimpleAuthorizationInfo();
                authoInfo.addRole(role.getRoleName());

                int roleId = role.getId();
                String permSql = "select perm_name from shiro_perm where role_id = ?";
                List<String> perms = JdbcHelper.queryList(String.class, permSql, roleId);
                authoInfo.addStringPermissions(perms);
            }
        }
        return authoInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upt = (UsernamePasswordToken) token;
        String loginUsername = upt.getUsername();
        String loginPassword = new String(upt.getPassword());
        String sql = "select u.id, u.username, u.password, u.status, r.role_name from shiro_user u, shiro_role r where u.role_id = r.id and username = ?";
        User user = JdbcHelper.querySingle(User.class, sql, loginUsername);
        int status = user.getStatus();
        if(status == 0)
            throw new LockedAccountException("账户锁定");
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(),
                                    ByteSource.Util.bytes(loginUsername + loginPassword), getName());
        return info;
    }
}
