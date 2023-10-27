package br.com.solutis.squad1.orderservice.model.entity;

import br.com.solutis.squad1.orderservice.dto.product.ImageResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Image {
    @Id
    Long id;

    @Column(name = "archive_name", nullable = false)
    private String archiveName;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    Long size;

    String url;

    public Image(ImageResponseDto image) {
        this.id = image.id();
        this.archiveName = image.archiveName();
        this.originalName = image.originalName();
        this.contentType = image.contentType();
        this.size = image.size();
        this.url = image.url();
    }
}
