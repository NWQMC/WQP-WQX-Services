package gov.usgs.cida.wqp;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.FileCopyUtils;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

import gov.usgs.cida.wqp.mapping.Profile;
import gov.usgs.cida.wqp.springinit.TestSpringConfig;
import static gov.usgs.cida.wqp.swagger.model.StationCountJson.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestSpringConfig.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class
	})
@DbUnitConfiguration(dataSetLoader = ColumnSensingFlatXMLDataSetLoader.class)
public abstract class BaseSpringTest {

	@Autowired
	@Qualifier("TEST_NLDI_URL")
	protected URL testNldiUrl;

	public String harmonizeXml(String xmlDoc) {
		return xmlDoc.replace("\r", "").replace("\n", "").replace("\t", "").replaceAll("> *<", "><");
	}

	public String getSourceFile(String file) throws IOException {
		return new String(FileCopyUtils.copyToByteArray(new ClassPathResource("testData/" + file).getInputStream()));
	}

	public String getCompareFile(String file) throws IOException {
		return new String(FileCopyUtils.copyToByteArray(new ClassPathResource("testResult/" + file).getInputStream()));
	}

	public String getCompareFile(Profile profile, String fileType, String suffix) throws IOException {
		String fileName = "testResult/" + profile.toString() + "/" + profile.toString() + (null == suffix ? "" : suffix) + "." + fileType;
		return new String(FileCopyUtils.copyToByteArray(new ClassPathResource(fileName).getInputStream()));
	}

