package com.thomasvitale.demo.keycloak;

import java.util.HashSet;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

/**
 * Jackson module for the Keycloak adapter. This module register
 * {@link KeycloakAuthenticationToken}, {@link KeycloakPrincipal},
 * {@link HashSet}, {@link RefreshableKeycloakSecurityContext},
 * and {@link SimpleKeycloakAccount}. If no default typing enabled by
 * default then it'll enable it because typing info is needed to properly
 * serialize/deserialize objects. In order to use this module just add this module into
 * your ObjectMapper configuration.
 *
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     mapper.registerModule(new KeycloakCoreJackson2Module());
 * </pre>
 *
 * <b>Note: use {@link KeycloakJackson2Modules#getModules(ClassLoader)} to get list
 * of all security modules.</b>
 */
public class KeycloakCoreJackson2Module extends SimpleModule {

	public KeycloakCoreJackson2Module() {
		super(KeycloakCoreJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
	}

	@Override
	public void setupModule(SetupContext context) {
		KeycloakJackson2Modules.enableDefaultTyping(context.getOwner());
		context.setMixInAnnotations(KeycloakAuthenticationToken.class, KeycloakAuthenticationTokenMixin.class);
		context.setMixInAnnotations(KeycloakPrincipal.class, KeycloakPrincipalMixin.class);
		context.setMixInAnnotations(HashSet.class, HashSetMixin.class);
		context.setMixInAnnotations(RefreshableKeycloakSecurityContext.class, RefreshableKeycloakSecurityContextMixin.class);
		context.setMixInAnnotations(SimpleKeycloakAccount.class, SimpleKeycloakAccountMixin.class);
	}
}
