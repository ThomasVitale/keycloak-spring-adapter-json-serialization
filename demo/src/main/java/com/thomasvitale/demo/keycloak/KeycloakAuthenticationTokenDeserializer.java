package com.thomasvitale.demo.keycloak;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

import org.springframework.security.core.GrantedAuthority;

/**
 * Custom deserializer for {@link KeycloakAuthenticationToken}. At the time of
 * deserialization it will invoke suitable constructor depending on the value of
 * <b>authorities</b> property. It will ensure that the token's state must not change.
 * <p>
 * This deserializer is already registered with
 * {@link KeycloakAuthenticationTokenMixin} but you can also registered it with
 * your own mixin class.
 *
 * @see KeycloakAuthenticationTokenMixin
 */
public class KeycloakAuthenticationTokenDeserializer extends JsonDeserializer<KeycloakAuthenticationToken> {

	private static final TypeReference<List<GrantedAuthority>> GRANTED_AUTHORITY_LIST = new TypeReference<>() {};

	/**
	 * This method construct {@link KeycloakAuthenticationToken} object from
	 * serialized json.
	 * @param jp the JsonParser
	 * @param ctxt the DeserializationContext
	 * @return the user
	 * @throws IOException if a exception during IO occurs
	 * @throws JsonProcessingException if an error during JSON processing occurs
	 */
	@Override
	public KeycloakAuthenticationToken deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		JsonNode jsonNode = mapper.readTree(jp);
		boolean interactive = readJsonNode(jsonNode, "interactive").asBoolean();
		List<GrantedAuthority> authorities = mapper.readValue(readJsonNode(jsonNode, "authorities").traverse(mapper),
				GRANTED_AUTHORITY_LIST);
		JsonNode detailsNode = readJsonNode(jsonNode, "details");
		OidcKeycloakAccount details = getDetails(mapper, detailsNode);
		return (authorities == null || authorities.isEmpty())
				? new KeycloakAuthenticationToken(details, interactive)
				: new KeycloakAuthenticationToken(details, interactive, authorities);
	}

	private OidcKeycloakAccount getDetails(ObjectMapper mapper, JsonNode detailsNode) throws IOException {
		return mapper.readValue(detailsNode.traverse(mapper), OidcKeycloakAccount.class);
	}

	private JsonNode readJsonNode(JsonNode jsonNode, String field) {
		return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
	}
}
