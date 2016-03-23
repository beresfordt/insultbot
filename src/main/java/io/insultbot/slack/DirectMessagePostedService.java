package io.insultbot.slack;

import com.ullink.slack.simpleslackapi.SlackMessageHandle;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.replies.SlackMessageReply;
import io.insultbot.insulter.InsultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DirectMessagePostedService {

    private static final Logger logger = LoggerFactory.getLogger(DirectMessagePostedService.class);
    private static final Pattern INSULT_PATTERN = Pattern.compile("^(.*)\\s*$");
    private static final String UNKNOWNUSER_PATTERN = "You requested an insult for an unknown user. DM me with their username - eg 'richard' and I will insult them for you";
    private static final String INSULT_SENT = "Insult sent to the requested user";

    private final InsultService insultService;
    private final UserResolutionService userResolutionService;

    @Autowired
    public DirectMessagePostedService(InsultService insultService, UserResolutionService userResolutionService) {
        this.insultService = insultService;
        this.userResolutionService = userResolutionService;
    }

    public void processDirectMessage(SlackMessagePosted event, SlackSession session) {
        logger.debug(formattedLogMessage(event));

        if (eventWasSentByCurrentUser(event, session)) {
            return;
        }

        if (event.getMessageContent() == null) {
            logger.debug("MessageContent was null");
            return;
        }

        Matcher insultMatcher = INSULT_PATTERN.matcher(event.getMessageContent());
        String insult = insultService.getInsult().orElse("You smell");

        if (insultMatcher.matches()) {
            Optional<SlackUser> target = userResolutionService.getUser(insultMatcher.group(1));
            if (target.isPresent()) {
                sendSlackDirectMessage(target.get(), session, insult);
                sendSlackDirectMessage(event.getSender(), session, INSULT_SENT);
            }
            else {
                sendSlackDirectMessage(event.getSender(), session, UNKNOWNUSER_PATTERN);
            }
        }
        else {
            sendSlackDirectMessage(event.getSender(), session, UNKNOWNUSER_PATTERN);
        }
    }

    private static boolean eventWasSentByCurrentUser(SlackMessagePosted event, SlackSession session) {
        return event.getSender().getUserName().equals(session.sessionPersona().getUserName());
    }

    private static SlackMessageHandle<SlackMessageReply> sendSlackDirectMessage(SlackUser user, SlackSession session, String message) {
        return session.sendMessageToUser(user, message, null);
    }

    private static String formattedLogMessage(SlackMessagePosted event) {
        return String.format("Received message from '%s' requesting insult to '%s'", event.getSender().getUserName(), event.getMessageContent());
    }
}
