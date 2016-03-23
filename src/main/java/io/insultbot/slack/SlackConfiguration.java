package io.insultbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ullink.slack.simpleslackapi.impl.SlackSessionFactory.createWebSocketSlackSession;

@Configuration
public class SlackConfiguration {

    @Bean
    public SlackSession slackSession(@Value("${slackAuthToken}") String slackAuthToken) {
        return createWebSocketSlackSession(slackAuthToken);
    }
}
