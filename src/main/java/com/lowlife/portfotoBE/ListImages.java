package com.lowlife.portfotoBE;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class ListImages {

    private final Cloudinary cloudinary;

    public ListImages() {
         cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dnzwsptc8",
                "api_key", "193233763422214",
                "api_secret", "SEdvkcaD2cUvT7QsLHocE6NeGGs",
                "secure", true));
    }

    @GetMapping("/list-images")
    public List<ImageDetails> listImages(@RequestParam String albumName) {
        try {
            Map<?, ?> resourcesResult = cloudinary.api().resourcesByAssetFolder(albumName, ObjectUtils.emptyMap());
            return extractImageDetails(resourcesResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    private List<ImageDetails> extractImageDetails(Map<?, ?> resourcesResult) {
        List<ImageDetails> images = new ArrayList<>();
        List<Map<?, ?>> resources = (List<Map<?, ?>>) resourcesResult.get("resources");

        for (Map<?, ?> resource : resources) {
            try {
                String publicId = (String) resource.get("public_id");
                String format = (String) resource.get("format");
                String url = cloudinary.url().generate(publicId + "." + format);

                int width = getSafeInt(resource.get("width"));
                int height = getSafeInt(resource.get("height"));
                long bytes = getSafeLong(resource.get("bytes"));
                Date createdAt = parseDate(resource.get("created_at"));

                List<String> tags = (List<String>) resource.get("tags");

                images.add(new ImageDetails(
                        url,
                        publicId,
                        format,
                        width,
                        height,
                        bytes,
                        createdAt,
                        tags
                ));
            } catch (Exception e) {
                System.err.println("Erro ao processar recurso: " + resource);
                e.printStackTrace();
            }
        }

        return images;
    }

    private int getSafeInt(Object value) {
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        return 0;
    }

    private long getSafeLong(Object value) {
        if (value instanceof Long) return (Long) value;
        if (value instanceof Number) return ((Number) value).longValue();
        return 0L;
    }

    private Date parseDate(Object value) {
        try {
            if (value instanceof String) {
                // Ex: "2025-04-06T17:08:35Z"
                Instant instant = Instant.parse((String) value);
                return Date.from(instant);
            } else if (value instanceof Long) {
                return new Date((Long) value);
            }
        } catch (Exception e) {
            System.err.println("Erro ao parsear data: " + value);
            e.printStackTrace();
        }
        return new Date(); // fallback para data atual
    }
}
