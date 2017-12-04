package gov.usgs.cida.wqp.mapping;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static gov.usgs.cida.wqp.mapping.ProjectColumn.*;
import static gov.usgs.cida.wqp.swagger.model.ProjectCountJson.*;
import static gov.usgs.cida.wqp.swagger.model.StationCountJson.*;

public class TestProjectMap {
	
	public static final Map<String, Object> PROJECT;
	static {
		PROJECT = new LinkedHashMap<String, Object>();
		PROJECT.put(KEY_DATA_SOURCE_ID, BigDecimal.valueOf(3));
		PROJECT.put(KEY_DATA_SOURCE, STORET);
		PROJECT.put(KEY_PROJECT_IDENTIFIER, "YZ123");
		PROJECT.put(KEY_PROJECT_NAME, "Random Statewide Project");
		PROJECT.put(KEY_PROJECT_DESCRIPTION, "A project description");
		PROJECT.put(KEY_SAMPLING_DESIGN_TYPE_CODE, "X");
		PROJECT.put(KEY_QAPP_APPROVED_INDICATOR, "Y");
		PROJECT.put(KEY_QAPP_APPROVAL_AGENCY_NAME, "Name");
		PROJECT.put(KEY_PROJECT_FILE_URL, "fake.gov");
		PROJECT.put(KEY_MONITORING_LOCATION_WEIGHT_URL, "fake2.gov");
	}
	
	private TestProjectMap() {
	}
}
