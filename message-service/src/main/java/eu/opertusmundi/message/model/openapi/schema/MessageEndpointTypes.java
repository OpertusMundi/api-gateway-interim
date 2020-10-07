package eu.opertusmundi.message.model.openapi.schema;

import eu.opertusmundi.common.model.QueryResultPage;
import eu.opertusmundi.common.model.RestResponse;
import eu.opertusmundi.message.model.MessageDto;
import io.swagger.v3.oas.annotations.media.Schema;

public class MessageEndpointTypes {

    @Schema(description = "Message response")
    public static class MessageResponseDto extends RestResponse<MessageDto> {

    }

    @Schema(description = "Message query result response")
    public static class MessageListResponseDto extends RestResponse<QueryResultPage<MessageDto>> {

    }

}
