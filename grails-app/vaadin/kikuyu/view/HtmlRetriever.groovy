package kikuyu.view

import org.apache.commons.logging.LogFactory
import org.springframework.web.client.RestTemplate

class HtmlRetriever {

    public String retrieveHtml(String url) {
        LogFactory.getLog(this.class).debug("calling url: $url")
        RestTemplate restTemplate = new RestTemplate()
        final String templateHtml = restTemplate.getForObject(url, String.class)
        templateHtml
    }
}
