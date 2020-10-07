package eu.opertusmundi.rating.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.opertusmundi.rating.domain.AssetRatingEntity;
import eu.opertusmundi.rating.model.AssetRatingCommandDto;
import eu.opertusmundi.rating.model.AssetRatingDto;

@Transactional(readOnly = true)
@Repository
public interface AssetRatingRepository extends JpaRepository<AssetRatingEntity, Integer> {

    @Query("From AssetRating r where r.asset = :asset")
    List<AssetRatingEntity> findAllAssetId(@Param("asset") UUID id);

    @Transactional(readOnly = false)
    default AssetRatingDto add(AssetRatingCommandDto command) {
        final AssetRatingEntity rating = new AssetRatingEntity();

        rating.setAccount(command.getAccount());
        rating.setAsset(command.getAsset());
        rating.setComment(command.getComment());
        rating.setValue(command.getValue());

        return this.saveAndFlush(rating).toDto();
    }

}
