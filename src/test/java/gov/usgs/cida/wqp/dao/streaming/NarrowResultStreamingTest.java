package gov.usgs.cida.wqp.dao.streaming;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

import gov.usgs.cida.wqp.CsvDataSetLoader;
import gov.usgs.cida.wqp.DBIntegrationTest;
import gov.usgs.cida.wqp.dao.NameSpace;
import gov.usgs.cida.wqp.mapping.ActivityColumn;
import gov.usgs.cida.wqp.mapping.TestNarrowResultMap;

@Category(DBIntegrationTest.class)
@DatabaseSetup("classpath:/testData/csv/")
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
public class NarrowResultStreamingTest extends BaseResultStreamingTest {

	protected NameSpace nameSpace = NameSpace.NARROW_RESULT;
	protected Map<String, Object> expectedMap = TestNarrowResultMap.NARROW;

	@Test
	public void testHarness() {
		activityTest(nameSpace);
		analyticalMethodTest(nameSpace);
		assemblageTest(nameSpace);
		avoidTest(nameSpace);
		bboxTest(nameSpace);
		characteristicNameTest(nameSpace);
		characteristicTypeTest(nameSpace);
		countryTest(nameSpace);
		countyTest(nameSpace);
		emptyParameterTest(nameSpace);
		huc2Test(nameSpace);
		huc3Test(nameSpace);
		huc4Test(nameSpace);
		huc5Test(nameSpace);
		huc6Test(nameSpace);
		huc7Test(nameSpace);
		huc8Test(nameSpace);
		huc10Test(nameSpace);
		huc12Test(nameSpace);
		mimeTypeTest(nameSpace);
		minActivitiesTest(nameSpace);
		minResultsTest(nameSpace);
		nldiSitesTest(nameSpace);
		nldiUrlTest(nameSpace);
		nullParameterTest(nameSpace);
		organizationTest(nameSpace);
		pcodeTest(nameSpace);
		projectTest(nameSpace);
		providersTest(nameSpace);
		resultTest(nameSpace);
		sampleMediaTest(nameSpace);
		siteIdTest(nameSpace);
		siteIdLargeListTest(nameSpace);
		siteTypeTest(nameSpace);
		siteUrlBaseTest(nameSpace);
		sortedTest(nameSpace, expectedMap);
		startDateHiTest(nameSpace);
		startDateLoTest(nameSpace);
		stateTest(nameSpace);
		subjectTaxonomicNameTest(nameSpace);
		withinTest(nameSpace);
		zipTest(nameSpace);
		multipleParameterResultTest(nameSpace);
		multipleParameterResultStationSumTest(nameSpace);
	}

	@Override
	protected void assertSiteUrlBase(Map<String, Object> row) {
		if (null != row.get(ActivityColumn.KEY_ACTIVITY_METRIC_URL)) {
			assertTrue("ActivityMetricURL incorrect", row.get(ActivityColumn.KEY_ACTIVITY_METRIC_URL).toString().startsWith("http://siteUrlBase/activities/"));
		}
	}

}
