package com.gencode.issuetool._websocket.security;

import com.gencode.issuetool.dao.UserInfoDao;
//import com.gencode.issuetool.websocket.domain.User;
//import com.gencode.issuetool.websocket.repositories.UserRepository;
import com.gencode.issuetool.obj.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    //UserRepository userRepository;
    private UserInfoDao userInfoDao;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        //User user = userRepository.findByUsername(authentication.getName());
        Optional<UserInfo> userInfo = userInfoDao.login(authentication.getName());
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(request.getContextPath() + "/secured/success");
    }
}