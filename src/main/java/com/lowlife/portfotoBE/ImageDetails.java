package com.lowlife.portfotoBE;

import java.util.Date;
import java.util.List;

public class ImageDetails {
    private String url;
    private String publicId;
    private String format;
    private int width;
    private int height;
    private long bytes;
    private Date createdAt;
    private List<String> tags;

    // Construtor
    public ImageDetails(String url, String publicId, String format, int width, int height, long bytes, Date createdAt, List<String> tags) {
        this.url = url;
        this.publicId = publicId;
        this.format = format;
        this.width = width;
        this.height = height;
        this.bytes = bytes;
        this.createdAt = createdAt;
        this.tags = tags;
    }

    // Getters (Obrigatórios para o Spring gerar o JSON)
    public String getUrl() { return url; }
    public String getPublicId() { return publicId; }
    public String getFormat() { return format; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public long getBytes() { return bytes; }
    public Date getCreatedAt() { return createdAt; }
    public List<String> getTags() { return tags; }
}