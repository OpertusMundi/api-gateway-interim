package eu.opertusmundi.message.service;

import java.time.ZonedDateTime;
import java.util.UUID;

import eu.opertusmundi.common.model.QueryResultPage;
import eu.opertusmundi.message.model.MessageCommandDto;
import eu.opertusmundi.message.model.MessageDto;

public interface MessageService {

    /**
     * Find messages
     *
     * @param pageIndex
     * @param pageSize
     * @param userKey
     * @param dateFrom
     * @param dateTo
     * @param read
     *
     * @return An instance of {@link QueryResultPage} with items of type {@link MessageDto}
     */
    QueryResultPage<MessageDto> find(Integer pageIndex, Integer pageSize, UUID userKey, ZonedDateTime dateFrom, ZonedDateTime dateTo, Boolean read);

    /**
     * Send a new message
     *
     * @param command The message command
     * @return The new message
     */
    MessageDto send(MessageCommandDto command);

    /**
     * Mark a message as read
     *
     * @param owner Owner key
     * @param key Message key
     *
     * @return The updated message
     */
    MessageDto read(UUID owner, UUID key);

}
