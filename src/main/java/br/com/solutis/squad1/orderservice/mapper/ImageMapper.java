package br.com.solutis.squad1.orderservice.mapper;

import br.com.solutis.squad1.orderservice.dto.product.ImageResponseDto;
import br.com.solutis.squad1.orderservice.model.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageResponseDto toResponseDto(Image image);

    @Mapping(target = "id", ignore = true)
    Image postDtoToEntity(ImageResponseDto imageResponseDto);
}
