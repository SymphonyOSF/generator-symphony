package com.mycompany.bot;

import com.symphony.bdk.core.SymphonyBdk;
import com.symphony.bdk.template.api.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.symphony.bdk.core.config.BdkConfigLoader.loadFromClasspath;
import static com.symphony.bdk.core.activity.command.SlashCommand.slash;
import static java.util.Collections.emptyMap;

/**
 * FormReply Sample Application.
 */
public class BotApplication {

  /** The Logger */
  private static final Logger log = LoggerFactory.getLogger(BotApplication.class);

  public static void main(String[] args) throws Exception {

    // Initialize BDK entry point
    final SymphonyBdk bdk = new SymphonyBdk(loadFromClasspath("/config.yaml"));

    bdk.activities().register(slash(
            "/gif",
            true,
            context -> {
              try {
                bdk.messages().send(context.getStreamId(), "/templates/gif.ftl", emptyMap());
              } catch (TemplateException e) {
                log.error(e.getMessage());
              }
            }
    ));

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      log.info("Stopping Datafeed...");
      bdk.datafeed().stop();
    }));

    // finally, start the datafeed read loop
    bdk.datafeed().start();
  }
}
