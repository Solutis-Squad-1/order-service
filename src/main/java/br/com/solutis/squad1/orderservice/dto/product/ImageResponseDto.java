package br.com.solutis.squad1.orderservice.dto.product;

public record ImageResponseDto (
        Long id,
        String archiveName,
        String originalName,
        String contentType,
        Long size,
        String url
) {
}
