package com.xyz.shiro.filter;

import com.xyz.constant.UserConstant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ShiroWebAccessFilter extends AccessControlFilter {
    private static final Logger logger = LoggerFactory.getLogger(ShiroWebAccessFilter.class);

    private String registerUrl;
    private static final String UNAUTHORIZED_URL = "/unauthorized";

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        if(SecurityUtils.getSubject().hasRole(UserConstant.ROLE_ADMIN)) {
            HttpServletRequest request = WebUtils.toHttp(servletRequest);
            String requestUri = WebUtils.getRequestUri(request);
            logger.info(this.getClass().getName() + " filter requestUri : " + requestUri);
            logger.info(this.getClass().getName() + " requestUri : " + this.registerUrl);
            return requestUri.contains(this.registerUrl);
        } else {
            return false;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        WebUtils.issueRedirect(servletRequest, servletResponse, UNAUTHORIZED_URL);
        return false;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }
}
