package com.thomasvitale.demo.keycloak;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

/**
 * Custom deserializer for {@link KeycloakPrincipal}. At the time of
 * deserialization it will invoke suitable constructor depending on the value of
 * <b>authorities</b> property. It will ensure that the token's state must not change.
 * <p>
 * This deserializer is already registered with
 * {@link KeycloakPrincipalMixin} but you can also registered it with
 * your own mixin class.
 *
 * @see KeycloakPrincipalMixin
 */
public class KeycloakPrincipleDeserializer extends JsonDeserializer<KeycloakPrincipal> {

	/**
	 * This method construct {@link KeycloakPrincipal} object from
	 * serialized json.
	 * @param jp the JsonParser
	 * @param ctxt the DeserializationContext
	 * @return the user
	 * @throws IOException if a exception during IO occurs
	 * @throws JsonProcessingException if an error during JSON processing occurs
	 */
	@Override
	public KeycloakPrincipal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		JsonNode jsonNode = mapper.readTree(jp);
		String name = readJsonNode(jsonNode, "name").asText();
		JsonNode contextNode = readJsonNode(jsonNode, "context");
		KeycloakSecurityContext context = getContext(mapper, contextNode);
		return new KeycloakPrincipal(name, context);
	}

	private KeycloakSecurityContext getContext(ObjectMapper mapper, JsonNode contextNode) throws IOException {
		return mapper.readValue(contextNode.traverse(mapper), KeycloakSecurityContext.class);
	}

	private JsonNode readJsonNode(JsonNode jsonNode, String field) {
		return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
	}
}
