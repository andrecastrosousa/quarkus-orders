package academy.mindswap.service;

import academy.mindswap.dto.ItemCreateDto;
import academy.mindswap.dto.ItemDto;
import academy.mindswap.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemCreateDto item);

    List<ItemDto> getAll();

    ItemDto findById(Long id);

    ItemDto update(Long id, ItemDto itemDto);

    void delete(Long id);
}
