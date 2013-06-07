package kikuyu.util

import java.util.regex.Pattern

class UrlSymbolResolver {
    //these patterns are just rough guesses, not exhaustive or necessarily correct
    private static final String VALID_PATH_QUERY_STRING = "[\\.a-zA-Z0-9\\-_~!\$&'()\\*\\+,;=:@%/]+?\\??[\\.a-zA-Z0-9\\-_~!\$&'()\\*\\+,;=:@%/]+\$"
    private static final Pattern VALID_PATH_QUERY_PATTERN = ~(VALID_PATH_QUERY_STRING)
    private static final Pattern VALID_URL_PATTERN = ~("^http://" + VALID_PATH_QUERY_STRING)
    private static final String PATH_SEPARATOR = "/"

    Properties urlSymbolProperties

    boolean isValidSymbolicUrl(String url) {
        String[] parts = splitUrl(url)
        boolean symbolValid = urlSymbolProperties.containsKey(parts[0])
        boolean pathValid = true
        if (parts.length > 1) {
            pathValid = VALID_PATH_QUERY_PATTERN.matcher(parts[1]).matches()
        }
        symbolValid && pathValid
    }

    boolean isValidConcreteUrl(String url) {
        VALID_URL_PATTERN.matcher(url).matches()
    }

    private String[] splitUrl(String url) {
        String[] parts = url.split(PATH_SEPARATOR, 2)
        return parts
    }

    private String resolveSymbol(String symbol) {
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
