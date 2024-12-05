package com.dev.eventmanager.location;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    private final LocationEntityConverter entityConverter;

    public LocationService(LocationRepository locationRepository, LocationEntityConverter locationEntityConverter) {
        this.locationRepository = locationRepository;
        this.entityConverter = locationEntityConverter;

    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(entityConverter::toDomain)
                .toList();

    }

    public Location createLocation(Location location) {
        if (locationRepository.existsByName(location.name())) {
            throw new IllegalArgumentException("Location name already taken");
        }

        var entityToSave = entityConverter.toEntity(location);
        return entityConverter.toDomain(
                locationRepository.save(entityToSave)
        );
    }

    public void deleteLocation(long locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new EntityNotFoundException("Not found location by id=%s"
                    .formatted(locationId));
        }

        locationRepository.deleteById(locationId);
    }

    public Location findById(long locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new EntityNotFoundException("Not found location by id=%s"
                    .formatted(locationId));
        }

        var entity = locationRepository.getReferenceById(locationId);
        return entityConverter.toDomain(entity);
    }

    public Location updateLocation(Long id, Location locationToUpdate) {
        if (!locationRepository.existsById(id))
            throw new EntityNotFoundException(
                    "No found location by id=%s"
                            .formatted(id));
        if (locationRepository.existsByName(locationToUpdate.name())) {
            throw new IllegalArgumentException("Location name already taken");
        }

        locationRepository.updateLocation(
                id,
                locationToUpdate.name(),
                locationToUpdate.address(),
                locationToUpdate.capacity(),
                locationToUpdate.description()
        );
        return entityConverter.toDomain(
                locationRepository.findById(id).orElseThrow()
        );
    }
}
