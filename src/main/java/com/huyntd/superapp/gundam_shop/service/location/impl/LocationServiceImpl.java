package com.huyntd.superapp.gundam_shop.service.location.impl;

import com.huyntd.superapp.gundam_shop.model.StoreLocation;
import com.huyntd.superapp.gundam_shop.repository.LocationRepository;
import com.huyntd.superapp.gundam_shop.service.location.LocationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class LocationServiceImpl implements LocationService {
    LocationRepository locationRepository;

    @Override
    public List<StoreLocation> getAllStoreLocation() {
        return locationRepository.findAll();
    }
}
