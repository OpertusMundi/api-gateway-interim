package eu.opertusmundi.rating.model.openapi.schema;

import java.util.List;

import eu.opertusmundi.common.model.RestResponse;
import eu.opertusmundi.rating.model.AssetRatingDto;
import eu.opertusmundi.rating.model.ProviderRatingDto;

public class RatingEndPointTypes {

    public static class AssetResponse extends RestResponse<List<AssetRatingDto>> {

    }

    public static class ProviderResponse extends RestResponse<List<ProviderRatingDto>> {

    }

}
