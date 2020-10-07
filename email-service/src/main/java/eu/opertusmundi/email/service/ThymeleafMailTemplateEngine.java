package eu.opertusmundi.email.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.opertusmundi.email.model.EnumOutputFormat;
import eu.opertusmundi.email.model.MessageDto;

@Service
public class ThymeleafMailTemplateEngine implements MailTemplateEngine {

    private final SpringTemplateEngine templateEngine;

    private final ApplicationContext applicationContext;

    private final ObjectMapper objectMapper;

    public ThymeleafMailTemplateEngine(ApplicationContext applicationContext, ObjectMapper objectMapper) {
        this.applicationContext = applicationContext;
        this.objectMapper = objectMapper;

        this.templateEngine = this.thymeleafTemplateEngine();
    }

    @Override
    public String render(EnumOutputFormat format, MessageDto message) throws IllegalArgumentException {
        final Context ctx = new Context();

        this.setContextParameters(ctx, message.getModel());

        switch (format) {
            case TEXT :
                final String textTemplate = this.getEffectiveTemplateName(message.getTemplate(), ".txt");

                return this.templateEngine.process(textTemplate, ctx);

            case HTML :
                final String htmlTemplate = this.getEffectiveTemplateName(message.getTemplate(), ".html");

                return this.templateEngine.process(htmlTemplate, ctx);

            default :
                throw new IllegalArgumentException(String.format("Format %s is not supported!", format));
        }
    }

    private String getEffectiveTemplateName(String name, String suffix) {
        Assert.isTrue(!StringUtils.isBlank(name), "Template name must not be null or empty");
        if (name.endsWith(suffix)) {
            return name;
        }
        return name + suffix;
    }

    private void setContextParameters(Context ctx, JsonNode model) {
        final Map<String, Object> variables = this.objectMapper.convertValue(model, new TypeReference<Map<String, Object>>() {});

        ctx.setVariables(variables);
    }

    private ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("classpath:/email/messages");
        return messageSource;
    }

    private SpringResourceTemplateResolver thymeleafTemplateResolver() {
        final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("classpath:/email/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    private SpringTemplateEngine thymeleafTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(this.thymeleafTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(this.emailMessageSource());
        return templateEngine;
    }

}
