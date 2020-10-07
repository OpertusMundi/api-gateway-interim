package eu.opertusmundi.rating.service;

import java.util.List;
import java.util.UUID;

import eu.opertusmundi.rating.model.AssetRatingCommandDto;
import eu.opertusmundi.rating.model.AssetRatingDto;
import eu.opertusmundi.rating.model.ProviderRatingCommandDto;
import eu.opertusmundi.rating.model.ProviderRatingDto;

public interface RatingService {

    List<AssetRatingDto> getAssetRatings(final UUID id);

    List<ProviderRatingDto> getProviderRatings(final UUID id);

    void addAssetRating(final AssetRatingCommandDto command);

    void addProviderRating(final ProviderRatingCommandDto command);

}
