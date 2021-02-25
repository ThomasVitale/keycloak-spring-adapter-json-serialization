package com.thomasvitale.demo.keycloak;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.common.util.Base64Url;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;
import org.keycloak.util.JsonSerialization;

/**
 * Custom deserializer for {@link RefreshableKeycloakSecurityContext}. At the time of
 * deserialization it will invoke suitable constructor depending on the value of
 * <b>authorities</b> property. It will ensure that the token's state must not change.
 * <p>
 * This deserializer is already registered with
 * {@link RefreshableKeycloakSecurityContextMixin} but you can also registered it with
 * your own mixin class.
 *
 * @see RefreshableKeycloakSecurityContextMixin
 */
public class RefreshableKeycloakSecurityContextDeserializer extends JsonDeserializer<RefreshableKeycloakSecurityContext> {

	/**
	 * This method construct {@link RefreshableKeycloakSecurityContext} object from
	 * serialized json.
	 * @param jp the JsonParser
	 * @param ctxt the DeserializationContext
	 * @return the user
	 * @throws IOException if a exception during IO occurs
	 * @throws JsonProcessingException if an error during JSON processing occurs
	 */
	@Override
	public RefreshableKeycloakSecurityContext deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		JsonNode jsonNode = mapper.readTree(jp);
		String refreshToken = readJsonNode(jsonNode, "refreshToken").asText();
		String tokenString = readJsonNode(jsonNode, "tokenString").asText();
		String idTokenString = readJsonNode(jsonNode, "idTokenString").asText();

		AccessToken token = parseToken(tokenString, AccessToken.class);
		IDToken idToken = parseToken(idTokenString, IDToken.class);

		return new RefreshableKeycloakSecurityContext(
				null,
				null,
				tokenString,
				token,
				idTokenString,
				idToken,
				refreshToken
		);
	}

	private <T> T parseToken(String encoded, Class<T> clazz) throws IOException {
		if (encoded == null || encoded.equals("") || encoded.equals("null"))
			return null;

		String[] parts = encoded.split("\\.");
		if (parts.length < 2 || parts.length > 3) {
			throw new IllegalArgumentException("Keycloak token parsing failed for class: " + clazz.getName());
		}

		byte[] bytes = Base64Url.decode(parts[1]);
		return JsonSerialization.readValue(bytes, clazz);
	}

	private JsonNode readJsonNode(JsonNode jsonNode, String field) {
		return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
	}
}
