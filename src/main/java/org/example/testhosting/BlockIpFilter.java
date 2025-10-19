package org.example.testhosting;// BlockIpFilter.java

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class BlockIpFilter extends OncePerRequestFilter {

    // ممكن تعوّض هذه القائمة بتحميل من DB أو Redis أو ملف
    private final Set<String> blockedIps = new HashSet<>(Set.of(
            "192.0.2.1",
            "203.0.113.5"
//            "41.46.27.142" // مثال
    ));

    // لو عندك proxy (مثل nginx) تحقق من X-Forwarded-For أولاً
    private String getClientIp(HttpServletRequest request) {
        String xfwd = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xfwd)) {
            // X-Forwarded-For قد يحتوي على قائمة ip مفصولة بكم
            return xfwd.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = getClientIp(request);

        if (blockedIps.contains(clientIp)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("Access denied");
            // يمكن إضافة logging هنا
            return;
        }
        filterChain.doFilter(request, response);
    }
}
