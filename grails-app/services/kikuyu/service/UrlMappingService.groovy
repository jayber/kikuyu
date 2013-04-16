package kikuyu.service

import kikuyu.domain.UrlMapping

class UrlMappingService {

    List<UrlMapping> listUrlMappings() {
        return UrlMapping.listOrderByMatchOrder()
    }

    void incrementMatchOrder(UrlMapping urlMapping) {

    }

    void decrementMatchOrder(UrlMapping urlMapping) {

    }

    void switchMatchOrder(UrlMapping firstUrlMapping, UrlMapping secondUrlMapping) {

    }
}
