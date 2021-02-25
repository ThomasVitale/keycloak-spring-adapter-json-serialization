package com.thomasvitale.demo.keycloak;

import java.security.Principal;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;

/**
 * This is a Jackson mixin class helps in serialize/deserialize
 * {@link org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount} class.
 * To use this class you need to register it with
 * {@link com.fasterxml.jackson.databind.ObjectMapper}.
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     mapper.addMixIn(SimpleKeycloakAccount.class, SimpleKeycloakAccountMixin.class);
 * </pre>
 *
 * <i>Note: This class will save full class name into a property called @class</i>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
		isGetterVisibility = JsonAutoDetect.Visibility.NONE)
abstract class SimpleKeycloakAccountMixin {

	@JsonCreator
	SimpleKeycloakAccountMixin(
			@JsonProperty("principal") Principal principal,
			@JsonProperty("roles") Set<String> roles,
			@JsonProperty("securityContext") RefreshableKeycloakSecurityContext securityContext
	) {}
}
