package eu.opertusmundi.email.service;

import eu.opertusmundi.email.model.EnumOutputFormat;
import eu.opertusmundi.email.model.MessageDto;

public interface MailTemplateEngine {

    String render(EnumOutputFormat format, MessageDto message) throws IllegalArgumentException;

}
