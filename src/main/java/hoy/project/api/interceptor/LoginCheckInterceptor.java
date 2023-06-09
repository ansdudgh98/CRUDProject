package hoy.project.api.interceptor;

import hoy.project.api.controller.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}",requestURI);
        HttpSession session = request.getSession();

        if(session == null || session.getAttribute(SessionConst.ACCOUNT) == null){
            return false;
        }
        if(session.getAttribute(SessionConst.ACCOUNT).toString().equals(SessionConst.GUEST)){
            return false;
        }

        return true;
    }
}
