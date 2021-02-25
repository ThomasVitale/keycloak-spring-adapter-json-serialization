package com.thomasvitale.demo.keycloak;

import java.util.HashSet;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

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
