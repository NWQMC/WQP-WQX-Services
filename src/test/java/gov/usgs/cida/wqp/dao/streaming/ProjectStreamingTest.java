package gov.usgs.cida.wqp.dao.streaming;

import static gov.usgs.cida.wqp.swagger.model.StationCountJson.BIODATA;
import static gov.usgs.cida.wqp.swagger.model.StationCountJson.NWIS;
import static gov.usgs.cida.wqp.swagger.model.StationCountJson.STEWARDS;
import static gov.usgs.cida.wqp.swagger.model.StationCountJson.STORET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

import gov.usgs.cida.wqp.BaseSpringTest;
import gov.usgs.cida.wqp.CsvDataSetLoader;
import gov.usgs.cida.wqp.DBIntegrationTest;
import gov.usgs.cida.wqp.dao.NameSpace;
import gov.usgs.cida.wqp.dao.intfc.IStreamingDao;
import gov.usgs.cida.wqp.mapping.BaseColumn;
import gov.usgs.cida.wqp.mapping.StationColumn;
import gov.usgs.cida.wqp.mapping.ProjectColumn;
import gov.usgs.cida.wqp.mapping.TestResultHandler;
import gov.usgs.cida.wqp.mapping.xml.StationKml;
import gov.usgs.cida.wqp.parameter.Parameters;

@Category(DBIntegrationTest.class)
@DatabaseSetup("classpath:/testData/csv/")
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
public class ProjectStreamingTest extends BaseSpringTest {
	private static final Logger LOG = LoggerFactory.getLogger(ProjectStreamingTest.class);

	@Autowired
	IStreamingDao streamingDao;

	TestResultHandler handler;
	Map<String, Object> parms;
	
	public static final String [] PROJECT_LAKE_BASELINE = new String[] {STORET, "Lake-BaselineMonitoringDNR"};
	public static final String [] PROJECT_SAM = new String[] {STORET, "SAM"};
	public static final String [] PROJECT_WR047 = new String[] {STORET, "WR047"};
	public static final String [] PROJECT_PROJECTID = new String[] {STORET, "projectId"};
	public static final String [] PROJECT_EPABEACH = new String[] {STORET, "EPABEACH"};
	public static final String [] PROJECT_CEAP = new String[] {STEWARDS, "CEAP"};
	public static final String [] PROJECT_NAWQA = new String[] {NWIS, "NAWQA"};
	public static final String [] PROJECT_SOMETHINGELSE = new String[] {STORET, "SOMETHINGELSE"};
	public static final String [] PROJECT_SACR_BIOTDB = new String[] {BIODATA, "SACR BioTDB"};

	@Before
	public void init() {
		handler = new TestResultHandler();
		parms = new HashMap<>();
	}

