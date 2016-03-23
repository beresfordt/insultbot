package io.insultbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserResolutionService {
    private static final Logger logger = LoggerFactory.getLogger(UserResolutionService.class);
    private static final String LOG_MESSAGE = "Failed to identify user with username '{}'";

    private final SlackSession session;

    @Autowired
    public UserResolutionService(SlackSession session) {
        this.session = session;
    }

    public Optional<SlackUser> getUser(String username) {
        SlackUser userByUserName = session.findUserByUserName(username);

        if (userByUserName == null) {
            logger.info(LOG_MESSAGE, username);
        }

        return Optional.ofNullable(userByUserName);
    }
}
