//package org.example.testhosting;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//@Order(1) // ✅ أول فلتر يتنفذ
//public class ExtensionBannerFilter implements Filter {
//
//    private static final String SCRIPT = """
//        <script>
//            if (!localStorage.getItem('extension_prompt_shown')) {
//                setTimeout(() => {
//                    if (confirm("لتحسين تجربتك على الموقع، يرجى تثبيت الإضافة الخاصة بنا — هل ترغب في تحميلها الآن؟")) {
//                          window.location.href = '/api/extension/download-extension';
//                    }
//                    localStorage.setItem('extension_prompt_shown', 'true');
//                }, 2000);
//            }
//        </script>
//        """;
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        String uri = httpRequest.getRequestURI();
//        System.out.println("🧪 [FILTER] Intercepted request: " + uri);
//
//        // ⚠️ تجاهل كل الحاجات اللي مش HTML
//        if (shouldSkipInjection(uri)) {
//            System.out.println("⚠️ [FILTER] Skipped script injection for: " + uri);
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // استخدام wrapper للـ HTML pages فقط
//        CharResponseWrapper wrappedResponse = new CharResponseWrapper(httpResponse);
//        chain.doFilter(request, wrappedResponse);
//
//        String contentType = wrappedResponse.getContentType();
//        System.out.println("ℹ️ [FILTER] Response Content-Type: " + contentType);
//
//        if (contentType != null && contentType.contains("text/html") && !httpResponse.isCommitted()) {
//            String content = wrappedResponse.toString();
//            System.out.println("✅ [FILTER] HTML detected. Injecting script...");
//
//            if (content.contains("</body>")) {
//                content = content.replace("</body>", SCRIPT + "</body>");
//            } else if (content.contains("</html>")) {
//                content = content.replace("</html>", SCRIPT + "</html>");
//            } else {
//                content = content + SCRIPT;
//            }
//
//            byte[] bytes = content.getBytes(wrappedResponse.getCharacterEncoding());
//            httpResponse.setContentLength(bytes.length);
//            httpResponse.getOutputStream().write(bytes);
//        } else {
//            System.out.println("⛔ [FILTER] Not HTML or response already committed. Skipping injection.");
//            if (!httpResponse.isCommitted()) {
//                byte[] data = wrappedResponse.toByteArray();
//                if (data.length > 0) {
//                    httpResponse.getOutputStream().write(data);
//                }
//            }
//        }
//    }
//
//    private boolean shouldSkipInjection(String uri) {
//        return uri.contains("/download-extension") ||
//                uri.startsWith("/api/") ||
//                uri.contains("/static/") ||
//                uri.endsWith(".js") ||
//                uri.endsWith(".css") ||
//                uri.endsWith(".png") ||
//                uri.endsWith(".jpg") ||
//                uri.endsWith(".ico") ||
//                uri.endsWith(".rar") ||
//                uri.endsWith(".zip") ||
//                uri.endsWith(".json");
//    }
//}