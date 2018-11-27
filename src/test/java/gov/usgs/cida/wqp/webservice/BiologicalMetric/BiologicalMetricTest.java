package gov.usgs.cida.wqp.webservice.BiologicalMetric;

import gov.usgs.cida.wqp.BaseTest;
import static gov.usgs.cida.wqp.swagger.model.ActivityCountJson.HEADER_NWIS_ACTIVITY_COUNT;
import static gov.usgs.cida.wqp.swagger.model.ResDetectQntLmtCountJson.HEADER_NWIS_RES_DETECT_QNT_LMT_COUNT;
import static gov.usgs.cida.wqp.swagger.model.ResultCountJson.HEADER_NWIS_RESULT_COUNT;
import static gov.usgs.cida.wqp.swagger.model.StationCountJson.HEADER_NWIS_SITE_COUNT;
import gov.usgs.cida.wqp.util.HttpConstants;
import gov.usgs.cida.wqp.webservice.BaseControllerTest;
import gov.usgs.cida.wqp.webservice.biological.BiologicalMetricController;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

public class BiologicalMetricTest extends BaseTest {
	
	protected BiologicalMetricController controller;

	@Before
	public void setup() {
		controller = new BiologicalMetricController(null, null, null, null, null, null, null);
		BiologicalMetricController.remove();
	}

	@Test
	public void addCountHeadersTest() {
		MockHttpServletResponse response = new MockHttpServletResponse();
//		String countHeader = controller.addCountHeaders(response, BaseControllerTest.getRawCounts());
//		assertEquals(HttpConstants.HEADER_TOTAL_RESULT_COUNT, countHeader);
//		assertEquals(BaseControllerTest.TEST_NWIS_STATION_COUNT, response.getHeaderValue(HEADER_NWIS_SITE_COUNT));
//		assertEquals(BaseControllerTest.TEST_TOTAL_STATION_COUNT, response.getHeaderValue(HttpConstants.HEADER_TOTAL_SITE_COUNT));
//		assertEquals(BaseControllerTest.TEST_NWIS_ACTIVITY_COUNT, response.getHeaderValue(HEADER_NWIS_ACTIVITY_COUNT));
//		assertEquals(BaseControllerTest.TEST_TOTAL_ACTIVITY_COUNT, response.getHeaderValue(HttpConstants.HEADER_TOTAL_ACTIVITY_COUNT));
//		assertEquals(BaseControllerTest.TEST_NWIS_RESULT_COUNT, response.getHeaderValue(HEADER_NWIS_RESULT_COUNT));
//		assertEquals(BaseControllerTest.TEST_TOTAL_RESULT_COUNT, response.getHeaderValue(HttpConstants.HEADER_TOTAL_RESULT_COUNT));
//		assertFalse(response.containsHeader(HEADER_NWIS_RES_DETECT_QNT_LMT_COUNT));
//		assertFalse(response.containsHeader(HttpConstants.HEADER_TOTAL_RES_DETECT_QNT_LMT_COUNT));
	}

	@Test
	public void getMappingTest() {
//		ResultDelimitedTest.assertBiologicalProfile(controller.getMapping(Profile.BIOLOGICAL));
//		ResultDelimitedTest.assertPcResultProfile(controller.getMapping(Profile.PC_RESULT));
	}

	@Test
	public void determineProfileTest() {
//		assertEquals(Profile.PC_RESULT, controller.determineProfile(null));
//
//		FilterParameters filter = new FilterParameters();
//		filter.setDataProfile("biological");
//		assertEquals(Profile.BIOLOGICAL, controller.determineProfile(filter));
	}
	
}