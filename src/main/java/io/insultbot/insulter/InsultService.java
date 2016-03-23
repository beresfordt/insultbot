package io.insultbot.insulter;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InsultService {

    private static final Logger LOG = LoggerFactory.getLogger(InsultService.class);

    private final HttpClient client;
    private final String pageUrl;

    @Autowired
    public InsultService(@Value("${insultUrl}") String pageUrl) {
        this.client = HttpClients.createDefault();
        this.pageUrl = pageUrl;
    }

    public Optional<String> getInsult() {
        String pageBody = getPage();
        return Optional.ofNullable(extractInsult(pageBody));
    }

    private String extractInsult(String pageHtml) {
        Document document = Jsoup.parse(pageHtml);
        Elements divs = document.select("div");
        for (Element div : divs) {
            if (div.hasClass("wrap")) {
                return div.ownText();
            }
        }

        LOG.warn("Unable to retrieve an insult from {}", pageUrl);

        return null;
    }

    private String getPage() {
        try {
            return client.execute(
                    RequestBuilder.get(pageUrl).build(),
                    new BasicResponseHandler()
            );
        } catch (IOException e) {
            LOG.error(String.format("Error getting insult page '%s'", pageUrl), e);
            throw new RuntimeException(e);
        }
    }
}

