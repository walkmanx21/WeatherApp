package org.walkmanx21.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.walkmanx21.dto.UserResponseDto;

@Component
public class SetSessionAttributesUtil {

    public void setSessionAttributes(HttpServletRequest request, UserResponseDto userResponseDto) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("userId", userResponseDto.getId());
    }
}
