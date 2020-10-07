package eu.opertusmundi.rating.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.opertusmundi.rating.domain.ProviderRatingEntity;
import eu.opertusmundi.rating.model.ProviderRatingCommandDto;
import eu.opertusmundi.rating.model.ProviderRatingDto;

@Transactional(readOnly = true)
@Repository
public interface ProviderRatingRepository extends JpaRepository<ProviderRatingEntity, Integer> {

    @Query("From ProviderRating r where r.provider = :provider")
    List<ProviderRatingEntity> findAllByProviderId(@Param("provider") UUID id);

    @Transactional(readOnly = false)
    default ProviderRatingDto add(ProviderRatingCommandDto command) {
        final ProviderRatingEntity rating = new ProviderRatingEntity();

        rating.setAccount(command.getAccount());
        rating.setComment(command.getComment());
        rating.setProvider(command.getProvider());
        rating.setValue(command.getValue());

        return this.saveAndFlush(rating).toDto();
    }

}
