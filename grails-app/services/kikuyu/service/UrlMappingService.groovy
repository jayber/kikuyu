package kikuyu.service

import kikuyu.domain.UrlMapping

class UrlMappingService {

    List<UrlMapping> listUrlMappings() {
        return UrlMapping.listOrderByMatchOrder()
    }
}
