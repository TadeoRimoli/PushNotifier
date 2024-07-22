package EntrenaYa.PushNotifier.Security;

import EntrenaYa.PushNotifier.JWT.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Log4j2
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtTokenUtils jwtTokenUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String jwtToken = request.getHeader("Authorization");
        boolean validationOfToken = jwtTokenUtil.validateJwtToken(jwtToken);
        if(!validationOfToken)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return validationOfToken;
    }
}
