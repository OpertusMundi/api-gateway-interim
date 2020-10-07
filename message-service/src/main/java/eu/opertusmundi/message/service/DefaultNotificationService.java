package eu.opertusmundi.message.service;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import eu.opertusmundi.common.model.QueryResultPage;
import eu.opertusmundi.message.domain.NotificationEntity;
import eu.opertusmundi.message.model.NotificationCommandDto;
import eu.opertusmundi.message.model.NotificationDto;
import eu.opertusmundi.message.repository.JpaNotificationRepository;

@Service
public class DefaultNotificationService implements NotificationService {

    @Autowired
    private JpaNotificationRepository notificationRepository;

    @Override
    public QueryResultPage<NotificationDto> find(
        Integer pageIndex, Integer pageSize, UUID userKey, ZonedDateTime dateFrom, ZonedDateTime dateTo, Boolean read
    ) {
        final PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by(Direction.ASC, "sendAt"));

        final Page<NotificationEntity> page = this.notificationRepository.findAll(userKey, dateFrom, dateTo, read, pageRequest);

        final QueryResultPage<NotificationDto> result = QueryResultPage.from(page, NotificationEntity::toDto);

        return result;
    }

    @Override
    public NotificationDto send(NotificationCommandDto command) {
        return this.notificationRepository.send(command);
    }

    @Override
    public NotificationDto read(UUID key) {
        return this.notificationRepository.read(key);
    }
}