	public String extractZipContent(byte[] content, String expectedEntryName) throws IOException {
		ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(content));
		ZipEntry e = in.getNextEntry();
		assertEquals(expectedEntryName, e.getName());

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int len;
		byte[] buffer = new byte[1024];
		while ((len = in.read(buffer)) > 0) {
			os.write(buffer, 0, len);
		}
		return os.toString();
	}

	public static String TOTAL_SITE_COUNT = "13";
	public static String NWIS_SITE_COUNT = "2";
	public static String STEWARDS_SITE_COUNT = "2";
	public static String STORET_SITE_COUNT = "7";
	public static String BIODATA_SITE_COUNT = "2";

	//The test data has a case where both a biodata and a storet site have no activity/result data associated with them. Thus they should not show up in any queries/counts below the station level.
	public static String TOTAL_SITE_COUNT_MINUS_1 = "11";
	public static String STORET_SITE_COUNT_MINUS_1 = "6";
	public static String BIODATA_SITE_COUNT_MINUS_1 = "1";

	//The test data has a case where a biodata site has no geom associated with it. Thus it should not show up in the json formats.
	public static String TOTAL_SITE_COUNT_GEOM = "12";
	public static String BIODATA_SITE_COUNT_GEOM = "1";

	public static String TOTAL_ACTIVITY_COUNT = "23";
	public static String NWIS_ACTIVITY_COUNT = "3";
	public static String STEWARDS_ACTIVITY_COUNT = "3";
	public static String STORET_ACTIVITY_COUNT = "16";
	public static String BIODATA_ACTIVITY_COUNT = "1";

	public static String TOTAL_ACTIVITY_METRIC_COUNT = "28";
	public static String NWIS_ACTIVITY_METRIC_COUNT = "3";
	public static String STEWARDS_ACTIVITY_METRIC_COUNT = "3";
	public static String STORET_ACTIVITY_METRIC_COUNT = "21";
	public static String BIODATA_ACTIVITY_METRIC_COUNT = "1";

	public static String TOTAL_RESULT_COUNT = "59";
	public static String NWIS_RESULT_COUNT = "5";
	public static String STEWARDS_RESULT_COUNT = "3";
	public static String STORET_RESULT_COUNT = "50";
	public static String BIODATA_RESULT_COUNT = "1";

	public static String TOTAL_RES_DETECT_QNT_LMT_COUNT = "74";
	public static String NWIS_RES_DETECT_QNT_LMT_COUNT = "7";
	public static String STEWARDS_RES_DETECT_QNT_LMT_COUNT = "9";
	public static String STORET_RES_DETECT_QNT_LMT_COUNT = "57";
	public static String BIODATA_RES_DETECT_QNT_LMT_COUNT = "1";
	
	public static String TOTAL_PROJECT_COUNT = "9";
	public static String NWIS_PROJECT_COUNT = "1";
	public static String STEWARDS_PROJECT_COUNT = "1";
	public static String STORET_PROJECT_COUNT = "6";
	public static String BIODATA_PROJECT_COUNT = "1";

	public static final String FILTERED_STORET_SITE_COUNT = "1";
	public static final String FILTERED_TOTAL_SITE_COUNT = "1";
	public static final String FILTERED_STORET_ACTIVITY_COUNT = "2";
	public static final String FILTERED_TOTAL_ACTIVITY_COUNT = "2";
	public static final String FILTERED_STORET_ACTIVITY_METRIC_COUNT = "2";
	public static final String FILTERED_TOTAL_ACTIVITY_METRIC_COUNT = "2";
	public static final String FILTERED_STORET_RESULT_COUNT = "4";
	public static final String FILTERED_TOTAL_RESULT_COUNT = "4";
	public static final String FILTERED_STORET_RES_DETECT_QNT_LMT_COUNT = "7";
	public static final String FILTERED_TOTAL_RES_DETECT_QNT_LMT_COUNT = "7";
	public static final String FILTERED_TOTAL_PROJECT_COUNT = "1";
	public static final String FILTERED_STORET_PROJECT_COUNT = "1";

	public static final BigDecimal STEWARDS_ID = BigDecimal.ONE;
	public static final BigDecimal NWIS_ID = BigDecimal.valueOf(2);
	public static final BigDecimal STORET_ID = BigDecimal.valueOf(3);
	public static final BigDecimal BIODATA_ID = BigDecimal.valueOf(4);

	public static final String AND_ZIP = "&zip=yes";
	public static final String CSV = "csv";
	public static final String CSV_AND_ZIP = CSV + AND_ZIP;
	public static final String TSV = "tsv";
	public static final String TSV_AND_ZIP = TSV + AND_ZIP;
	public static final String XLSX = "xlsx";
	public static final String XLSX_AND_ZIP = XLSX + AND_ZIP;
	public static final String XML = "xml";
	public static final String XML_AND_ZIP = XML + AND_ZIP;
	public static final String KML = "kml";
	public static final String KML_AND_ZIP = KML + AND_ZIP;
	public static final String KMZ = "kmz";
	public static final String KMZ_AND_ZIP = KMZ + AND_ZIP;
	public static final String GEOJSON = "geojson";
	public static final String GEOJSON_AND_ZIP = GEOJSON + AND_ZIP;
	public static final String JSON = "json";
	public static final String JSON_AND_ZIP = JSON + AND_ZIP;


	public String[] getAnalyticalMethod() {
		return new String[]{"https://www.nemi.gov/methods/method_summary/4665/", "https://www.nemi.gov/methods/method_summary/8896/", "analyticalMethod"};
	}

	public static String[] getActivity() {
		return new String[]{"WIDNR_WQX-7788475"};
	}

	public String[] getAvoid() {
		return new String[]{NWIS};
	}

	public String[] getBBox() {
		return new String[]{"-111", "39", "-77", "47"};
	}

	public String[] getAssemblage() {
		return new String[]{"Fish/Nekton", "Benthic Macroinvertebrates", "assemblageSampledName"};
	}

	public String[] getCharacteristicName() {
		return new String[]{"Beryllium", "Nitrate", "characteristicName"};
	}

	public String[] getCharacteristicType() {
		return new String[]{"Inorganics, Minor, Metals", "Nutrient", "Population/Community", "characteristicType"};
	}

	public String[] getCountry() {
		return new String[]{"MX", "US", "XX"};
	}

	public String[] getCounty() {
		return new String[]{"US:19:015", "US:30:003", "US:55:017", "US:55:021", "US:55:027", "XX:44:555"};
	}

	public String[] getHuc() {
		return new String[]{"07*", "0708*", "070801*", "07090002", "07080105", "0000"};
	}

	public String[] getHuc2() {
		return new String[]{"07"};
	}

	public String[] getHuc4() {
		return new String[]{"0709"};
	}

	public String[] getHuc6() {
		return new String[]{"070900"};
	}

	public String[] getHuc8() {
		return new String[]{"07090001"};
	}

	public String[] getLatitude() {
		return new String[]{"43.3836014"};
	}

	public String[] getLongitude() {
		return new String[]{"-88.9773314"};
	}

	public String[] getMinActivities() {
		return new String[]{"2"};
	}

	public String[] getMinResults() {
		return new String[]{"3"};
	}

	public String[] getNldiSites() {
		return new String[]{"11NPSWRD-BICA_MFG_B", "WIDNR_WQX-10030952", "USGS-05425700",
				"USGS-431925089002701", "ARS-IAWC-IAWC225", "ARS-IAWC-IAWC410", "USGS-11421000", "organization-siteId2", "organization-siteId3"};
	}

	public Set<String> getNldiSitesAsSet() {
		return new HashSet<String> (Arrays.asList(getNldiSites()));
	}

	public String[] getNldiurl() {
		return new String[]{testNldiUrl.toString()};
	}

	public String[] getOrganization() {
		return new String[]{"ARS", "11NPSWRD", "USGS-WI", "WIDNR_WQX", "organization"};
	}

	public String[] getPcode() {
		return new String[]{"00032", "00004", "99999"};
	}

	public String[] getProject() {
		return new String[]{"projectId", "CEAP", "NAWQA", "SACR BioTDB"};
	}

	public String[] getProviders() {
		return new String[]{NWIS, STEWARDS, STORET};
	}

	public static String[] getResult() {
		return new String[]{"STORET-5"};
	}

	public String[] getSampleMedia() {
		return new String[]{"sampleMedia", "Other", "Sediment", "Water"};
	}

	public String[] getSiteid() {
		return new String[]{"organization-siteId", "organization-siteId2", "organization-siteId3", "11NPSWRD-BICA_MFG_B", "WIDNR_WQX-10030952", "USGS-05425700", "USGS-431925089002701", "ARS-IAWC-IAWC225", "ARS-IAWC-IAWC410"};
	}

	public String[] getManySiteId() throws IOException {
		return getSourceFile("manySites.txt").split(",");
	}

	public String[] getSiteType() {
		return new String[]{"siteType", "Lake, Reservoir, Impoundment", "Land", "Stream"};
	}

	public String[] getStartDateHi() {
		return new String[]{"10-11-2012"};
	}

	public String[] getStartDateLo() {
		return new String[]{"10-11-1995"};
	}

	public String[] getState() {
		return new String[]{"XX:44", "US:19", "US:30", "US:55"};
	}

	public String[] getSubjectTaxonomicName() {
		return new String[]{"Acipenser", "Lota lota", "sampleTissueTaxonomicName"};
	}

	public String[] getWithin() {
		return new String[]{"650"};
	}

	public void assertMapIsAsExpected(Map<String, Object> expectedRow, Map<String, Object> actualRow) {
		//Doing both left to right and right to left to catch missing/extra on either side.
		for (String i : expectedRow.keySet()) {
			assertEquals(i, expectedRow.get(i), actualRow.get(i));
		}
		for (String i : actualRow.keySet()) {
			assertEquals(i, actualRow.get(i), expectedRow.get(i));
		}
	}

}
