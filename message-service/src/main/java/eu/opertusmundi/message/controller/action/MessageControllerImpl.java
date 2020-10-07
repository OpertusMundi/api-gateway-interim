package eu.opertusmundi.message.controller.action;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import eu.opertusmundi.common.model.BaseResponse;
import eu.opertusmundi.common.model.QueryResultPage;
import eu.opertusmundi.common.model.RestResponse;
import eu.opertusmundi.message.model.MessageCommandDto;
import eu.opertusmundi.message.model.MessageDto;
import eu.opertusmundi.message.service.MessageService;

@RestController
public class MessageControllerImpl implements MessageController {

    @Autowired
    private MessageService messageService;

    @Override
    public RestResponse<?> findMessages(
        Integer pageIndex, Integer pageSize, UUID userKey, ZonedDateTime dateFrom, ZonedDateTime dateTo, Boolean read
    ) {
        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        final QueryResultPage<MessageDto> result = this.messageService.find(pageIndex, pageSize, userKey, dateFrom, dateTo, read);

        return RestResponse.result(result);
    }

    @Override
    public RestResponse<?> sendMessage(MessageCommandDto command) {
        final MessageDto message = this.messageService.send(command);

        return RestResponse.result(message);
    }

    @Override
    public BaseResponse readMessage(UUID owner, UUID key) {
        final MessageDto message = this.messageService.read(owner, key);

        return RestResponse.result(message);
    }

}