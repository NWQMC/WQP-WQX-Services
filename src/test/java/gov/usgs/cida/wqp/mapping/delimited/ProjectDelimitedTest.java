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
		assertEquals(8, mapping.size());
		Object[] keys = mapping.keySet().toArray();
		assertEquals(ProjectColumn.KEY_ORGANIZATION, keys[0]);
	}

}
