package io.insultbot.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SlackServiceTest {

    @Mock
    private DirectMessagePostedService directMessagePostedService;
    @Mock
    private SlackSession slackSession;
    
    @InjectMocks
    private SlackService slackService;

    @Test
    public void shouldConnectToSlack() throws Exception {
        slackService.connect();

        verify(slackSession).connect();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionIfCantConnect() throws Exception {
        doThrow(new IOException()).when(slackSession).connect();

        slackService.connect();
    }

    @Test
    public void shouldDisconnectFromSlack() throws Exception {
        slackService.disconnect();

        verify(slackSession).disconnect();
    }

    @Test
    public void shouldReturnNoExceptionShouldDisconnectFail() throws Exception {
        doThrow(new IOException()).when(slackSession).disconnect();

        slackService.disconnect();
    }
}