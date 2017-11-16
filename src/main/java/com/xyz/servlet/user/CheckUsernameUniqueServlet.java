package com.xyz.servlet.user;

import com.xyz.service.user.UserService;
import com.xyz.service.user.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CheckUsernameUnique", value = "/checkUsernameUnique")
public class CheckUsernameUniqueServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CheckUsernameUniqueServlet.class);

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        int count = userService.getUserCountByName(username);
        req.setAttribute("isUnique", count >= 1 ? false : true);
    }
}
