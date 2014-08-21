package com.holgergarcia

import grails.transaction.Transactional

@Transactional
class PadreService {

    List<Padre> list(Map params) {

        params.max = params.max ?: DEFAULT_MAX
        Padre.list(params)
    }


    Long count(Map params) {
        Padre.count()
    }
}
