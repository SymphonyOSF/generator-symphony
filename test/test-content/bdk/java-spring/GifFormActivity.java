package com.mycompany.bot;

import com.symphony.bdk.core.activity.ActivityMatcher;
import com.symphony.bdk.core.activity.command.CommandContext;
import com.symphony.bdk.core.activity.form.FormReplyActivity;
import com.symphony.bdk.core.activity.form.FormReplyContext;
import com.symphony.bdk.core.activity.model.ActivityInfo;
import com.symphony.bdk.core.activity.model.ActivityType;
import com.symphony.bdk.core.service.MessageService;
import com.symphony.bdk.spring.annotation.Slash;
import com.symphony.bdk.template.api.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Collections.emptyMap;

@Component
public class GifFormActivity extends FormReplyActivity<FormReplyContext> {

    private static final Logger log = LoggerFactory.getLogger(GifFormActivity.class);

    @Autowired
    private MessageService messageService;

    @Slash("/gif")
    public void displayGifForm(CommandContext context) throws TemplateException {
        this.messageService.send(context.getStreamId(), "/templates/gif.ftl", emptyMap());
    }

    @Override
    public ActivityMatcher<FormReplyContext> matcher() {
        return context -> "gif-category-form".equals(context.getFormId())
            && "submit".equals(context.getFormValue("action"))
            && !StringUtils.isEmpty(context.getFormValue("category"));
    }

    @Override
    public void onActivity(FormReplyContext context) {
        log.info("Gif category is \"{}\"", context.getFormValue("category"));
    }

    @Override
    protected ActivityInfo info() {
        return new ActivityInfo().type(ActivityType.FORM)
            .name("Gif Display category form command")
            .description("\"Form handler for the Gif Category form\"");
    }
}