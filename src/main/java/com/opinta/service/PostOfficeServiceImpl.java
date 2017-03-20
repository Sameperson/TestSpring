package com.opinta.service;

import com.opinta.dao.PostOfficeDao;
import com.opinta.dto.PostOfficeDto;
import com.opinta.mapper.PostOfficeMapper;
import com.opinta.model.PostOffice;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.apache.commons.beanutils.BeanUtils.copyProperties;

@Service
@Slf4j
public class PostOfficeServiceImpl implements PostOfficeService {
    private PostOfficeDao postOfficeDao;
    private PostOfficeMapper postOfficeMapper;

    @Autowired
    public PostOfficeServiceImpl(PostOfficeDao postOfficeDao, PostOfficeMapper postOfficeMapper) {
        this.postOfficeDao = postOfficeDao;
        this.postOfficeMapper = postOfficeMapper;
    }

    @Override
    @Transactional
    public List<PostOfficeDto> getAll() {
        log.info("Getting all postOffices");
        return postOfficeMapper.toDto(postOfficeDao.getAll());
    }

    @Override
    @Transactional
    public PostOfficeDto getById(Long id) {
        log.info("Getting postOffice by id {}", id);
        return postOfficeMapper.toDto(postOfficeDao.getById(id));
    }

    @Override
    @Transactional
    public PostOfficeDto save(PostOfficeDto postOfficeDto) {
        log.info("Saving postOffice {}", postOfficeDto);
        return postOfficeMapper.toDto(postOfficeDao.save(postOfficeMapper.toEntity(postOfficeDto)));
    }

    @Override
    @Transactional
    public PostOfficeDto update(Long id, PostOfficeDto postOfficeDto) {
        PostOffice source = postOfficeMapper.toEntity(postOfficeDto);
        PostOffice target = postOfficeDao.getById(id);
        if (target == null) {
            log.info("Can't update postOffice. PostOffice doesn't exist {}", id);
            return null;
        }
        try {
            copyProperties(target, source);
        } catch (Exception e) {
            log.error("Can't get properties from object to updatable object for postOffice", e);
        }
        target.setId(id);
        log.info("Updating postOffice {}", target);
        postOfficeDao.update(target);
        return postOfficeMapper.toDto(target);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        PostOffice postOffice = postOfficeDao.getById(id);
        if (postOffice == null) {
            log.debug("Can't delete postOffice. PostOffice doesn't exist {}", id);
            return false;
        }
        log.info("Deleting postOffice {}", postOffice);
        postOfficeDao.delete(postOffice);
        return true;
    }
}
