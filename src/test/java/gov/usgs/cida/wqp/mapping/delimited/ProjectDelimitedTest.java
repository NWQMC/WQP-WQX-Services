package gov.usgs.cida.wqp.mapping.delimited;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import gov.usgs.cida.wqp.mapping.Profile;
import gov.usgs.cida.wqp.mapping.ProjectColumn;

public class ProjectDelimitedTest {
	
	@Test
	public void projectProfileTest() {
		assertProjectProfile(ProjectDelimited.getMapping(Profile.PROJECT));
	}
	
	public static void assertProjectProfile(Map<String, String> mapping) {
		assertEquals(10, mapping.size());
		Object[] keys = mapping.keySet().toArray();
		// System.out.println(keys[7]);
		assertEquals(ProjectColumn.KEY_ORGANIZATION, keys[0]);
		assertEquals(ProjectDelimited.VALUE_ORGANIZATION_IDENTIFIER, mapping.get(keys[0]));
		assertEquals(ProjectColumn.KEY_ORGANIZATION_NAME, keys[1]);
		assertEquals(ProjectDelimited.VALUE_ORGANIZATION_FORMAL_NAME, mapping.get(keys[1]));
		assertEquals(ProjectColumn.KEY_PROJECT_IDENTIFIER, keys[2]);
		assertEquals(ProjectDelimited.VALUE_PROJECT, mapping.get(keys[2]));
		assertEquals(ProjectColumn.KEY_PROJECT_NAME, keys[3]);
		assertEquals(ProjectDelimited.VALUE_PROJECT_NAME, mapping.get(keys[3]));
		assertEquals(ProjectColumn.KEY_PROJECT_DESCRIPTION, keys[4]);
		assertEquals(ProjectDelimited.VALUE_PROJECT_DESCRIPTION_TEXT, mapping.get(keys[4]));
		assertEquals(ProjectColumn.KEY_SAMPLING_DESIGN_TYPE_CODE, keys[5]);
		assertEquals(ProjectDelimited.VALUE_SAMPLING_DESIGN_TYPE_CODE, mapping.get(keys[5]));
		assertEquals(ProjectColumn.KEY_QAPP_APPROVED_INDICATOR, keys[6]);
		assertEquals(ProjectDelimited.VALUE_QAPP_APPROVED_INDICATOR, mapping.get(keys[6]));
		assertEquals(ProjectColumn.KEY_QAPP_APPROVAL_AGENCY_NAME, keys[7]);
		assertEquals(ProjectDelimited.VALUE_QAPP_APPROVAL_AGENCY_NAME, mapping.get(keys[7]));
		assertEquals(ProjectColumn.KEY_PROJECT_FILE_URL, keys[8]);
		assertEquals(ProjectDelimited.VALUE_PROJECT_FILE_URL, mapping.get(keys[8]));
		assertEquals(ProjectColumn.KEY_MONITORING_LOCATION_WEIGHT_URL, keys[9]);
		assertEquals(ProjectDelimited.VALUE_PROJECT_MONITORING_LOCATION_WEIGHT_URL, mapping.get(keys[9]));
	}

}
