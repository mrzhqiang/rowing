package com.github.mrzhqiang.rowing.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mrzhqiang.rowing.util.Jsons;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录成功处理器。
 * <p>
 * 主要看前端的需求，目前前端需要一个 token 来支持是否登录成功的状态，并没有实际用途。
 * <p>
 * 后续如果要切换为 jwt 模式，只需要在这里返回相关 token 即可。
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        HttpSession session = request.getSession(false);
        ObjectNode jsonNodes = Jsons.newObject();
        jsonNodes.put("token", session.getId());
        response.getWriter().write(Jsons.stringify(jsonNodes));
    }

}
