package eu.opertusmundi.email.controller.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import eu.opertusmundi.common.model.BaseResponse;
import eu.opertusmundi.common.model.RestResponse;
import eu.opertusmundi.email.model.EnumOutputFormat;
import eu.opertusmundi.email.model.MessageDto;
import eu.opertusmundi.email.service.MailService;

@RestController
public class MailControllerImpl implements MailController {

    @Autowired
    private MailService mailService;

    @Override
    public BaseResponse sendMail(MessageDto message) {
        try {
            this.mailService.send(message);
        } catch (final Exception ex) {
            return RestResponse.failure();
        }

        return RestResponse.success();
    }

    @Override
    public RestResponse<String> renderText(MessageDto message) {
        final String result = this.mailService.render(EnumOutputFormat.TEXT, message);

        return RestResponse.result(result);
    }

    @Override
    public RestResponse<String> renderHtml(MessageDto message) {
        final String result = this.mailService.render(EnumOutputFormat.HTML, message);

        return RestResponse.result(result);
    }

}