package com.huyntd.superapp.gundam_shop.controller;

import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import com.huyntd.superapp.gundam_shop.model.StoreLocation;
import com.huyntd.superapp.gundam_shop.service.location.LocationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("api/locations")
public class LocationController {
    LocationService locationService;

    @GetMapping("")
    ApiResponse<List<StoreLocation>> getAllLocations() {
        return ApiResponse.<List<StoreLocation>>builder()
                .result(locationService.getAllStoreLocation())
                .build();
    }
}
