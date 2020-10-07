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
import eu.opertusmundi.message.domain.MessageEntity;
import eu.opertusmundi.message.model.MessageCommandDto;
import eu.opertusmundi.message.model.MessageDto;
import eu.opertusmundi.message.repository.JpaMessageRepository;

@Service
public class DefaultMessageService implements MessageService {

    @Autowired
    private JpaMessageRepository messageRepository;

    @Override
    public QueryResultPage<MessageDto> find(
        Integer pageIndex, Integer pageSize, UUID userKey, ZonedDateTime dateFrom, ZonedDateTime dateTo, Boolean read
    ) {
        final PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by(Direction.DESC, "sendAt"));

        final Page<MessageEntity> page = this.messageRepository.findAll(userKey, dateFrom, dateTo, read, pageRequest);

        final QueryResultPage<MessageDto> result = QueryResultPage.from(page, MessageEntity::toDto);

        return result;
    }

    @Override
    public MessageDto send(MessageCommandDto command) {
        return this.messageRepository.send(command);
    }

    @Override
    public MessageDto read(UUID owner, UUID key) {
        return this.messageRepository.read(owner, key);
    }

}
