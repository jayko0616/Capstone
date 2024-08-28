package com.example.test1.board.service;

import com.example.test1.board.dto.StoreDTO;
import com.example.test1.board.entity.StoreEntity;
import com.example.test1.board.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DTO -> Entity (Entity Class)
// Entity -> DTO (DTO Class)

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    public StoreDTO save(StoreDTO storeDTO) throws IOException {
        StoreEntity storeEntity = StoreEntity.toSaveEntity(storeDTO);
        storeRepository.save(storeEntity);
        return storeDTO;
    }

    @Transactional
    public List<StoreDTO> findAll() {
        List<StoreEntity> storeEntityList = storeRepository.findAll();
        List<StoreDTO> storeDTOList = new ArrayList<>();
        for (StoreEntity storeEntity : storeEntityList) {
            storeDTOList.add(StoreDTO.toStoreDTO(storeEntity));
        }
        return storeDTOList;
    }

    @Transactional
    public void increaseHits(Long id) {
        storeRepository.increaseHits(id);
    }

    @Transactional
    public void decreaseHits(Long id) {
        storeRepository.decreaseHits(id);
    }

    @Transactional
    public StoreDTO findById(Long id) {
        Optional<StoreEntity> optionalBoardEntity = storeRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            StoreEntity storeEntity = optionalBoardEntity.get();
            StoreDTO storeDTO = StoreDTO.toStoreDTO(storeEntity);
            return storeDTO;
        } else {
            return null;
        }
    }

    public StoreDTO update(StoreDTO storeDTO) {
        StoreEntity storeEntity = StoreEntity.toUpdateEntity(storeDTO);
        storeRepository.save(storeEntity);
        return findById(storeDTO.getId());
    }

    public void delete(Long id) {
        storeRepository.deleteById(id);
    }

    public Page<StoreDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3; // 한 페이지에 보여줄 글 갯수
        // 한페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작
        Page<StoreEntity> storeEntities =
                storeRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));


        // 목록: id, writer, title, hits, createdTime
        Page<StoreDTO> storeDTOS = storeEntities.map(store -> new StoreDTO(store.getId(), store.getStoreCode(), store.getBoardTitle(), store.getBoardHits(), store.getCreatedTime()));
        return storeDTOS;
    }
}