	@After
	public void cleanup() {
		handler = null;
		parms = null;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Activity_Sum

	@Test
	public void nullParameterTest() {
		streamingDao.stream(NameSpace.PROJECT, null, handler);
		assertEquals(TOTAL_PROJECT_COUNT, String.valueOf(handler.getResults().size()));
	}

	@Test
	public void emptyParameterTest() {
		streamingDao.stream(NameSpace.PROJECT, parms, handler);
		assertEquals(TOTAL_PROJECT_COUNT, String.valueOf(handler.getResults().size()));
	}

	@Test
	public void avoidTest() {
		parms.put(Parameters.AVOID.toString().replace(".", ""), getAvoid());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(STORET_PROJECT_COUNT, String.valueOf(results.size()));
		assertContainsProject(results, PROJECT_LAKE_BASELINE, PROJECT_WR047, PROJECT_EPABEACH, PROJECT_PROJECTID, PROJECT_SAM, PROJECT_SOMETHINGELSE);
	}

	@Test
	public void bboxTest() {
		parms.put(Parameters.BBOX.toString(), getBBox());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(7, results.size());
		assertContainsProject(results, PROJECT_NAWQA, PROJECT_CEAP, PROJECT_LAKE_BASELINE, PROJECT_WR047, PROJECT_PROJECTID, PROJECT_EPABEACH, PROJECT_SOMETHINGELSE);
	}

	@Test
	public void countryTest() {
		parms.put(Parameters.COUNTRY.toString(), getCountry());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		System.out.println(results);
		assertContainsProject(results, PROJECT_SACR_BIOTDB, PROJECT_NAWQA, PROJECT_CEAP, PROJECT_LAKE_BASELINE, PROJECT_WR047,
				PROJECT_EPABEACH, PROJECT_PROJECTID, PROJECT_SOMETHINGELSE);
	}

	@Test
	public void countyTest() {
		parms.put(Parameters.COUNTY.toString(), getCounty());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(7, results.size());
		assertContainsProject(results, PROJECT_LAKE_BASELINE, PROJECT_WR047, PROJECT_PROJECTID, PROJECT_CEAP,
				PROJECT_NAWQA, PROJECT_EPABEACH, PROJECT_SOMETHINGELSE);
	}

	@Test
	public void huc2Test() {
		parms.put(Parameters.HUC.toString(), getHuc2());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(7, results.size());
		assertContainsProject(results, PROJECT_LAKE_BASELINE, PROJECT_WR047, PROJECT_PROJECTID, PROJECT_CEAP, PROJECT_NAWQA,
				PROJECT_EPABEACH, PROJECT_SOMETHINGELSE);
	}

	@Test
	public void huc4Test() {
		parms.put(Parameters.HUC.toString(), getHuc4());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(3, results.size());
		assertContainsProject(results, PROJECT_LAKE_BASELINE, PROJECT_WR047, PROJECT_NAWQA);
	}

	@Test
	public void huc6Test() {
		parms.put(Parameters.HUC.toString(), getHuc6());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(3, results.size());
		assertContainsProject(results, PROJECT_LAKE_BASELINE, PROJECT_WR047, PROJECT_NAWQA);
	}

	@Test
	public void huc8Test() {
		parms.put(Parameters.HUC.toString(), getHuc8());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(2, results.size());
		assertContainsProject(results, PROJECT_LAKE_BASELINE, PROJECT_WR047);
	}

	@Test
	public void nldiUrlTest() {
		try {
			parms.put(Parameters.NLDIURL.toString(), getManySiteId());
			streamingDao.stream(NameSpace.PROJECT, parms, handler);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		LinkedList<Map<String, Object>> results = handler.getResults();
		System.out.println(results);
		assertEquals(3, results.size());
		assertContainsProject(results, PROJECT_LAKE_BASELINE, PROJECT_WR047, PROJECT_PROJECTID);
	}

	@Test
	public void organizationTest() {
		parms.put(Parameters.ORGANIZATION.toString(), getOrganization());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(7, results.size());
		assertContainsProject(results, PROJECT_LAKE_BASELINE, PROJECT_WR047, PROJECT_PROJECTID, PROJECT_CEAP,
				PROJECT_NAWQA, PROJECT_EPABEACH, PROJECT_SOMETHINGELSE);
	}

	@Test
	public void providersTest() {
		parms.put(Parameters.PROVIDERS.toString(), getProviders());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(8, results.size());
		assertContainsProject(results, PROJECT_LAKE_BASELINE, PROJECT_SAM, PROJECT_WR047, PROJECT_PROJECTID, PROJECT_CEAP,
				PROJECT_NAWQA, PROJECT_EPABEACH, PROJECT_SOMETHINGELSE);
	}

	@Test
	public void siteIdTest() {
		parms.put(Parameters.SITEID.toString(), getSiteid());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(5, results.size());
		assertContainsProject(results, PROJECT_PROJECTID, PROJECT_CEAP, PROJECT_NAWQA, PROJECT_EPABEACH, PROJECT_SOMETHINGELSE);
	}

	@Test
	public void manySitesTest() {
		try {
			parms.put(Parameters.SITEID.toString(), getManySiteId());
			streamingDao.stream(NameSpace.PROJECT, parms, handler);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(3, results.size());
		assertContainsProject(results, PROJECT_LAKE_BASELINE, PROJECT_WR047, PROJECT_PROJECTID);
	}

	@Test
	public void minActivitiesTest() {
		parms.put(Parameters.MIN_ACTIVITIES.toString(), getMinActivities());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(4, results.size());
		assertContainsProject(results, PROJECT_PROJECTID, PROJECT_SAM, PROJECT_NAWQA, PROJECT_CEAP);
	}

	@Test
	public void minResultsTest() {
		parms.put(Parameters.MIN_RESULTS.toString(), getMinResults());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(5, results.size());
		assertContainsProject(results, PROJECT_LAKE_BASELINE, PROJECT_NAWQA, PROJECT_SAM, PROJECT_WR047, PROJECT_PROJECTID);
	}

	@Test
	public void siteTypeTest() {
		parms.put(Parameters.SITE_TYPE.toString(), getSiteType());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(9, results.size());
		assertContainsProject(results, PROJECT_LAKE_BASELINE, PROJECT_SAM, PROJECT_WR047, PROJECT_PROJECTID, PROJECT_CEAP,
				PROJECT_NAWQA, PROJECT_EPABEACH, PROJECT_SOMETHINGELSE, PROJECT_SACR_BIOTDB);
	}

	@Test
	public void stateTest() {
		parms.put(Parameters.STATE.toString(), getState());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(7, results.size());
		assertContainsProject(results, PROJECT_LAKE_BASELINE, PROJECT_WR047, PROJECT_PROJECTID, PROJECT_CEAP,
				PROJECT_NAWQA, PROJECT_EPABEACH, PROJECT_SOMETHINGELSE);
	}
	
	@Test
	public void withinTest() {
		parms.put(Parameters.WITHIN.toString(), getWithin());
		parms.put(Parameters.LATITUDE.toString(), getLatitude());
		parms.put(Parameters.LONGITUDE.toString(), getLongitude());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(6, results.size());
		assertContainsProject(results, PROJECT_NAWQA, PROJECT_CEAP, PROJECT_LAKE_BASELINE, PROJECT_WR047,
				PROJECT_PROJECTID, PROJECT_SAM);
	}

	@Test
	public void projectTest() {
		parms.put(Parameters.PROJECT.toString(), getProject());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(4, results.size());
		assertContainsProject(results, PROJECT_PROJECTID, PROJECT_CEAP, PROJECT_NAWQA, PROJECT_SACR_BIOTDB);
	}

	@Test
	public void sampleMediaTest() {
		parms.put(Parameters.SAMPLE_MEDIA.toString(), getSampleMedia());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(9, results.size());
		assertContainsProject(results, PROJECT_LAKE_BASELINE, PROJECT_SAM, PROJECT_WR047, PROJECT_WR047, PROJECT_PROJECTID,
				PROJECT_CEAP, PROJECT_NAWQA, PROJECT_EPABEACH, PROJECT_SOMETHINGELSE, PROJECT_SACR_BIOTDB);
	}

	@Test
	public void startDateHiTest() {
		parms.put(Parameters.START_DATE_HI.toString(), getStartDateHi());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(9, results.size());
		assertContainsProject(results, PROJECT_SACR_BIOTDB, PROJECT_NAWQA, PROJECT_CEAP, PROJECT_LAKE_BASELINE,
				PROJECT_WR047, PROJECT_EPABEACH, PROJECT_PROJECTID, PROJECT_SAM, PROJECT_SOMETHINGELSE);
	}

	@Test
	public void startDateLoTest() {
		parms.put(Parameters.START_DATE_LO.toString(), getStartDateLo());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(6, results.size());
		assertContainsProject(results, PROJECT_PROJECTID, PROJECT_CEAP, PROJECT_NAWQA, PROJECT_EPABEACH,
				PROJECT_SOMETHINGELSE, PROJECT_SACR_BIOTDB);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Result_Sum

	@Test
	public void analyticalMethodTest() {
		parms.put(Parameters.ANALYTICAL_METHOD.toString(), getAnalyticalMethod());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(4, results.size());
		assertContainsProject(results, PROJECT_PROJECTID, PROJECT_NAWQA, PROJECT_EPABEACH, PROJECT_SOMETHINGELSE);
	}

	@Test
	public void assemblageTest() {
		parms.put(Parameters.ASSEMBLAGE.toString(), getAssemblage());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(4, results.size());
		assertContainsProject(results, PROJECT_PROJECTID, PROJECT_EPABEACH, PROJECT_SOMETHINGELSE, PROJECT_SACR_BIOTDB);
	}

	@Test
	public void characteristicNameTest() {
		parms.put(Parameters.CHARACTERISTIC_NAME.toString(), getCharacteristicName());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(3, results.size());
		assertContainsProject(results, PROJECT_PROJECTID, PROJECT_EPABEACH, PROJECT_SOMETHINGELSE);
	}

	@Test
	public void characteristicTypeTest() {
		parms.put(Parameters.CHARACTERISTIC_TYPE.toString(), getCharacteristicType());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(4, results.size());
		assertContainsProject(results, PROJECT_PROJECTID, PROJECT_CEAP, PROJECT_EPABEACH, PROJECT_SOMETHINGELSE);
	}

	@Test
	public void pcodeTest() {
		parms.put(Parameters.PCODE.toString(), getPcode());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(4, results.size());
		assertContainsProject(results, PROJECT_PROJECTID, PROJECT_NAWQA, PROJECT_EPABEACH, PROJECT_SOMETHINGELSE);
	}

	@Test
	public void subjectTaxonomicNameTest() {
		parms.put(Parameters.SUBJECT_TAXONOMIC_NAME.toString(), getSubjectTaxonomicName());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(2, results.size());
		assertContainsProject(results, PROJECT_PROJECTID, PROJECT_SACR_BIOTDB);
	}

	@Test
	public void multipleParameterStationSumTests() {
		parms.put(Parameters.BBOX.toString(), getBBox());
		parms.put(Parameters.COUNTRY.toString(), getCountry());
		parms.put(Parameters.COUNTY.toString(), getCounty());
		parms.put(Parameters.HUC.toString(), getHuc());
		parms.put(Parameters.LATITUDE.toString(), getLatitude());
		parms.put(Parameters.LONGITUDE.toString(), getLongitude());
		parms.put(Parameters.ORGANIZATION.toString(), getOrganization());
		parms.put(Parameters.PROVIDERS.toString(), getProviders());
		parms.put(Parameters.SITEID.toString(), getSiteid());
		parms.put(Parameters.SITE_TYPE.toString(), getSiteType());
		parms.put(Parameters.STATE.toString(), getState());
		parms.put(Parameters.WITHIN.toString(), getWithin());

		parms.put(Parameters.MIN_ACTIVITIES.toString(), getMinActivities());
		parms.put(Parameters.MIN_RESULTS.toString(), getMinResults());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(1, results.size());
		assertContainsProject(results, PROJECT_PROJECTID);
	}

	@Test
	public void multipleParameterActivitySumTests() {
		parms.put(Parameters.BBOX.toString(), getBBox());
		parms.put(Parameters.COUNTRY.toString(), getCountry());
		parms.put(Parameters.COUNTY.toString(), getCounty());
		parms.put(Parameters.HUC.toString(), getHuc());
		parms.put(Parameters.LATITUDE.toString(), getLatitude());
		parms.put(Parameters.LONGITUDE.toString(), getLongitude());
		parms.put(Parameters.MIN_ACTIVITIES.toString(), getMinActivities());
		parms.put(Parameters.MIN_RESULTS.toString(), getMinResults());
		parms.put(Parameters.ORGANIZATION.toString(), getOrganization());
		parms.put(Parameters.PROVIDERS.toString(), getProviders());
		parms.put(Parameters.SITEID.toString(), getSiteid());
		parms.put(Parameters.SITE_TYPE.toString(), getSiteType());
		parms.put(Parameters.STATE.toString(), getState());
		parms.put(Parameters.WITHIN.toString(), getWithin());

		parms.put(Parameters.PROJECT.toString(), getProject());
		parms.put(Parameters.SAMPLE_MEDIA.toString(), getSampleMedia());
		parms.put(Parameters.START_DATE_HI.toString(), getStartDateHi());
		parms.put(Parameters.START_DATE_LO.toString(), getStartDateLo());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(1, results.size());
		assertContainsProject(results, PROJECT_PROJECTID);
	}

	@Test
	public void multipleParameterResultSumTest() {
		parms.put(Parameters.BBOX.toString(), getBBox());
		parms.put(Parameters.COUNTRY.toString(), getCountry());
		parms.put(Parameters.COUNTY.toString(), getCounty());
		parms.put(Parameters.HUC.toString(), getHuc());
		parms.put(Parameters.LATITUDE.toString(), getLatitude());
		parms.put(Parameters.LONGITUDE.toString(), getLongitude());
		parms.put(Parameters.MIN_ACTIVITIES.toString(), getMinActivities());
		parms.put(Parameters.MIN_RESULTS.toString(), getMinResults());
		parms.put(Parameters.ORGANIZATION.toString(), getOrganization());
		parms.put(Parameters.PROVIDERS.toString(), getProviders());
		parms.put(Parameters.SITEID.toString(), getSiteid());
		parms.put(Parameters.SITE_TYPE.toString(), getSiteType());
		parms.put(Parameters.STATE.toString(), getState());
		parms.put(Parameters.WITHIN.toString(), getWithin());

		parms.put(Parameters.PROJECT.toString(), getProject());
		parms.put(Parameters.SAMPLE_MEDIA.toString(), getSampleMedia());
		parms.put(Parameters.START_DATE_HI.toString(), getStartDateHi());
		parms.put(Parameters.START_DATE_LO.toString(), getStartDateLo());

		parms.put(Parameters.PCODE.toString(), getPcode());
		parms.put(Parameters.ANALYTICAL_METHOD.toString(), getAnalyticalMethod());
		parms.put(Parameters.ASSEMBLAGE.toString(), getAssemblage());
		parms.put(Parameters.CHARACTERISTIC_NAME.toString(), getCharacteristicName());
		parms.put(Parameters.CHARACTERISTIC_TYPE.toString(), getCharacteristicType());
		parms.put(Parameters.SUBJECT_TAXONOMIC_NAME.toString(), getSubjectTaxonomicName());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		System.out.println(results);
		assertEquals(1, results.size());
		assertContainsProject(results, PROJECT_PROJECTID);
	}

	public void assertContainsProject(LinkedList<Map<String, Object>> results, String[]... projects) {
		for (Map<String, Object> result: results) {
			LOG.debug(ProjectColumn.KEY_DATA_SOURCE_ID + ":" + result.get(ProjectColumn.KEY_DATA_SOURCE_ID) + '/' + ProjectColumn.KEY_PROJECT_ID + ":" + result.get(ProjectColumn.KEY_PROJECT_ID));
		}

		for (String[] i : projects) {
			boolean isFound = false;
			for (Map<String, Object> result : results) {
				if (result.containsKey(ProjectColumn.KEY_DATA_SOURCE)
						&& i[0].equalsIgnoreCase(((String) result.get(ProjectColumn.KEY_DATA_SOURCE)))
						&& i[1].equalsIgnoreCase(result.get(ProjectColumn.KEY_PROJECT_IDENTIFIER).toString())) {
					isFound = true;
					break;
				}
			}
			if (!isFound) {
				fail(ProjectColumn.KEY_DATA_SOURCE + ":" + i[0] + "/" + ProjectColumn.KEY_SITE_ID + ":" + i[1] + " was not in the result set.");
			}
		}
		assertEquals("Double check result set expected size", projects.length, results.size());
	}

}
