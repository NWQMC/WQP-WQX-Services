package gov.usgs.cida.wqp.dao.count;

import static gov.usgs.cida.wqp.swagger.model.StationCountJson.BIODATA;
import static gov.usgs.cida.wqp.swagger.model.StationCountJson.NWIS;
import static gov.usgs.cida.wqp.swagger.model.StationCountJson.STEWARDS;
import static gov.usgs.cida.wqp.swagger.model.StationCountJson.STORET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import gov.usgs.cida.wqp.BaseSpringTest;
import gov.usgs.cida.wqp.dao.CountDao;
import gov.usgs.cida.wqp.dao.NameSpace;
import gov.usgs.cida.wqp.mapping.BaseColumn;
import gov.usgs.cida.wqp.mapping.CountColumn;
import gov.usgs.cida.wqp.parameter.FilterParameters;

public abstract class BaseCountDaoTest extends BaseSpringTest {
	private static final Logger LOG = LoggerFactory.getLogger(BaseCountDaoTest.class);

	@Autowired 
	CountDao countDao;

	FilterParameters filter;

	public void init() {
		filter = new FilterParameters();
	}

	public void cleanup() {
		filter = null;
	}

	protected List<Map<String, Object>> nullParameterTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, null);
		if (includeActivity || includeResults) {
			//Adjust site count for sites with no activities
			assertStationResults(counts, 5, TOTAL_SITE_COUNT_MINUS_1, NWIS_SITE_COUNT, STEWARDS_SITE_COUNT, STORET_SITE_COUNT_MINUS_1, BIODATA_SITE_COUNT_MINUS_1);
		} else {
			assertStationResults(counts, 5, TOTAL_SITE_COUNT, NWIS_SITE_COUNT, STEWARDS_SITE_COUNT, STORET_SITE_COUNT, BIODATA_SITE_COUNT);
		}
		if (includeActivity) {
			assertActivityResults(counts, 5, TOTAL_ACTIVITY_COUNT, NWIS_ACTIVITY_COUNT, STEWARDS_ACTIVITY_COUNT, STORET_ACTIVITY_COUNT, BIODATA_ACTIVITY_COUNT);
		}
		if (includeResults) {
			assertResultResults(counts, 5, TOTAL_RESULT_COUNT, NWIS_RESULT_COUNT, STEWARDS_RESULT_COUNT, STORET_RESULT_COUNT, BIODATA_RESULT_COUNT);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> emptyParameterTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for sites with no activities
			assertStationResults(counts, 5, TOTAL_SITE_COUNT_MINUS_1, NWIS_SITE_COUNT, STEWARDS_SITE_COUNT, STORET_SITE_COUNT_MINUS_1, BIODATA_SITE_COUNT_MINUS_1);
		} else {
			assertStationResults(counts, 5, TOTAL_SITE_COUNT, NWIS_SITE_COUNT, STEWARDS_SITE_COUNT, STORET_SITE_COUNT, BIODATA_SITE_COUNT);
		}
		if (includeActivity) {
			assertActivityResults(counts, 5, TOTAL_ACTIVITY_COUNT, NWIS_ACTIVITY_COUNT, STEWARDS_ACTIVITY_COUNT, STORET_ACTIVITY_COUNT, BIODATA_ACTIVITY_COUNT);
		}
		if (includeResults) {
			assertResultResults(counts, 5, TOTAL_RESULT_COUNT, NWIS_RESULT_COUNT, STEWARDS_RESULT_COUNT, STORET_RESULT_COUNT, BIODATA_RESULT_COUNT);
		}
		cleanup();
		return counts;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Single Parameter Counts against station_sum

	protected List<Map<String, Object>> avoidTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setCommand(getCommand());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 2, STORET_SITE_COUNT_MINUS_1, null, null, STORET_SITE_COUNT_MINUS_1, null);
		} else {
			assertStationResults(counts, 2, STORET_SITE_COUNT, null, null, STORET_SITE_COUNT, null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 2, STORET_ACTIVITY_COUNT, null, null, STORET_ACTIVITY_COUNT, null);
		}
		if (includeResults) {
			assertResultResults(counts, 2, STORET_RESULT_COUNT, null, null, STORET_RESULT_COUNT, null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> bboxTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setBBox(getBBox());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 4, "8", "2", "2", "4", null);
		} else {
			assertStationResults(counts, 4, "9", "2", "2", "5", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 4, "17", "3", "3", "11", null);
		}
		if (includeResults) {
			assertResultResults(counts, 4, "31", "5", "3", "23", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> countryTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setCountrycode(getCountry());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 5, "10", "2", "2", "5", "1");
		} else {
			assertStationResults(counts, 5, "11", "2", "2", "6", "1");
		}
		if (includeActivity) {
			assertActivityResults(counts, 5, "19", "3", "3", "12", "1");
		}
		if (includeResults) {
			assertResultResults(counts, 5, "33", "5", "3", "24", "1");
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> countyTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setCountycode(getCounty());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 4, "9", "2", "2", "5", null);
		} else {
			assertStationResults(counts, 4, "10", "2", "2", "6", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 4, "18", "3", "3", "12", null);
		}
		if (includeResults) {
			assertResultResults(counts, 4, "32", "5", "3", "24", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> huc2Test(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setHuc(getHuc2());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 4, "6", "2", "2", "2", null);
		} else {
			assertStationResults(counts, 4, "7", "2", "2", "3", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 4, "11", "3", "3", "5", null);
		}
		if (includeResults) {
			assertResultResults(counts, 4, "23", "5", "3", "15", null);
		}
		cleanup();
		return counts;
	}


	protected List<Map<String, Object>> huc3Test(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setHuc(getHuc3());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 4, "6", "2", "2", "2", null);
		} else {
			assertStationResults(counts, 4, "7", "2", "2", "3", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 4, "11", "3", "3", "5", null);
		}
		if (includeResults) {
			assertResultResults(counts, 4, "23", "5", "3", "15", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> huc4Test(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setHuc(getHuc4());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 3, "3", "2", null, "1", null);
		} else {
			assertStationResults(counts, 3, "4", "2", null, "2", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 3, "5", "3", null, "2", null);
		}
		if (includeResults) {
			assertResultResults(counts, 3, "16", "5", null, "11", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> huc5Test(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setHuc(getHuc5());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 3, "3", "2", null, "1", null);
		} else {
			assertStationResults(counts, 3, "4", "2", null, "2", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 3, "5", "3", null, "2", null);
		}
		if (includeResults) {
			assertResultResults(counts, 3, "16", "5", null, "11", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> huc6Test(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setHuc(getHuc6());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 3, "2", "1", null, "1", null);
		} else {
			assertStationResults(counts, 3, "3", "1", null, "2", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 3, "4", "2", null, "2", null);
		}
		if (includeResults) {
			assertResultResults(counts, 3, "15", "4", null, "11", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> huc7Test(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setHuc(getHuc7());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 3, "2", "1", null, "1", null);
		} else {
			assertStationResults(counts, 3, "3", "1", null, "2", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 3, "4", "2", null, "2", null);
		}
		if (includeResults) {
			assertResultResults(counts, 3, "15", "4", null, "11", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> huc8Test(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setHuc(getHuc8());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 2, "1", null, null, "1", null);
		} else {
			assertStationResults(counts, 2, "2", null, null, "2", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 2, "2", null, null, "2", null);
		}
		if (includeResults) {
			assertResultResults(counts, 2, "11", null, null, "11", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> huc10Test(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setHuc(getHuc10());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 2, "1", null, null, "1", null);
		} else {
			assertStationResults(counts, 2, "2", null, null, "2", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 2, "2", null, null, "2", null);
		}
		if (includeResults) {
			assertResultResults(counts, 2, "11", null, null, "11", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> huc12Test(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setHuc(getHuc12());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 2, "1", null, null, "1", null);
		} else {
			assertStationResults(counts, 2, "2", null, null, "2", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 2, "2", null, null, "2", null);
		}
		if (includeResults) {
			assertResultResults(counts, 2, "11", null, null, "11", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> minActivitiesTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setMinactivities(getMinActivities());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 4, "7", "1", "1", "5", null);
		if (includeActivity) {
			assertActivityResults(counts, 4, "19", "2", "2", "15", null);
		}
		if (includeResults) {
			assertResultResults(counts, 4, "55", "4", "2", "49", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> minResultsTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setMinresults(getMinResults());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 3, "6", "1", null, "5", null);
		if (includeActivity) {
			assertActivityResults(counts, 3, "17", "2", null, "15", null);
		}
		if (includeResults) {
			assertResultResults(counts, 3, "53", "4", null, "49", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> nldiUrlTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		try {
			filter.setNldiSites(getManySiteId());
			filter.setSiteid(getManySiteId());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 2, "3", null, null, "3", null);
		if (includeActivity) {
			assertActivityResults(counts, 2, "8", null, null, "8", null);
		}
		if (includeResults) {
			assertResultResults(counts, 2, "19", null, null, "19", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> organizationTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setOrganization(getOrganization());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 4, "9", "2", "2", "5", null);
		} else {
			assertStationResults(counts, 4, "10", "2", "2", "6", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 4, "18", "3", "3", "12", null);
		}
		if (includeResults) {
			assertResultResults(counts, 4, "32", "5", "3", "24", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> providersTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setProviders(getProviders());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 4, "10", "2", "2", "6", null);
		} else {
			assertStationResults(counts, 4, "11", "2", "2", "7", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 4, "22", "3", "3", "16", null);
		}
		if (includeResults) {
			assertResultResults(counts, 4, "58", "5", "3", "50", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> siteIdTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setSiteid(getSiteid());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 4, "8", "2", "2", "4", null);
		} else {
			assertStationResults(counts, 4, "9", "2", "2", "5", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 4, "16", "3", "3", "10", null);
		}
		if (includeResults) {
			assertResultResults(counts, 4, "21", "5", "3", "13", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> manySitesTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		try {
			filter.setSiteid(getManySiteId());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 2, "3", null, null, "3", null);
		if (includeActivity) {
			assertActivityResults(counts, 2, "8", null, null, "8", null);
		}
		if (includeResults) {
			assertResultResults(counts, 2, "19", null, null, "19", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> siteTypeTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setSiteType(getSiteType());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 5, "10", "1", "2", "6", "1");
		} else {
			assertStationResults(counts, 5, "11", "1", "2", "7", "1");
		}
		if (includeActivity) {
			assertActivityResults(counts, 5, "22", "2", "3", "16", "1");
		}
		if (includeResults) {
			assertResultResults(counts, 5, "58", "4", "3", "50", "1");
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> stateTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setStatecode(getState());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 4, "9", "2", "2", "5", null);
		} else {
			assertStationResults(counts, 4, "10", "2", "2", "6", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 4, "18", "3", "3", "12", null);
		}
		if (includeResults) {
			assertResultResults(counts, 4, "32", "5", "3", "24", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> withinTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setWithin(getWithin());
		filter.setLat(getLatitude());
		filter.setLong(getLongitude());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		if (includeActivity || includeResults) {
			//Adjust site count for STORET site with no activities
			assertStationResults(counts, 4, "9", "2", "2", "5", null);
		} else {
			assertStationResults(counts, 4, "10", "2", "2", "6", null);
		}
		if (includeActivity) {
			assertActivityResults(counts, 4, "19", "3", "3", "13", null);
		}
		if (includeResults) {
			assertResultResults(counts, 4, "54", "5", "3", "46", null);
		}
		cleanup();
		return counts;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Single Parameter Counts against activity_sum

	protected List<Map<String, Object>> projectTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setProject(getProject());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 5, "9", "2", "2", "4", "1");
		if (includeActivity) {
			assertActivityResults(counts, 5, "14", "2", "2", "9", "1");
		}
		if (includeResults) {
			assertResultResults(counts, 5, "19", "4", "2", "12", "1");
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> sampleMediaTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setSampleMedia(getSampleMedia());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 5, "11", "2", "2", "6", "1");
		if (includeActivity) {
			assertActivityResults(counts, 5, "21", "2", "2", "16", "1");
		}
		if (includeResults) {
			assertResultResults(counts, 5, "57", "4", "2", "50", "1");
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> startDateHiTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setStartDateHi(getStartDateHi());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 5, "11", "2", "2", "6", "1");
		if (includeActivity) {
			assertActivityResults(counts, 5, "21", "2", "2", "16", "1");
		}
		if (includeResults) {
			assertResultResults(counts, 5, "57", "4", "2", "50", "1");
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> startDateLoTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setStartDateLo(getStartDateLo());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 5, "9", "2", "2", "4", "1");
		if (includeActivity) {
			assertActivityResults(counts, 5, "17", "3", "3", "10", "1");
		}
		if (includeResults) {
			assertResultResults(counts, 5, "22", "5", "3", "13", "1");
		}
		cleanup();
		return counts;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Single Parameter Counts against result_sum

	protected List<Map<String, Object>> analyticalMethodTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setAnalyticalmethod(getAnalyticalMethod());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 3, "5", "1", null, "4", null);
		if (includeActivity) {
			assertActivityResults(counts, 3, "12", "2", null, "10", null);
		}
		if (includeResults) {
			assertResultResults(counts, 3, "17", "4", null, "13", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> assemblageTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setAssemblage(getAssemblage());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 3, "5", null, null, "4", "1");
		if (includeActivity) {
			assertActivityResults(counts, 3, "11", null, null, "10", "1");
		}
		if (includeResults) {
			assertResultResults(counts, 3, "14", null, null, "13", "1");
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> characteristicNameTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setCharacteristicName(getCharacteristicName());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 2, "4", null, null, "4", null);
		if (includeActivity) {
			assertActivityResults(counts, 2, "10", null, null, "10", null);
		}
		if (includeResults) {
			assertResultResults(counts, 2, "13", null, null, "13", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> characteristicTypeTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setCharacteristicType(getCharacteristicType());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 3, "5", null, "1", "4", null);
		if (includeActivity) {
			assertActivityResults(counts, 3, "12", null, "2", "10", null);
		}
		if (includeResults) {
			assertResultResults(counts, 3, "15", null, "2", "13", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> pcodeTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setPCode(getPcode());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 3, "4", "1", null, "3", null);
		if (includeActivity) {
			assertActivityResults(counts, 3, "10", "1", null, "9", null);
		}
		if (includeResults) {
			assertResultResults(counts, 3, "13", "1", null, "12", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> subjectTaxonomicNameTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setSubjectTaxonomicName(getSubjectTaxonomicName());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 3, "5", null, null, "4", "1");
		if (includeActivity) {
			assertActivityResults(counts, 3, "10", null, null, "9", "1");
		}
		if (includeResults) {
			assertResultResults(counts, 3, "13", null, null, "12", "1");
		}
		cleanup();
		return counts;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected List<Map<String, Object>> multipleParameterStationSumTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setCommand(getCommand());
		filter.setBBox(getBBox());
		filter.setCountrycode(getCountry());
		filter.setCountycode(getCounty());
		filter.setHuc(getHuc());
		filter.setLat(getLatitude());
		filter.setLong(getLongitude());
		filter.setMinactivities(getMinActivities());
		filter.setMinresults(getMinResults());
		filter.setNldiSites(getNldiSites());
		filter.setOrganization(getOrganization());
		filter.setProviders(getProviders());
		filter.setSiteid(getSiteid());
		filter.setSiteType(getSiteType());
		filter.setStatecode(getState());
		filter.setWithin(getWithin());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 2, "1", null, null, "1", null);
		if (includeActivity) {
			assertActivityResults(counts, 2, "2", null, null, "2", null);
		}
		if (includeResults) {
			assertResultResults(counts, 2, "4", null, null, "4", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> multipleParameterActivitySumTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setCommand(getCommand());
		filter.setMinactivities(getMinActivities());
		filter.setMinresults(getMinResults());
		filter.setProject(getProject());
		filter.setSampleMedia(getSampleMedia());
		filter.setStartDateHi(getStartDateHi());
		filter.setStartDateLo(getStartDateLo());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 2, "3", null, null, "3", null);
		if (includeActivity) {
			assertActivityResults(counts, 2, "8", null, null, "8", null);
		}
		if (includeResults) {
			assertResultResults(counts, 2, "11", null, null, "11", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> multipleParameterActivitySumStationSumTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setCommand(getCommand());
		filter.setBBox(getBBox());
		filter.setLat(getLatitude());
		filter.setLong(getLongitude());
		filter.setMinactivities(getMinActivities());
		filter.setMinresults(getMinResults());
		filter.setProject(getProject());
		filter.setSampleMedia(getSampleMedia());
		filter.setStartDateHi(getStartDateHi());
		filter.setStartDateLo(getStartDateLo());
		filter.setWithin(getWithin());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 2, "2", null, null, "2", null);
		if (includeActivity) {
			assertActivityResults(counts, 2, "6", null, null, "6", null);
		}
		if (includeResults) {
			assertResultResults(counts, 2, "8", null, null, "8", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> multipleParameterResultSumTest(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setAnalyticalmethod(getAnalyticalMethod());
		filter.setCommand(getCommand());
		filter.setAssemblage(getAssemblage());
		filter.setCharacteristicName(getCharacteristicName());
		filter.setCharacteristicType(getCharacteristicType());
		filter.setCountrycode(getCountry());
		filter.setCountycode(getCounty());
		filter.setHuc(getHuc());
		filter.setMinactivities(getMinActivities());
		filter.setMinresults(getMinResults());
		filter.setNldiSites(getNldiSites());
		filter.setOrganization(getOrganization());
		filter.setPCode(getPcode());
		filter.setProject(getProject());
		filter.setProviders(getProviders());
		filter.setSampleMedia(getSampleMedia());
		filter.setSiteid(getSiteid());
		filter.setSiteType(getSiteType());
		filter.setStatecode(getState());
		filter.setStartDateHi(getStartDateHi());
		filter.setStartDateLo(getStartDateLo());
		filter.setSubjectTaxonomicName(getSubjectTaxonomicName());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 2, "2", null, null, "2", null);
		if (includeActivity) {
			assertActivityResults(counts, 2, "4", null, null, "4", null);
		}
		if (includeResults) {
			assertResultResults(counts, 2, "7", null, null, "7", null);
		}
		cleanup();
		return counts;
	}

	protected List<Map<String, Object>> multipleParameterResultSumStationSumTests(NameSpace nameSpace, boolean includeActivity, boolean includeResults) {
		init();
		filter.setAnalyticalmethod(getAnalyticalMethod());
		filter.setCommand(getCommand());
		filter.setAssemblage(getAssemblage());
		filter.setBBox(getBBox());
		filter.setCharacteristicName(getCharacteristicName());
		filter.setCharacteristicType(getCharacteristicType());
		filter.setCountrycode(getCountry());
		filter.setCountycode(getCounty());
		filter.setHuc(getHuc());
		filter.setLat(getLatitude());
		filter.setLong(getLongitude());
		filter.setMinactivities(getMinActivities());
		filter.setMinresults(getMinResults());
		filter.setNldiSites(getNldiSites());
		filter.setOrganization(getOrganization());
		filter.setPCode(getPcode());
		filter.setProject(getProject());
		filter.setProviders(getProviders());
		filter.setSampleMedia(getSampleMedia());
		filter.setSiteid(getSiteid());
		filter.setSiteType(getSiteType());
		filter.setStartDateHi(getStartDateHi());
		filter.setStartDateLo(getStartDateLo());
		filter.setStatecode(getState());
		filter.setSubjectTaxonomicName(getSubjectTaxonomicName());
		filter.setWithin(getWithin());
		List<Map<String, Object>> counts = countDao.getCounts(nameSpace, filter);
		assertStationResults(counts, 2, FILTERED_TOTAL_SITE_COUNT, null, null, FILTERED_STORET_SITE_COUNT, null);
		if (includeActivity) {
			assertActivityResults(counts, 2, FILTERED_TOTAL_ACTIVITY_COUNT, null, null, FILTERED_STORET_ACTIVITY_COUNT, null);
		}
		if (includeResults) {
			assertResultResults(counts, 2, FILTERED_TOTAL_RESULT_COUNT, null, null, FILTERED_STORET_RESULT_COUNT, null);
		}
		cleanup();
		return counts;
	}

	protected void assertStationResults(List<Map<String, Object>> counts, int size,
			String total, String nwis, String stewards, String storet, String biodata) {
		LOG.debug(counts.toString());
		assertResults(counts, CountColumn.KEY_STATION_COUNT, size, total, nwis, stewards, storet, biodata);
	}

	protected void assertResultResults(List<Map<String, Object>> counts, int size,
			String total, String nwis, String stewards, String storet, String biodata) {
		assertResults(counts, CountColumn.KEY_RESULT_COUNT, size, total, nwis, stewards, storet, biodata);
	}

	protected void assertActivityResults(List<Map<String, Object>> counts, int size,
			String total, String nwis, String stewards, String storet, String biodata) {
		assertResults(counts, CountColumn.KEY_ACTIVITY_COUNT, size, total, nwis, stewards, storet, biodata);
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
