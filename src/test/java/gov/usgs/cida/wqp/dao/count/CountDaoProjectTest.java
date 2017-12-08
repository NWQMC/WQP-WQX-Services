package gov.usgs.cida.wqp.dao.count;

import static gov.usgs.cida.wqp.swagger.model.StationCountJson.BIODATA;
import static gov.usgs.cida.wqp.swagger.model.StationCountJson.NWIS;
import static gov.usgs.cida.wqp.swagger.model.StationCountJson.STEWARDS;
import static gov.usgs.cida.wqp.swagger.model.StationCountJson.STORET;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import gov.usgs.cida.wqp.CsvDataSetLoader;
import gov.usgs.cida.wqp.DBIntegrationTest;
import gov.usgs.cida.wqp.dao.NameSpace;

import gov.usgs.cida.wqp.BaseSpringTest;
import gov.usgs.cida.wqp.dao.CountDao;
import gov.usgs.cida.wqp.mapping.BaseColumn;
import gov.usgs.cida.wqp.mapping.CountColumn;
import gov.usgs.cida.wqp.parameter.Parameters;


@Category(DBIntegrationTest.class)
@DatabaseSetup("classpath:/testData/csv/")
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
public class CountDaoProjectTest extends BaseSpringTest {
	private static final Logger LOG = LoggerFactory.getLogger(CountDaoProjectTest.class);

	@Autowired
	CountDao countDao;

	Map<String, Object> parms;

	@Before
	public void init() {
		parms = new HashMap<>();
	}

	@After
	public void cleanup() {
		parms = null;
	}

