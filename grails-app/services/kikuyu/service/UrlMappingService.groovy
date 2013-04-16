package kikuyu.service

import kikuyu.domain.UrlMapping

class UrlMappingService {

    List<UrlMapping> listUrlMappings() {
        return UrlMapping.listOrderByMatchOrder()
    }

    void switchMatchOrder(UrlMapping firstUrlMapping, UrlMapping secondUrlMapping) {
        final BigInteger firstMatchOrder = firstUrlMapping.matchOrder
        final BigInteger secondMatchOrder = secondUrlMapping.matchOrder
        /*
        if (firstUrlMapping.refresh().matchOrder != firstMatchOrder) {
            throw new IllegalStateException("")
        }*/
        firstUrlMapping.matchOrder = null
        secondUrlMapping.matchOrder = firstMatchOrder
        firstUrlMapping.save(failOnError: true, flush: true)
        secondUrlMapping.save(failOnError: true, flush: true)
        firstUrlMapping.matchOrder = secondMatchOrder
        firstUrlMapping.save(failOnError: true, flush: true)
    }
}
