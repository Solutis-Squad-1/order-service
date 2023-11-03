package br.com.solutis.squad1.orderservice.mapper;

import br.com.solutis.squad1.orderservice.dto.product.CategoryResponseDto;
import br.com.solutis.squad1.orderservice.model.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toResponseDto(Category category);

    @Mapping(target = "id", ignore = true)
    Category postDtoToEntity(CategoryResponseDto categoryResponseDto);

    List<Category> toDtosToEntities(List<CategoryResponseDto> categoryDtos);

    List<CategoryResponseDto> toEntitiesToDtos(List<Category> categories);

}
