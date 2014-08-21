class UrlMappings {


    static mappings = {

        // Mappings supported by resource-specific controllers
        // should be added before the default mapping used for
        // resources handled by the default RestfulApiController.

        // name otherthingRestfulApi: "api/other-things/$id"

        // Default controller to handle RESTful API requests.
        // Place URL mappings to specific controllers BEFORE this mapping.
        //
        "/api/$pluralizedResourceName/$id"(controller: 'restfulApi') {
            action = [GET   : "show", PUT: "update",
                      DELETE: "delete"]
            parseRequest = false
            constraints {
                // to constrain the id to numeric, uncomment the following:
                // id matches: /\d+/
            }
        }
        "/api/$pluralizedResourceName"(controller: 'restfulApi') {
            action = [GET: "list", POST: "create"]
            parseRequest = false
        }

        // Support for nested resources. You may add additional URL mappings to handle
        // additional nested resource requirements.
        //
        "/api/$parentPluralizedResourceName/$parentId/$pluralizedResourceName/$id"(controller: 'restfulApi') {
            action = [GET: "show", PUT: "update", DELETE: "delete"]
            parseRequest = false
            constraints {
                // to constrain the id to numeric, uncomment the following:
                // id matches: /\d+/
            }
        }

        "/api/$parentPluralizedResourceName/$parentId/$pluralizedResourceName"(controller: 'restfulApi') {
            action = [GET: "list", POST: "create"]
            parseRequest = false
        }

        // We can also expose 'query-with-POST' URLs by using a different prefix:
        //
        "/qapi/$pluralizedResourceName"(controller: 'restfulApi') {
            action = [GET: "list", POST: "list"]
            parseRequest = false
        }

        "/"(view: "/index")
        "500"(view: '/error')
    }
}