	@Test
	public void nullParameterTest() {
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, null);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 9, TOTAL_PROJECT_COUNT, NWIS_PROJECT_COUNT, STEWARDS_PROJECT_COUNT, STORET_PROJECT_COUNT, BIODATA_PROJECT_COUNT);
	}

	@Test
	public void emptyParameterTest() {
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, parms);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 9, TOTAL_PROJECT_COUNT, NWIS_PROJECT_COUNT, STEWARDS_PROJECT_COUNT, STORET_PROJECT_COUNT, BIODATA_PROJECT_COUNT);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Single Parameter Counts against activity_sum

	@Test
	public void sampleMediaTest() {
		parms.put(Parameters.SAMPLE_MEDIA.toString(), getSampleMedia());
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, parms);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 5, "9", "1", "1", "6", "1");
	}
	// left off here
	@Test
	public void startDateHiTest() {
		parms.put(Parameters.START_DATE_HI.toString(), getStartDateHi());
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, parms);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 5, "11", "2", "2", "6", "1");
	}

	@Test
	public void startDateLoTest() {
		parms.put(Parameters.START_DATE_LO.toString(), getStartDateLo());
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, parms);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 5, "9", "2", "2", "4", "1");
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Single Parameter Counts against result_sum

	@Test
	public void analyticalMethodTest() {
		parms.put(Parameters.ANALYTICAL_METHOD.toString(), getAnalyticalMethod());
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, parms);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 3, "5", "1", null, "4", null);
	}

	@Test
	public void assemblageTest() {
		parms.put(Parameters.ASSEMBLAGE.toString(), getAssemblage());
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, parms);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 3, "5", null, null, "4", "1");
	}

	@Test
	public void characteristicNameTest() {
		parms.put(Parameters.CHARACTERISTIC_NAME.toString(), getCharacteristicName());
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, parms);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 2, "4", null, null, "4", null);
	}

	@Test
	public void characteristicTypeTest() {
		parms.put(Parameters.CHARACTERISTIC_TYPE.toString(), getCharacteristicType());
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, parms);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 3, "5", null, "1", "4", null);
	}

	@Test
	public void pcodeTest() {
		parms.put(Parameters.PCODE.toString(), getPcode());
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, parms);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 3, "4", "1", null, "3", null);
	}

	@Test
	public void subjectTaxonomicNameTest() {
		parms.put(Parameters.SUBJECT_TAXONOMIC_NAME.toString(), getSubjectTaxonomicName());
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, parms);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 3, "5", null, null, "4", "1");
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	public void multipleParameterActivitySumTest() {
		parms.put(Parameters.AVOID.toString().replace(".", ""), getAvoid());
		parms.put(Parameters.BBOX.toString(), getBBox());
		parms.put(Parameters.COUNTRY.toString(), getCountry());
		parms.put(Parameters.COUNTY.toString(), getCounty());
		parms.put(Parameters.HUC.toString(), getHuc());
		parms.put(Parameters.LATITUDE.toString(), getLatitude());
		parms.put(Parameters.LONGITUDE.toString(), getLongitude());
		parms.put(Parameters.MIN_ACTIVITIES.toString(), getMinActivities());
		parms.put(Parameters.MIN_RESULTS.toString(), getMinResults());
		parms.put(Parameters.NLDIURL.toString(), getNldiSites());
		parms.put(Parameters.ORGANIZATION.toString(), getOrganization());
		parms.put(Parameters.PROVIDERS.toString(), getProviders());
		parms.put(Parameters.SITEID.toString(), getSiteid());
		parms.put(Parameters.SITE_TYPE.toString(), getSiteType());
		parms.put(Parameters.STATE.toString(), getState());
		parms.put(Parameters.WITHIN.toString(), getWithin());
		parms.put(Parameters.AVOID.toString().replace(".", ""), getAvoid());
		parms.put(Parameters.MIN_ACTIVITIES.toString(), getMinActivities());
		parms.put(Parameters.MIN_RESULTS.toString(), getMinResults());
		parms.put(Parameters.PROJECT.toString(), getProject());
		parms.put(Parameters.SAMPLE_MEDIA.toString(), getSampleMedia());
		parms.put(Parameters.START_DATE_HI.toString(), getStartDateHi());
		parms.put(Parameters.START_DATE_LO.toString(), getStartDateLo());
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, parms);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 2, "1", null, null, "1", null);
	}

	@Test
	public void multipleParameterActivitySumStationSumTest() {
		parms.put(Parameters.AVOID.toString().replace(".", ""), getAvoid());
		parms.put(Parameters.BBOX.toString(), getBBox());
		parms.put(Parameters.LATITUDE.toString(), getLatitude());
		parms.put(Parameters.LONGITUDE.toString(), getLongitude());
		parms.put(Parameters.MIN_ACTIVITIES.toString(), getMinActivities());
		parms.put(Parameters.MIN_RESULTS.toString(), getMinResults());
		parms.put(Parameters.PROJECT.toString(), getProject());
		parms.put(Parameters.SAMPLE_MEDIA.toString(), getSampleMedia());
		parms.put(Parameters.START_DATE_HI.toString(), getStartDateHi());
		parms.put(Parameters.START_DATE_LO.toString(), getStartDateLo());
		parms.put(Parameters.WITHIN.toString(), getWithin());
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, parms);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 2, "2", null, null, "2", null);
	}

	@Test
	public void multipleParameterResultSumTest() {
		parms.put(Parameters.ANALYTICAL_METHOD.toString(), getAnalyticalMethod());
		parms.put(Parameters.AVOID.toString().replace(".", ""), getAvoid());
		parms.put(Parameters.ASSEMBLAGE.toString(), getAssemblage());
		parms.put(Parameters.CHARACTERISTIC_NAME.toString(), getCharacteristicName());
		parms.put(Parameters.CHARACTERISTIC_TYPE.toString(), getCharacteristicType());
		parms.put(Parameters.COUNTRY.toString(), getCountry());
		parms.put(Parameters.COUNTY.toString(), getCounty());
		parms.put(Parameters.HUC.toString(), getHuc());
		parms.put(Parameters.MIN_ACTIVITIES.toString(), getMinActivities());
		parms.put(Parameters.MIN_RESULTS.toString(), getMinResults());
		parms.put(Parameters.NLDIURL.toString(), getNldiSites());
		parms.put(Parameters.ORGANIZATION.toString(), getOrganization());
		parms.put(Parameters.PCODE.toString(), getPcode());
		parms.put(Parameters.PROJECT.toString(), getProject());
		parms.put(Parameters.PROVIDERS.toString(), getProviders());
		parms.put(Parameters.SAMPLE_MEDIA.toString(), getSampleMedia());
		parms.put(Parameters.SITEID.toString(), getSiteid());
		parms.put(Parameters.SITE_TYPE.toString(), getSiteType());
		parms.put(Parameters.STATE.toString(), getState());
		parms.put(Parameters.START_DATE_HI.toString(), getStartDateHi());
		parms.put(Parameters.START_DATE_LO.toString(), getStartDateLo());
		parms.put(Parameters.SUBJECT_TAXONOMIC_NAME.toString(), getSubjectTaxonomicName());
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, parms);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 2, "2", null, null, "2", null);
	}

	@Test
	public void multipleParameterResultSumActivitySumTests() {
		parms.clear();
		parms.put(Parameters.ANALYTICAL_METHOD.toString(), getAnalyticalMethod());
		parms.put(Parameters.AVOID.toString().replace(".", ""), getAvoid());
		parms.put(Parameters.ASSEMBLAGE.toString(), getAssemblage());
		parms.put(Parameters.BBOX.toString(), getBBox());
		parms.put(Parameters.CHARACTERISTIC_NAME.toString(), getCharacteristicName());
		parms.put(Parameters.CHARACTERISTIC_TYPE.toString(), getCharacteristicType());
		parms.put(Parameters.COUNTRY.toString(), getCountry());
		parms.put(Parameters.COUNTY.toString(), getCounty());
		parms.put(Parameters.HUC.toString(), getHuc());
		parms.put(Parameters.LATITUDE.toString(), getLatitude());
		parms.put(Parameters.LONGITUDE.toString(), getLongitude());
		parms.put(Parameters.MIN_ACTIVITIES.toString(), getMinActivities());
		parms.put(Parameters.MIN_RESULTS.toString(), getMinResults());
		parms.put(Parameters.NLDIURL.toString(), getNldiSites());
		parms.put(Parameters.ORGANIZATION.toString(), getOrganization());
		parms.put(Parameters.PCODE.toString(), getPcode());
		parms.put(Parameters.PROJECT.toString(), getProject());
		parms.put(Parameters.PROVIDERS.toString(), getProviders());
		parms.put(Parameters.SAMPLE_MEDIA.toString(), getSampleMedia());
		parms.put(Parameters.SITEID.toString(), getSiteid());
		parms.put(Parameters.SITE_TYPE.toString(), getSiteType());
		parms.put(Parameters.START_DATE_HI.toString(), getStartDateHi());
		parms.put(Parameters.START_DATE_LO.toString(), getStartDateLo());
		parms.put(Parameters.STATE.toString(), getState());
		parms.put(Parameters.SUBJECT_TAXONOMIC_NAME.toString(), getSubjectTaxonomicName());
		parms.put(Parameters.WITHIN.toString(), getWithin());
		List<Map<String, Object>> counts = countDao.getCounts(NameSpace.PROJECT, parms);
		assertResults(counts, CountColumn.KEY_PROJECT_COUNT, 2, FILTERED_TOTAL_SITE_COUNT, null, null, FILTERED_STORET_SITE_COUNT, null);
	}

	protected void assertResults(List<Map<String, Object>> counts, String countType, int expectedSize,
			String expectedTotal, String expectedNwis, String expectedStewards, String expectedStoret,
			String expectedBiodata) {
		assertEquals("Number of counts", expectedSize, counts.size());
		boolean nwis = (null == expectedNwis);
		boolean stewards = (null == expectedStewards);
		boolean storet = (null == expectedStoret);
		boolean biodata = (null == expectedBiodata);
		boolean total = (null == expectedTotal);
		for (int i = 0 ; i < counts.size() ; i++) {
			if (null == counts.get(i).get(BaseColumn.KEY_DATA_SOURCE)) {
				assertEquals("total " + countType + " count", expectedTotal, counts.get(i).get(countType).toString());
				total = true;
			} else {
				switch (counts.get(i).get(BaseColumn.KEY_DATA_SOURCE).toString()) {
				case NWIS:
					assertEquals("NWIS " + countType + " count", expectedNwis, counts.get(i).get(countType).toString());
					nwis = true;
					break;
				case STEWARDS:
					assertEquals("STEWARDS " + countType + " count", expectedStewards, counts.get(i).get(countType).toString());
					stewards = true;
					break;
				case STORET:
					assertEquals("STORET " + countType + " count", expectedStoret, counts.get(i).get(countType).toString());
					storet = true;
					break;
				case BIODATA:
					assertEquals("BIODATA " + countType + " count", expectedBiodata, counts.get(i).get(countType).toString());
					biodata = true;
					break;
				default:
					break;
				}
			}
		}
		assertTrue("Did not get " + countType + " Total", total);
		assertTrue("Did not get " + countType + " NWIS", nwis);
		assertTrue("Did not get " + countType + " STEWARDS", stewards);
		assertTrue("Did not get " + countType + " STORET", storet);
		assertTrue("Did not get " + countType + " BIODATA", biodata);
	}

}
