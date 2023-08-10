package academy.mindswap.converter;

import academy.mindswap.dto.ItemCreateDto;
import academy.mindswap.dto.ItemDto;
import academy.mindswap.model.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ItemConverter {

    @Inject
    ObjectMapper objectMapper;

    public Item fromDto(ItemDto createDto) {
        return objectMapper.convertValue(createDto, Item.class);
    }

    public ItemDto toDto(Item item) {
        return objectMapper.convertValue(item, ItemDto.class);
    }

    public Item toEntityFromCreateDto(ItemCreateDto itemCreateDto) {
        return objectMapper.convertValue(itemCreateDto, Item.class);
    }
}
