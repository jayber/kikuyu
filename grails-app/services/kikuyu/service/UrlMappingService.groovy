package kikuyu.service

import kikuyu.domain.UrlMapping

class UrlMappingService {

    List<UrlMapping> listUrlMappings() {
        return UrlMapping.listOrderByMatchOrder().sort({ it1, it2 -> it1.matchOrder.compareTo(it2.matchOrder) })
    }

    void switchMatchOrder(UrlMapping firstUrlMapping, UrlMapping secondUrlMapping) {
        final BigInteger firstMatchOrder = firstUrlMapping.matchOrder
        final BigInteger secondMatchOrder = secondUrlMapping.matchOrder

        firstUrlMapping.matchOrder = null
        secondUrlMapping.matchOrder = firstMatchOrder
        firstUrlMapping.save(failOnError: true, flush: true)
        secondUrlMapping.save(failOnError: true, flush: true)
        firstUrlMapping.matchOrder = secondMatchOrder
        firstUrlMapping.save(failOnError: true, flush: true)
    }

    void saveUrlMapping(UrlMapping urlMapping) {
        urlMapping.save(failOnError: true, flush: true)
    }

    UrlMapping findByPattern(String pattern) {
        UrlMapping.findByPattern(pattern)
    }

    BigInteger findLastMatchOrder() {
        listUrlMappings().last().matchOrder
    }
}
