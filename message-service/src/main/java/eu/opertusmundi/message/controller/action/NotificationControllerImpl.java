package eu.opertusmundi.message.controller.action;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import eu.opertusmundi.common.model.BaseResponse;
import eu.opertusmundi.common.model.QueryResultPage;
import eu.opertusmundi.common.model.RestResponse;
import eu.opertusmundi.message.model.NotificationCommandDto;
import eu.opertusmundi.message.model.NotificationDto;
import eu.opertusmundi.message.service.NotificationService;

@RestController
public class NotificationControllerImpl implements NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Override
    public RestResponse<?> findNotifications(
        Integer pageIndex, Integer pageSize, UUID userKey, ZonedDateTime dateFrom, ZonedDateTime dateTo, Boolean read
    ) {
        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        final QueryResultPage<NotificationDto> result = this.notificationService.find(pageIndex, pageSize, userKey, dateFrom, dateTo, read);

        return RestResponse.result(result);
    }

    @Override
    public RestResponse<?> sendNotification(NotificationCommandDto command) {
        final NotificationDto notification = this.notificationService.send(command);

        return RestResponse.result(notification);
    }

    @Override
    public BaseResponse readNotification(UUID key) {
        final NotificationDto notification = this.notificationService.read(key);

        return RestResponse.result(notification);
    }

}
