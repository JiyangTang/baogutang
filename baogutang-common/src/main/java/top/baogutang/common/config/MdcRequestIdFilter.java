package top.baogutang.common.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/15 : 12:25
 */
@Slf4j
@Component
@Order(1)
public class MdcRequestIdFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID_KEY = "X-Request-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String traceId = request.getHeader(REQUEST_ID_KEY);
            if (traceId == null) {
                traceId = UUID.randomUUID().toString().replace("-", "");
                log.info("requestId为空，自动生成 {}", traceId);
                request.setAttribute(REQUEST_ID_KEY, traceId);
            }
            MDC.put(REQUEST_ID_KEY, traceId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_ID_KEY);
        }

    }
}
