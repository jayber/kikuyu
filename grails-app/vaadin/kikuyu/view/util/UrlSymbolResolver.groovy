package kikuyu.view.util

import java.util.regex.Pattern

class UrlSymbolResolver {
    private static final Pattern VALID_URL_PATTERN = ~"^http://[\\.\\d\\w-_/]+\\?[\\.\\d\\w-_/=]+\$"
    private static final String PATH_SEPARATOR = "/"

    Properties urlSymbolProperties

    boolean isValidSymbolicUrl(String url) {
        String symbol = extractSymbol(url)
        urlSymbolProperties.containsKey(symbol)
    }

    boolean isValidConcreteUrl(String url) {
        VALID_URL_PATTERN.matcher(url).matches()
    }

    private String extractSymbol(String url) {
        String[] parts = splitUrl(url)
        return parts[0]
    }

    private String[] splitUrl(String url) {
        String[] parts = url.split(PATH_SEPARATOR, 2)
        return parts
    }

    String resolveSymbol(String symbol) {
        urlSymbolProperties.getProperty(symbol)
    }

    String resolveToConcreteUrl(String url) {
        if (isValidConcreteUrl(url)) {
            return url
        } else {
            return resolveSymbolicUrl(url)
        }
    }

    private String resolveSymbolicUrl(String url) {
        String[] parts = splitUrl(url)
        String prefix = resolveSymbol(parts[0])
        if (prefix == null) {
            return url
        } else if (parts.length > 1) {
            return prefix + PATH_SEPARATOR + parts[1]
        } else {
            return prefix
        }
    }
}
