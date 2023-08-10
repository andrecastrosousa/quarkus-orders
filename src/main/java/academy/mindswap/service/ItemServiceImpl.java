package academy.mindswap.service;

import academy.mindswap.converter.ItemConverter;
import academy.mindswap.dto.ItemCreateDto;
import academy.mindswap.dto.ItemDto;
import academy.mindswap.model.Item;
import academy.mindswap.repository.ItemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ItemServiceImpl implements ItemService {
    @Inject
    ItemRepository itemRepository;

    @Inject
    ItemConverter itemConverter;

    @Override
    public ItemDto create(ItemCreateDto itemCreateDto) {
        Item item = itemConverter.toEntityFromCreateDto(itemCreateDto);
        itemRepository.persist(item);
        return itemConverter.toDto(item);
    }

    @Override
    public List<ItemDto> getAll() {
        return itemRepository.listAll().stream().map(item -> itemConverter.toDto(item)).toList();
    }

    @Override
    public ItemDto findById(Long id) {
        return itemConverter.toDto(itemRepository.findById(id));
    }
}
