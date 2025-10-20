package org.example.testhosting;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/extension")
public class ExtensionController {

    @GetMapping("/download-extension")
    public ResponseEntity<byte[]> downloadExtension() throws IOException {
        System.out.println("📦 [EXT] Download request received");

        ClassPathResource resource = new ClassPathResource("static/extension/my-extension.rar");

        if (!resource.exists()) {
            System.out.println("❌ [EXT] File not found in classpath");
            return ResponseEntity.notFound().build();
        }

        try (InputStream inputStream = resource.getInputStream()) {
            byte[] fileContent = FileCopyUtils.copyToByteArray(inputStream);

            System.out.println("✅ [EXT] File read successfully, size: " + fileContent.length + " bytes");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "my-extension.rar");
            headers.setContentLength(fileContent.length);
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);

        } catch (IOException e) {
            System.out.println("❌ [EXT] Error while reading file: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/tabs")
    public ResponseEntity<Map<String, String>> receiveTabEvent(@RequestBody Map<String, Object> payload) {
        System.out.println("📋 [EXT] Received tab event: " + payload);
        return ResponseEntity.ok(Map.of("status", "success"));
    }

    @GetMapping("/test-file")
    public ResponseEntity<String> testFile() {
        try {
            ClassPathResource resource = new ClassPathResource("static/extension/my-extension.rar");
            boolean exists = resource.exists();
            long size = exists ? resource.contentLength() : 0;
            String path = resource.getPath();

            return ResponseEntity.ok(
                    "File exists: " + exists + "\n" +
                            "Path: " + path + "\n" +
                            "Size: " + size + " bytes\n" +
                            "Absolute path: " + (exists ? resource.getFile().getAbsolutePath() : "N/A")
            );
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }
}