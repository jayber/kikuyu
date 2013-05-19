package kikuyu.view

import org.springframework.web.client.RestTemplate

class HtmlRetriever {

    public String retrieveHtml(String url) {
        RestTemplate restTemplate = new RestTemplate()
        final String templateHtml = restTemplate.getForObject(url, String.class)
        templateHtml
    }
}
