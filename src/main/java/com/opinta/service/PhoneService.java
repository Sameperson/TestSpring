package com.opinta.service;

import com.opinta.dto.PhoneDto;
import com.opinta.entity.Phone;

import java.util.List;

public interface PhoneService {

    List<Phone> getAllEntities();

    Phone getEntityById(long id);

    Phone getEntityByPhoneNumber(String phoneNumber);

    Phone getOrCreateEntityByPhoneNumber(String phoneNumber);

    Phone saveEntity(Phone phone);

    Phone updateEntity(long id, Phone phone);

    List<PhoneDto> getAll();

    PhoneDto getById(long id);

    PhoneDto save(PhoneDto phoneDto);

    PhoneDto update(long id, PhoneDto phoneDto);

    boolean delete(long id);
}
