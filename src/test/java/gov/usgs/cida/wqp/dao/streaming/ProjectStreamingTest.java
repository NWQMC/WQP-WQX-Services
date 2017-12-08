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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import gov.usgs.cida.wqp.BaseSpringTest;
import gov.usgs.cida.wqp.dao.NameSpace;
import gov.usgs.cida.wqp.dao.intfc.IStreamingDao;
import gov.usgs.cida.wqp.mapping.BaseColumn;
import gov.usgs.cida.wqp.mapping.StationColumn;
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

	public static final String ARS_SITE = "ARSSite";
	public static final String NWIS_SITE = "NWISSite";
	public static final String STORET_SITE = "STORETSite";
	public static final String BIODATA_SITE = "Unknown";

	public static final String[] STEWARDS_36 = new String[]{STEWARDS, "ARS-IAWC-IAWC225", ARS_SITE};
	public static final String[] STEWARDS_46 = new String[]{STEWARDS, "ARS-IAWC-IAWC410", ARS_SITE};
	public static final String[] NWIS_1353690 = new String[]{NWIS, "USGS-05425700", NWIS_SITE};
	public static final String[] NWIS_1360035 = new String[]{NWIS, "USGS-431925089002701", NWIS_SITE};
	public static final String[] STORET_777 = new String[]{STORET, "organization-siteId2", STORET_SITE};
	public static final String[] STORET_888 = new String[]{STORET, "organization-siteId", STORET_SITE};
	public static final String[] STORET_999 = new String[]{STORET, "organization-siteId3", STORET_SITE};
	public static final String[] STORET_1383 = new String[]{STORET, "WIDNR_WQX-113086", STORET_SITE};
	public static final String[] STORET_436723 = new String[]{STORET, "WIDNR_WQX-10030952", STORET_SITE};
	public static final String[] STORET_504707 = new String[]{STORET, "21NYDECA_WQX-ONTARIO-02", STORET_SITE};
	public static final String[] STORET_1043441 = new String[]{STORET, "11NPSWRD-BICA_MFG_B", STORET_SITE};
	public static final String[] BIODATA_61233184 = new String[]{BIODATA, "USGS-11421000", BIODATA_SITE};
	public static final String[] BIODATA_433830088977331 = new String[]{BIODATA, "USGS-433830088977331", BIODATA_SITE};

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
	//Activity + Activity_Sum

	@Test
	public void nullParameterTest() {
		streamingDao.stream(NameSpace.PROJECT.PROJECT, null, handler);
		assertEquals(TOTAL_PROJECT_COUNT, String.valueOf(handler.getResults().size()));
	}

	@Test
	public void emptyParameterTest() {
		streamingDao.stream(NameSpace.PROJECT.PROJECT, parms, handler);
		assertEquals(TOTAL_PROJECT_COUNT, String.valueOf(handler.getResults().size()));
	}

	protected void mimeTypeTest() {
		parms.put(Parameters.MIMETYPE.toString(), JSON);
		streamingDao.stream(NameSpace.PROJECT, parms, handler);
		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(TOTAL_SITE_COUNT_GEOM, String.valueOf(results.size()));
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_436723, STORET_504707,
				STORET_1043441, BIODATA_61233184);

		handler = new TestResultHandler();
		parms.put(Parameters.MIMETYPE.toString(), GEOJSON);
		streamingDao.stream(NameSpace.PROJECT, parms, handler);
		results = handler.getResults();
		assertEquals(TOTAL_SITE_COUNT_GEOM, String.valueOf(results.size()));
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_436723, STORET_504707,
				STORET_1043441, BIODATA_61233184);

		handler = new TestResultHandler();
		parms.put(Parameters.MIMETYPE.toString(), KML);
		streamingDao.stream(NameSpace.PROJECT, parms, handler);
		results = handler.getResults();
		assertEquals(TOTAL_SITE_COUNT_GEOM, String.valueOf(results.size()));
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_436723, STORET_504707,
				STORET_1043441, BIODATA_61233184);

		handler = new TestResultHandler();
		parms.put(Parameters.MIMETYPE.toString(), KMZ);
		streamingDao.stream(NameSpace.PROJECT, parms, handler);
		results = handler.getResults();
		assertEquals(TOTAL_SITE_COUNT_GEOM, String.valueOf(results.size()));
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_436723, STORET_504707,
				STORET_1043441, BIODATA_61233184);

		handler = new TestResultHandler();
		parms.put(Parameters.MIMETYPE.toString(), CSV);
		streamingDao.stream(NameSpace.PROJECT, parms, handler);
		results = handler.getResults();
		assertEquals(TOTAL_SITE_COUNT, String.valueOf(results.size()));
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_436723, STORET_504707,
				STORET_1043441, BIODATA_61233184, BIODATA_433830088977331);
	}

	@Test
	public void allDataSortedTest() {
		Integer expectedColumnCount = expectedMap.keySet().size();
		parms.put(Parameters.SORTED.toString(), "yes");
		streamingDao.stream(NameSpace.PROJECT.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		//Validate the number AND order of results.
		assertEquals(TOTAL_SITE_COUNT, String.valueOf(results.size()));
		assertRow(results.get(0), STEWARDS_36, expectedColumnCount);
		assertRow(results.get(1), STEWARDS_46, expectedColumnCount);
		assertRow(results.get(2), NWIS_1353690, expectedColumnCount);
		assertRow(results.get(3), NWIS_1360035, expectedColumnCount);
		assertRow(results.get(4), STORET_1043441, expectedColumnCount);
		assertRow(results.get(5), STORET_504707, expectedColumnCount);
		assertRow(results.get(6), STORET_436723, expectedColumnCount);
		assertRow(results.get(7), STORET_1383, expectedColumnCount);
		assertStoret888(expectedMap, results.get(8));
		assertRow(results.get(9), STORET_777, expectedColumnCount);
		assertRow(results.get(10), STORET_999, expectedColumnCount);
		assertRow(results.get(11), BIODATA_61233184, expectedColumnCount);
	}

	@Test
	public void avoidTest() {
		parms.put(Parameters.AVOID.toString().replace(".", ""), getAvoid());
		streamingDao.stream(NameSpace.PROJECT.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(STORET_PROJECT_COUNT, String.valueOf(results.size()));
		assertContainsProject(results, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_436723, STORET_504707, STORET_1043441);
	}

	@Test
	public void bboxTest() {
		parms.put(Parameters.BBOX.toString(), getBBox());
		streamingDao.stream(NameSpace.PROJECT.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(9, results.size());
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_1383, STORET_436723, STORET_1043441);
	}

	@Test
	public void countryTest() {
		parms.put(Parameters.COUNTRY.toString(), getCountry());
		streamingDao.stream(NameSpace.PROJECT.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(11, results.size());
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_436723, STORET_1043441,
				BIODATA_61233184);
	}

	@Test
	public void countyTest() {
		parms.put(Parameters.COUNTY.toString(), getCounty());
		streamingDao.stream(NameSpace.PROJECT.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(10, results.size());
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_436723, STORET_1043441);
	}

	@Test
	public void huc2Test() {
		parms.put(Parameters.HUC.toString(), getHuc2());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(7, results.size());
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_1383, STORET_436723, STORET_1043441);
	}

	@Test
	public void huc4Test() {
		parms.put(Parameters.HUC.toString(), getHuc4());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(4, results.size());
		assertContainsProject(results, NWIS_1353690, NWIS_1360035, STORET_1383, STORET_436723);
	}

	@Test
	public void huc6Test() {
		parms.put(Parameters.HUC.toString(), getHuc6());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(3, results.size());
		assertContainsProject(results, NWIS_1353690, STORET_1383, STORET_436723);
	}

	@Test
	public void huc8Test() {
		parms.put(Parameters.HUC.toString(), getHuc8());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(2, results.size());
		assertContainsProject(results, STORET_1383, STORET_436723);
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
		assertEquals(3, results.size());
		assertContainsProject(results, STORET_777, STORET_888, STORET_1383);
	}

	@Test
	public void organizationTest() {
		parms.put(Parameters.ORGANIZATION.toString(), getOrganization());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(10, results.size());
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_436723, STORET_1043441);
	}

	@Test
	public void providersTest() {
		parms.put(Parameters.PROVIDERS.toString(), getProviders());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(11, results.size());
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_436723, STORET_504707,
				STORET_1043441);
	}

	@Test
	public void siteIdTest() {
		parms.put(Parameters.SITEID.toString(), getSiteid());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(9, results.size());
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_436723, STORET_1043441);
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
		assertContainsProject(results, STORET_777, STORET_888, STORET_1383);
	}

	@Test
	public void minActivitiesTest() {
		parms.put(Parameters.MIN_ACTIVITIES.toString(), getMinActivities());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(7, results.size());
		assertContainsProject(results, STEWARDS_36, NWIS_1353690, STORET_777, STORET_888, STORET_1383, STORET_504707, STORET_1043441);
	}

	@Test
	public void minResultsTest() {
		parms.put(Parameters.MIN_RESULTS.toString(), getMinResults());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(6, results.size());
		assertContainsProject(results, NWIS_1353690, STORET_777, STORET_888, STORET_1383, STORET_504707, STORET_1043441);
	}

	@Test
	public void siteTypeTest() {
		parms.put(Parameters.SITE_TYPE.toString(), getSiteType());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(11, results.size());
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_436723, STORET_504707, STORET_1043441,
				BIODATA_61233184);
	}

	@Test
	public void stateTest() {
		parms.put(Parameters.STATE.toString(), getState());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(10, results.size());
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_436723, STORET_1043441);
	}

	@Test
	public void withinTest() {
		parms.put(Parameters.WITHIN.toString(), getWithin());
		parms.put(Parameters.LATITUDE.toString(), getLatitude());
		parms.put(Parameters.LONGITUDE.toString(), getLongitude());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(10, results.size());
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_436723, STORET_504707);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Station + Activity_Sum

	@Test
	public void projectTest() {
		parms.put(Parameters.PROJECT.toString(), getProject());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(9, results.size());
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1043441, BIODATA_61233184);
	}

	@Test
	public void sampleMediaTest() {
		parms.put(Parameters.SAMPLE_MEDIA.toString(), getSampleMedia());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(11, results.size());
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_504707, STORET_1043441,
				BIODATA_61233184);
	}

	@Test
	public void startDateHiTest() {
		parms.put(Parameters.START_DATE_HI.toString(), getStartDateHi());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(11, results.size());
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1383, STORET_504707, STORET_1043441,
				BIODATA_61233184);
	}

	@Test
	public void startDateLoTest() {
		parms.put(Parameters.START_DATE_LO.toString(), getStartDateLo());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(9, results.size());
		assertContainsProject(results, STEWARDS_36, STEWARDS_46, NWIS_1353690, NWIS_1360035, STORET_777, STORET_888, STORET_999, STORET_1043441, BIODATA_61233184);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Station + Result_Sum

	@Test
	public void analyticalMethodTest() {
		parms.put(Parameters.ANALYTICAL_METHOD.toString(), getAnalyticalMethod());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(5, results.size());
		assertContainsProject(results, NWIS_1353690, STORET_777, STORET_888, STORET_999, STORET_1043441);
	}

	@Test
	public void assemblageTest() {
		parms.put(Parameters.ASSEMBLAGE.toString(), getAssemblage());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(5, results.size());
		assertContainsProject(results, STORET_777, STORET_888, STORET_999, STORET_1043441, BIODATA_61233184);
	}

	@Test
	public void characteristicNameTest() {
		parms.put(Parameters.CHARACTERISTIC_NAME.toString(), getCharacteristicName());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(4, results.size());
		assertContainsProject(results, STORET_777, STORET_888, STORET_999, STORET_1043441);
	}

	@Test
	public void characteristicTypeTest() {
		parms.put(Parameters.CHARACTERISTIC_TYPE.toString(), getCharacteristicType());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(5, results.size());
		assertContainsProject(results, STEWARDS_36, STORET_777, STORET_888, STORET_999, STORET_1043441);
	}

	@Test
	public void pcodeTest() {
		parms.put(Parameters.PCODE.toString(), getPcode());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(4, results.size());
		assertContainsProject(results, NWIS_1360035, STORET_777, STORET_888, STORET_1043441);
	}

	@Test
	public void subjectTaxonomicNameTest() {
		parms.put(Parameters.SUBJECT_TAXONOMIC_NAME.toString(), getSubjectTaxonomicName());
		streamingDao.stream(NameSpace.PROJECT, parms, handler);

		LinkedList<Map<String, Object>> results = handler.getResults();
		assertEquals(5, results.size());
		assertContainsProject(results, STORET_777, STORET_888, STORET_999, STORET_1043441, BIODATA_61233184);
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
		assertEquals(3, results.size());
		assertContainsProject(results, NWIS_1353690, STORET_777, STORET_888);
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
		assertEquals(2, results.size());
		assertContainsProject(results, STORET_777, STORET_888);
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
		assertEquals(2, results.size());
		assertContainsProject(results, STORET_777, STORET_888);
	}

	public static void assertRow(Map<String, Object> row, String[] station, int expectedColumnCount) {
		//The KML does not include data_source, it has a style_url
		assertEquals(expectedColumnCount, row.keySet().size());
		if (row.containsKey(StationColumn.KEY_DATA_SOURCE)) {
			assertEquals(station[0], row.get(BaseColumn.KEY_DATA_SOURCE));
		}
		assertEquals(station[1], row.get(StationColumn.KEY_SITE_ID));
		if (row.containsKey(StationKml.KEY_STYLE_URL)) {
			assertEquals(station[2], row.get(StationKml.KEY_STYLE_URL));
		}
	}

	public void assertContainsProject(LinkedList<Map<String, Object>> results, String[]... projects) {
		for (Map<String, Object> result: results) {
			LOG.debut(ProjectColumn.KEY_DATA_SOURCE_ID + ":" + result.get(ProjectColumn.KEY_DATA_SOURCE_ID) + '/' + ProjectColumn.KEY_PROJECT_ID + ":" + result.get(ProjectColumn.KEY_PROJECT_ID));
		}

		for (String[] i : projects) {
			boolean isFound = false;
			for (Map<String, Object> result : results) {
				if (result.containsKey(ProjectColumn.KEY_DATA_SOURCE)
						&& i[0].equalsIgnoreCase(((String) result.get(ProjectColumn.KEY_DATA_SOURCE)))
						&& i[1].equalsIgnoreCase(result.get(ProjectColumn.KEY_SITE_ID).toString())) {
					isFound = true;
					break;
				}
			}
			if (!isFound) {
				fail(StationColumn.KEY_DATA_SOURCE + ":" + i[0] + "/" + StationColumn.KEY_SITE_ID + ":" + i[1] + " was not in the result set.");
			}
		}
		assertEquals("Double check result set expected size", stations.length, results.size());
	}

}
