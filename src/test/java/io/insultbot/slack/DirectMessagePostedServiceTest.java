package io.insultbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackEvent;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import io.insultbot.insulter.InsultService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DirectMessagePostedServiceTest {

    private final InsultService insultService = mock(InsultService.class);
    private final UserResolutionService userResolutionService = mock(UserResolutionService.class);
    private final SlackMessagePosted event = mock(SlackMessagePosted.class);
    private final SlackSession session = mock(SlackSession.class);
    private final SlackUser sender = mock(SlackUser.class);
    private final DirectMessagePostedService underTest = new DirectMessagePostedService(insultService, userResolutionService);

    @Before
    public void setUp() {
        when(insultService.getInsult()).thenReturn(Optional.empty());
        when(sender.getUserName()).thenReturn("sendersUsername");
        when(event.getSender()).thenReturn(sender);
    }

    @Test
    public void sendInsultToTargetIfIdentified() throws Exception {
        SlackUser someUser = mock(SlackUser.class);
        given(event.getMessageContent()).willReturn("@foo");
        given(userResolutionService.getUser("foo")).willReturn(Optional.of(someUser));

        underTest.processDirectMessage(event, session);

        verify(session).sendMessageToUser(eq(someUser), anyString(), eq(null));
    }

    @Test
    public void sendInsultToCallerIfTargetCannotBeIdentified() throws Exception {
        given(event.getMessageContent()).willReturn("@foo");
        given(userResolutionService.getUser("foo")).willReturn(Optional.empty());

        underTest.processDirectMessage(event, session);

        verify(session).sendMessageToUser(eq(sender), anyString(), eq(null));
    }

}