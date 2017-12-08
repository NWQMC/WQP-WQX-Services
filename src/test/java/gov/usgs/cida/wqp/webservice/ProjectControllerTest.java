package gov.usgs.cida.wqp.webservice;

import static gov.usgs.cida.wqp.swagger.model.ProjectCountJson.HEADER_NWIS_PROJECT_COUNT;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import gov.usgs.cida.wqp.BaseSpringTest;
import gov.usgs.cida.wqp.mapping.Profile;
import gov.usgs.cida.wqp.mapping.delimited.ProjectDelimitedTest;
import gov.usgs.cida.wqp.parameter.Parameters;
import gov.usgs.cida.wqp.util.HttpConstants;

public class ProjectControllerTest extends BaseSpringTest {
	
	protected ProjectController controller;
	
	@Before
	public void setup() {
		controller = new ProjectController(null, null, null, null, null, null, null, null);
	}
	
	@After
	public void tearDown() {
		// Need to manually clear out this thread local
		ProjectController.setCounts(null);
	}
	
	@Test
	public void addCountHeadersTest() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		String countHeader = controller.addCountHeaders(response, BaseControllerTest.getRawCounts());
		assertEquals(HttpConstants.HEADER_TOTAL_PROJECT_COUNT, countHeader);
		assertEquals(BaseControllerTest.TEST_NWIS_PROJECT_COUNT, response.getHeaderValue(HEADER_NWIS_PROJECT_COUNT));
		assertEquals(BaseControllerTest.TEST_TOTAL_PROJECT_COUNT, response.getHeaderValue(HttpConstants.HEADER_TOTAL_PROJECT_COUNT));
	}
	
	@Test
	public void getMappingTest() {
		ProjectDelimitedTest.assertProjectProfile(controller.getMapping(Profile.PROJECT));
	}
	
	@Test
	public void determineProfileTest() {
		assertEquals(Profile.PROJECT, controller.determineProfile(null));
		
		Map<String, Object> pm = new HashMap<>();
		pm.put(Parameters.DATA_PROFILE.toString(), new String[] {"biological"});
		assertEquals(Profile.BIOLOGICAL, controller.determineProfile(pm));
	}

}
