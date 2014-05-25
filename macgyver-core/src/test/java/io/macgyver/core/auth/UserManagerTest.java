package io.macgyver.core.auth;

import io.macgyver.test.MacGyverIntegrationTest;

import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.neo4j.graphdb.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class UserManagerTest extends MacGyverIntegrationTest {

	@Autowired
	InternalUserManager userManager;

	@Test
	public void testAutowire() {
		Assert.assertNotNull(userManager);

	}

	@Test
	public void testAuthentication() {

		Assert.assertFalse(userManager.authenticate("admin", "xxx"));

	}

	@Test
	public void testAuthenticateFailureWithMissingUser() {

		Assert.assertFalse(userManager.authenticate("user_not_found", "xxx"));
	}

	@Test
	public void testUpdateRoles() {
		String username = "user_" + UUID.randomUUID().toString();
		userManager.createUser(username, Lists.newArrayList("ROLE_A"));
		
		Assert.assertTrue(userManager.getInternalUser(username).get().getRoles().contains("ROLE_A"));
		Assert.assertFalse(userManager.getInternalUser(username).get().getRoles().contains("ROLE_B"));
		userManager.setRoles(username, Lists.newArrayList("ROLE_A","ROLE_B"));
		
		Assert.assertTrue(userManager.getInternalUser(username).get().getRoles().contains("ROLE_A"));
		Assert.assertTrue(userManager.getInternalUser(username).get().getRoles().contains("ROLE_B"));
	}
	@Test
	public void testSaveAndLoad() {
		String username = "user_" + UUID.randomUUID().toString();

		userManager.createUser(username, Lists.newArrayList("MAC"));
		Assert.assertFalse(userManager.authenticate(username, "abc123"));
		userManager.setPassword(username, "abc123");
		Assert.assertTrue(userManager.authenticate(username, "abc123"));

	}

	@Test(expected = ConstraintViolationException.class)
	public void testUnique() {
		String username = "bob";
		List<String> roles = Lists.newArrayList();

		userManager.createUser(username, roles);

		Assert.assertTrue(userManager.getInternalUser("bob").isPresent());

		userManager.createUser(username, roles);

	}

}
