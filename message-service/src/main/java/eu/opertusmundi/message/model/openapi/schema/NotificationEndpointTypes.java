package eu.opertusmundi.message.model.openapi.schema;

import eu.opertusmundi.common.model.PageResultDto;
import eu.opertusmundi.common.model.RestResponse;
import eu.opertusmundi.message.model.NotificationDto;
import io.swagger.v3.oas.annotations.media.Schema;

public class NotificationEndpointTypes {

    @Schema(description = "Notification response")
    public static class NotificationResponseDto extends RestResponse<NotificationDto> {

    }

    @Schema(description = "Notification query result response")
    public static class NotificationListResponseDto extends RestResponse<PageResultDto<NotificationDto>> {

    }

}
