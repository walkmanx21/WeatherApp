package org.walkmanx21.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.walkmanx21.util.CookieUtil;

@Component
public class SessionCookieInterceptor implements HandlerInterceptor {

    private final CookieUtil cookieUtil;

    public SessionCookieInterceptor(CookieUtil cookieUtil) {
        this.cookieUtil = cookieUtil;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String sessionId = (String) request.getAttribute("sessionId");
        if (sessionId != null)
            cookieUtil.setSessionId(sessionId, response);
    }
}
