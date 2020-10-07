package eu.opertusmundi.message.service;

import java.time.ZonedDateTime;
import java.util.UUID;

import eu.opertusmundi.common.model.QueryResultPage;
import eu.opertusmundi.message.model.NotificationCommandDto;
import eu.opertusmundi.message.model.NotificationDto;

public interface NotificationService {

    /**
     * Find notifications
     *
     * @param pageIndex
     * @param pageSize
     * @param userKey
     * @param dateFrom
     * @param dateTo
     * @param read
     *
     * @return An instance of {@link QueryResultPage} with items of type {@link NotificationDto}
     */
    QueryResultPage<NotificationDto> find(Integer pageIndex, Integer pageSize, UUID userKey, ZonedDateTime dateFrom, ZonedDateTime dateTo, Boolean read);

    /**
     * Send a new notification
     *
     * @param command The notification command
     * @return The new notification
     */
    NotificationDto send(NotificationCommandDto command);

    /**
     * Mark a notification as read
     *
     * @param key Notification key
     *
     * @return The updated notification
     */
    NotificationDto read(UUID key);

}
