package com.thomasvitale.demo.keycloak;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This is a Jackson mixin class helps in serialize/deserialize
 * {@link org.keycloak.adapters.RefreshableKeycloakSecurityContext} class.
 * To use this class you need to register it with
 * {@link com.fasterxml.jackson.databind.ObjectMapper}.
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     mapper.addMixIn(RefreshableKeycloakSecurityContext.class, RefreshableKeycloakSecurityContextMixin.class);
 * </pre>
 *
 * <i>Note: This class will save full class name into a property called @class</i>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
		isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonDeserialize(using = RefreshableKeycloakSecurityContextDeserializer.class)
abstract class RefreshableKeycloakSecurityContextMixin {
}
