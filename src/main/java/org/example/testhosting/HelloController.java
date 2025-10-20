package org.example.testhosting;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class HelloController {

    // 1️⃣ عرض صفحة HTML فيها JavaScript
    @GetMapping
    public String htmlPage() {
        log.info("User accessed /api page to request geolocation.");
        return """
                <html>
                <body>
                    <h2>جارٍ تحديد موقعك...</h2>
                    <script>
                        navigator.geolocation.getCurrentPosition(function(pos) {
                            const lat = pos.coords.latitude;
                            const lon = pos.coords.longitude;
                            window.location = '/api/save-location?lat=' + lat + '&lon=' + lon;
                        }, function(err) {
                            document.body.innerHTML = 'فشل في الحصول على الموقع: ' + err.message;
                        });
                    </script>
                </body>
                </html>
                """;
    }

    // 2️⃣ استقبال اللوكيشن من المستخدم بعد الموافقة
    @GetMapping("/save-location")
    public String saveLocation(@RequestParam double lat, @RequestParam double lon, HttpServletRequest request) {

        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) ipAddress = request.getRemoteAddr();

        String googleMapsLink = "https://www.google.com/maps?q=" + lat + "," + lon;

        log.info("📍 User Location Link: {}", googleMapsLink);

        return "✅ Location received!<br>" +
                "IP: " + ipAddress + "<br>" +
                "Lat: " + lat + "<br>" +
                "Lon: " + lon + "<br>" +
                "<a href='" + googleMapsLink + "' target='_blank'>📍 Open in Google Maps</a>";
    }

}
