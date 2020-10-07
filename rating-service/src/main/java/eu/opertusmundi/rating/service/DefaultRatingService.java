package eu.opertusmundi.rating.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.opertusmundi.rating.domain.AssetRatingEntity;
import eu.opertusmundi.rating.domain.ProviderRatingEntity;
import eu.opertusmundi.rating.model.AssetRatingCommandDto;
import eu.opertusmundi.rating.model.AssetRatingDto;
import eu.opertusmundi.rating.model.ProviderRatingCommandDto;
import eu.opertusmundi.rating.model.ProviderRatingDto;
import eu.opertusmundi.rating.repository.AssetRatingRepository;
import eu.opertusmundi.rating.repository.ProviderRatingRepository;

@Service
public class DefaultRatingService implements RatingService {

    @Autowired
    private AssetRatingRepository assetRatingRepository;

    @Autowired
    private ProviderRatingRepository providerRatingRepository;

    @Override
    public List<AssetRatingDto> getAssetRatings(final UUID id) {
        return this.assetRatingRepository.findAllAssetId(id).stream()
            .map(AssetRatingEntity::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProviderRatingDto> getProviderRatings(final UUID id) {
        return this.providerRatingRepository.findAllByProviderId(id).stream()
            .map(ProviderRatingEntity::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public void addAssetRating(final AssetRatingCommandDto command) {
        this.assetRatingRepository.add(command);
    }

    @Override
    public void addProviderRating(final ProviderRatingCommandDto command) {
        this.providerRatingRepository.add(command);
    }

}
