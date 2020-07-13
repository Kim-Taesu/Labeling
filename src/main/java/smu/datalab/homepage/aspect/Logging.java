package smu.datalab.homepage.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class Logging {
    @Around("@annotation(LoggingAnnotation)")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final String ip = request.getRemoteAddr();
        final String method = request.getMethod();
        final String uri = request.getRequestURI();
        final Principal principal = request.getUserPrincipal();
        String user = Objects.isNull(principal) ? "Anonymous" : principal.getName();
        long begin = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        log.info("[{} {}] [{} {}] {} {} ms", user, ip, uri, method, joinPoint.getSignature().getName(), (System.currentTimeMillis() - begin));
        return proceed;
    }
}
