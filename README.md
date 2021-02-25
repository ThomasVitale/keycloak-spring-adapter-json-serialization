# Keycloak Spring Adapter JSON Serialization (Jackson)

The Keycloak Spring Adapter doesn't support JSON serialization for the HTTP session objects. If you use Spring Session with Redis, you need to provide custom mixins and deserializer in order to use the `GenericJackson2JsonRedisSerializer` or `Jackson2JsonRedisSerializer`.

Inspired by how Spring Security supports JSON serialization with the `SecurityJackson2Modules`, I implemented a `KeycloakJackson2Modules` to solve the problem.
