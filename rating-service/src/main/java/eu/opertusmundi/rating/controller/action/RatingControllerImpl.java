package eu.opertusmundi.rating.controller.action;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;

import eu.opertusmundi.common.model.BaseResponse;
import eu.opertusmundi.common.model.RestResponse;
import eu.opertusmundi.rating.model.AssetRatingCommandDto;
import eu.opertusmundi.rating.model.AssetRatingDto;
import eu.opertusmundi.rating.model.ProviderRatingCommandDto;
import eu.opertusmundi.rating.model.ProviderRatingDto;
import eu.opertusmundi.rating.service.RatingService;

@RestController
public class RatingControllerImpl implements RatingController {

    @Autowired
    private RatingService ratingService;

    @Override
    public RestResponse<List<AssetRatingDto>> getAssetRatings(UUID id) {
        final List<AssetRatingDto> result = this.ratingService.getAssetRatings(id);

        return RestResponse.result(result);
    }

    @Override
    public RestResponse<List<ProviderRatingDto>> getProviderRatings(UUID id) {
        final List<ProviderRatingDto> result = this.ratingService.getProviderRatings(id);

        return RestResponse.result(result);
    }

    @Override
    public BaseResponse addAssetRating(UUID id, AssetRatingCommandDto command) {
        try {
            Assert.isTrue(id.equals(command.getAsset()), "Path variable id value must equal command asset value");

            this.ratingService.addAssetRating(command);
        } catch (final Exception ex) {
            return RestResponse.failure();
        }

        return RestResponse.success();
    }

    @Override
    public BaseResponse addProviderRating(UUID id, ProviderRatingCommandDto command) {
        try {
            Assert.isTrue(id.equals(command.getProvider()), "Path variable id value must equal command provider value");

            this.ratingService.addProviderRating(command);
        } catch (final Exception ex) {
            return RestResponse.failure();
        }

        return RestResponse.success();
    }

}