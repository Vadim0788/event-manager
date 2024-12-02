package com.dev.eventmanager.location;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/locations")
public class LocationController {
    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    private final LocationDtoConverter dtoConverter;

    public LocationController(LocationService locationService, LocationDtoConverter dtoConverter) {
        this.locationService = locationService;
        this.dtoConverter = dtoConverter;
    }

    @GetMapping
    public List<LocationDto> getAllLocations() {
        log.info("Get request for get all locations");
        return locationService.getAllLocations()
                .stream()
                .map(dtoConverter::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<LocationDto> creatLocation(
            @RequestBody @Valid LocationDto locationToCreate
    ) {
        log.info("Get request for create location: location {}", locationToCreate);
        Location createdLocation = locationService.createLocation(
                dtoConverter.toDomain(locationToCreate)
        );
        return status(HttpStatus.CREATED)
                .body(dtoConverter.toDto(createdLocation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable("id") long locationId
    ) {
        log.info("Get request for delete location: location = {}", locationId);
        locationService.deleteLocation(locationId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    public LocationDto findById(
            @PathVariable("id") long locationId
    ) {
        log.info("Get request for get location: location = {}", locationId);
        var foundLocation = locationService.findById(locationId);

        return dtoConverter.toDto(foundLocation);
    }

    @PutMapping("/{id}")
    public LocationDto updateLocation(
            @PathVariable("id") Long id,
            @RequestBody @Valid LocationDto locationToUpdate
    ) {
        log.info("Get request for put location by id: id={}, locationToUpdate={}", id, locationToUpdate);
        var updatedLocation = locationService.updateLocation(
                id,
                dtoConverter.toDomain(locationToUpdate));
        return dtoConverter.toDto(updatedLocation);
    }

}
