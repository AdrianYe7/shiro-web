package com.xyz.servlet.user;

import com.xyz.model.Role;
import com.xyz.model.User;
import com.xyz.service.user.UserService;
import com.xyz.service.user.impl.UserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "toRoles", value = "/toRoles")
public class ToRoles extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ToRoles.class);

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        System.out.println("ToRoles : " + user);
        List<Role> roles = userService.getAllRoles();
        logger.info("roles : " + roles);
    }
}
