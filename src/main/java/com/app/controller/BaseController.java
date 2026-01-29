package com.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public class BaseController {
	/**
	 * Returns a standardized success response for delete operations.
	 * All delete endpoints should use this method to ensure consistent responses.
	 * 
	 * @param resourceName The name of the resource that was deleted (e.g., "World",
	 *                     "User")
	 * @return ResponseEntity with a success message
	 */
	protected ResponseEntity<Map<String, String>> deleteSuccessResponse(String resourceName) {
		Map<String, String> response = new HashMap<>();
		response.put("message", resourceName + " deleted successfully");
		return ResponseEntity.ok(response);
	}
}
