// 文件路径: src/main/java/com/example/movietheatersystem/dto/FeaturedMovieDTO.java
package com.example.movietheatersystem.dto;

public class FeaturedMovieDTO {
    private Long id;
    private String title;
    private String posterUrl;

    public FeaturedMovieDTO(Long id, String title, String posterUrl) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
    }

    // Getter 和 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
