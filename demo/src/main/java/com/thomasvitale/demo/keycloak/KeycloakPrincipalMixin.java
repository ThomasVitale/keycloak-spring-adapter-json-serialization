package com.thomasvitale.demo.keycloak;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.keycloak.KeycloakSecurityContext;

/**
 * This is a Jackson mixin class helps in serialize/deserialize
 * {@link org.keycloak.KeycloakPrincipal} class.
 * To use this class you need to register it with
 * {@link com.fasterxml.jackson.databind.ObjectMapper}.
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     mapper.addMixIn(KeycloakPrincipal.class, KeycloakPrincipalMixin.class);
 * </pre>
 *
 * <i>Note: This class will save full class name into a property called @class</i>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
		isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = KeycloakPrincipleDeserializer.class)
abstract class KeycloakPrincipalMixin<T extends KeycloakSecurityContext> {

	@JsonCreator
	KeycloakPrincipalMixin(@JsonProperty("name") String name, @JsonProperty("context") T context) {}

	@JsonProperty("name") abstract String getName();
	@JsonProperty("context") abstract T getKeycloakSecurityContext();
}
