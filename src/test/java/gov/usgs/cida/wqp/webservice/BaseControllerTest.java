package gov.usgs.cida.wqp.webservice;

import static gov.usgs.cida.wqp.swagger.model.ActivityCountJson.HEADER_NWIS_ACTIVITY_COUNT;
import static gov.usgs.cida.wqp.swagger.model.ActivityMetricCountJson.HEADER_NWIS_ACTIVITY_METRIC_COUNT;
import static gov.usgs.cida.wqp.swagger.model.OrganizationCountJson.HEADER_NWIS_ORGANIZATION_COUNT;
import static gov.usgs.cida.wqp.swagger.model.ResDetectQntLmtCountJson.HEADER_NWIS_RES_DETECT_QNT_LMT_COUNT;
import static gov.usgs.cida.wqp.swagger.model.ResultCountJson.HEADER_NWIS_RESULT_COUNT;
import static gov.usgs.cida.wqp.swagger.model.StationCountJson.HEADER_NWIS_SITE_COUNT;
import static gov.usgs.cida.wqp.swagger.model.StationCountJson.NWIS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.ibatis.session.ResultHandler;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;

import gov.usgs.cida.wqp.TestConstraintViolation;
import gov.usgs.cida.wqp.dao.NameSpace;
import gov.usgs.cida.wqp.dao.intfc.ICountDao;
import gov.usgs.cida.wqp.dao.intfc.IStreamingDao;
import gov.usgs.cida.wqp.mapping.BaseColumn;
import gov.usgs.cida.wqp.mapping.CountColumn;
import gov.usgs.cida.wqp.mapping.Profile;
import gov.usgs.cida.wqp.parameter.FilterParameters;
import gov.usgs.cida.wqp.service.ConfigurationService;
import gov.usgs.cida.wqp.service.ILogService;
import gov.usgs.cida.wqp.transform.MapToDelimitedTransformer;
import gov.usgs.cida.wqp.transform.MapToJsonTransformer;
import gov.usgs.cida.wqp.transform.MapToKmlTransformer;
import gov.usgs.cida.wqp.transform.MapToXlsxTransformer;
import gov.usgs.cida.wqp.transform.MapToXmlTransformer;
import gov.usgs.cida.wqp.transform.Transformer;
import gov.usgs.cida.wqp.util.HttpConstants;
import gov.usgs.cida.wqp.util.MimeType;

public class BaseControllerTest {

	public static final String TEST_NWIS_STATION_COUNT = "12";
	public static final String TEST_NWIS_ACTIVITY_COUNT = "113";
	public static final String TEST_NWIS_ACTIVITY_METRIC_COUNT = "32";
	public static final String TEST_NWIS_RESULT_COUNT = "359";
	public static final String TEST_NWIS_RES_DETECT_QNT_LMT_COUNT = "432";
	public static final String TEST_NWIS_PROJECT_COUNT = "106";
	public static final String TEST_NWIS_PROJECT_ML_WEIGHTING_COUNT = "1";
	public static final String TEST_TOTAL_STATION_COUNT = "121";
	public static final String TEST_TOTAL_ACTIVITY_COUNT = "1131";
	public static final String TEST_TOTAL_ACTIVITY_METRIC_COUNT = "321";
	public static final String TEST_TOTAL_RESULT_COUNT = "3591";
	public static final String TEST_TOTAL_RES_DETECT_QNT_LMT_COUNT = "4321";
	public static final String TEST_TOTAL_PROJECT_COUNT = "1061";
	public static final String TEST_TOTAL_PROJECT_ML_WEIGHTING_COUNT = "4";
	public static final String TEST_NWIS_ORGANIZATION_COUNT = "1";
	public static final String TEST_TOTAL_ORGANIZATION_COUNT = "1";

	@Mock
	private IStreamingDao streamingDao;
	@Mock
	private ICountDao countDao;
	@Mock
	private ILogService logService;
	@Mock
	private ContentNegotiationStrategy contentStrategy;
	@Mock
	private Validator validator;

	private TestBaseController testController;
	private MockHttpServletRequest request;
	private ConfigurationService configurationService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		configurationService = new ConfigurationService();
		Whitebox.setInternalState(configurationService, "maxResultRows", 100);
		Whitebox.setInternalState(configurationService, "codesUrl", "http://test-url.usgs.gov");
		testController = new TestBaseController(streamingDao, countDao, logService, contentStrategy, validator, configurationService);
		request = new MockHttpServletRequest();
		TestBaseController.remove();
		when(validator.validate(any(FilterParameters.class))).thenReturn(Collections.emptySet());
	}

	@After
	public void teardown() {
		//Need to manually clear out thread locals
		TestBaseController.remove();
	}

	@Test
	public void determineZippedTest() {
		testController.determineZipped(null);
		assertFalse(TestBaseController.getZipped());

		FilterParameters filter = new FilterParameters();
		TestBaseController.setFilter(filter);

		testController.determineZipped(null);
		assertFalse(TestBaseController.getZipped());

		filter.setZip("xxx");
		testController.determineZipped(null);
		assertFalse(TestBaseController.getZipped());
		testController.determineZipped(MimeType.csv.getMediaType());
		assertFalse(TestBaseController.getZipped());

		filter.setZip("no");
		testController.determineZipped(null);
		assertFalse(TestBaseController.getZipped());
		testController.determineZipped(MimeType.kml.getMediaType());
		assertFalse(TestBaseController.getZipped());
		testController.determineZipped(MimeType.kmz.getMediaType());
		assertTrue(TestBaseController.getZipped());

		filter.setZip("yes");
		testController.determineZipped(MimeType.kmz.getMediaType());
		assertTrue(TestBaseController.getZipped());
		testController.determineZipped(MimeType.json.getMediaType());
		assertTrue(TestBaseController.getZipped());
		testController.determineZipped(MimeType.csv.getMediaType());
		assertTrue(TestBaseController.getZipped());
	}

	@Test
	public void getOutputStreamTest() {
		HttpServletResponse response = new MockHttpServletResponse();
		try {
			OutputStream out = testController.getOutputStream(response, false, "abc");
			assertTrue(out instanceof BufferedOutputStream);
		} catch (RuntimeException e) {
			fail("Should not get exception but did:" + e.getLocalizedMessage());
		}

		try {
			OutputStream out = testController.getOutputStream(response, true, "abc");
			assertTrue(out instanceof ZipOutputStream);
			out.write("xyz".getBytes());
			((ZipOutputStream) out).closeEntry();
			((ZipOutputStream) out).finish();
			out.close();
			ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(((MockHttpServletResponse)response).getContentAsByteArray()));
			ZipEntry entry = in.getNextEntry();
			assertTrue(entry.getName().contentEquals("abc"));
		} catch (IOException e) {
			fail("Should not get exception but did:" + e.getLocalizedMessage());
		}
	}

	@Test
	public void determineMimeTypeTest() {
		TestBaseController.setZipped(false);
		assertEquals(MimeType.csv, testController.determineMimeType(MimeType.csv));

		TestBaseController.setZipped(true);
		assertEquals(MimeType.csv, testController.determineMimeType(MimeType.csv));

		TestBaseController.setZipped(false);
		assertEquals(MimeType.kml, testController.determineMimeType(MimeType.kml));

		TestBaseController.setZipped(true);
		assertEquals(MimeType.kmz, testController.determineMimeType(MimeType.kml));
	}

	@Test
	public void closeZipTest() {
		try {
			ByteArrayOutputStream response = new ByteArrayOutputStream();
			ZipOutputStream zipOut = new ZipOutputStream(response);

			zipOut.putNextEntry(new ZipEntry("abc"));
			zipOut.write("xyz".getBytes());
			testController.closeZip(zipOut);

			ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(response.toByteArray()));
			ZipEntry entry = in.getNextEntry();
			assertTrue(entry.getName().contentEquals("abc"));

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			int len;
			byte[] buffer = new byte[1024];
			while ((len = in.read(buffer)) > 0) {
				os.write(buffer, 0, len);
			}
			assertEquals("xyz", os.toString());

		} catch (IOException e) {
			fail("Should not get exception but did:" + e.getLocalizedMessage());
		}
	}

	@Test
	public void getContentHeaderTest() {
		TestBaseController.setMimeType(MimeType.csv);
		TestBaseController.setZipped(false);
		assertEquals(HttpConstants.MIME_TYPE_CSV, testController.getContentHeader());

		TestBaseController.setMimeType(MimeType.csv);
		TestBaseController.setZipped(true);
		assertEquals(HttpConstants.MIME_TYPE_ZIP, testController.getContentHeader());

		TestBaseController.setMimeType(MimeType.kml);
		TestBaseController.setZipped(false);
		assertEquals(HttpConstants.MIME_TYPE_KML, testController.getContentHeader());

		TestBaseController.setMimeType(MimeType.kml);
		TestBaseController.setZipped(true);
		assertEquals(HttpConstants.MIME_TYPE_KMZ, testController.getContentHeader());

		TestBaseController.setMimeType(MimeType.kmz);
		TestBaseController.setZipped(false);
		assertEquals(HttpConstants.MIME_TYPE_KMZ, testController.getContentHeader());

		TestBaseController.setMimeType(MimeType.kmz);
		TestBaseController.setZipped(false);
		assertEquals(HttpConstants.MIME_TYPE_KMZ, testController.getContentHeader());
	}

	@Test
	public void getAttachementFileNameTest() {
		TestBaseController.setMimeType(MimeType.csv);
		TestBaseController.setZipped(false);
		TestBaseController.setProfile(Profile.STATION);
		assertEquals("station.csv", testController.getAttachementFileName());

		TestBaseController.setZipped(true);
		assertEquals("station.zip", testController.getAttachementFileName());

		TestBaseController.setMimeType(MimeType.kml);
		assertEquals("station.kmz", testController.getAttachementFileName());

		TestBaseController.setMimeType(MimeType.kmz);
		assertEquals("station.kmz", testController.getAttachementFileName());

		TestBaseController.setMimeType(MimeType.tsv);
		TestBaseController.setZipped(false);
		TestBaseController.setProfile(Profile.PC_RESULT);
		assertEquals("result.tsv", testController.getAttachementFileName());

		TestBaseController.setMimeType(MimeType.xml);
		TestBaseController.setProfile(Profile.BIOLOGICAL);
		assertEquals("biologicalresult.xml", testController.getAttachementFileName());
	}

	@Test
	public void doHeadRequest2NullTest() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterParameters filter = new FilterParameters();
		testController.doHeadRequest(request, response, filter, null, null);

		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService, never()).logHeadComplete(any(HttpServletResponse.class), any(BigDecimal.class));
		verify(logService).logRequestComplete(isNull(), anyString(), isNull());
	}

	@Test
	public void doHeadRequest2ValuesTest() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterParameters filter = new FilterParameters();
		TestBaseController.setFilter(filter);
		testController.doHeadRequest(request, response, filter, "csv", "yes");

		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		//This is xml because we did not go through the normal Spring path and thus got no media type and defaulted to xml
		assertEquals("xml", filter.getMimeType());
		assertEquals("yes", filter.getZip());
		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService).logHeadComplete(any(HttpServletResponse.class), isNull());
		verify(logService).logRequestComplete(isNull(), anyString(), isNull());
	}

	@Test
	public void doHeadRequestTest() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterParameters filter = new FilterParameters();
		filter.setMinactivities("1");
		testController.doHeadRequest(request, response, filter);

		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService).logHeadComplete(any(HttpServletResponse.class), isNull());
		verify(logService).logRequestComplete(isNull(), anyString(), isNull());
	}

	@Test
	public void doHeadRequestTest_error() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterParameters filter = new FilterParameters();
		testController.doHeadRequest(request, response, filter);

		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService, never()).logHeadComplete(any(HttpServletResponse.class), any(BigDecimal.class));
		verify(logService).logRequestComplete(isNull(), anyString(), isNull());
	}

	@Test
	public void doCommonSetupTest_NoParms() {
		FilterParameters filter = new FilterParameters();
		MockHttpServletResponse response = new MockHttpServletResponse();
		BigDecimal logId = BigDecimal.ONE;
		TestBaseController.setLogId(logId);

		assertFalse(testController.doCommonSetup(request, response, null));
		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), isNull());
		verify(logService, never()).logHeadComplete(any(HttpServletResponse.class), any(BigDecimal.class));
		verify(countDao, never()).getCounts(any(NameSpace.class), any(FilterParameters.class));

		assertFalse(testController.doCommonSetup(request, response, filter));
		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService, never()).logHeadComplete(any(HttpServletResponse.class), any(BigDecimal.class));
		verify(countDao, never()).getCounts(any(NameSpace.class), any(FilterParameters.class));
	}

	@Test
	public void doCommonSetupTest_InvalidParms() {
		FilterParameters filter = new FilterParameters();
		filter.setMinactivities("a");
		MockHttpServletResponse response = new MockHttpServletResponse();
		BigDecimal logId = BigDecimal.ONE;
		TestBaseController.setLogId(logId);
		when(validator.validate(filter)).thenReturn(getConstraintViolations(filter));

		assertFalse(testController.doCommonSetup(request, response, filter));
		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertTrue(response.getHeader(HttpConstants.HEADER_WARNING).startsWith("299 WQP \"Unknown"));
		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService, never()).logHeadComplete(any(HttpServletResponse.class), any(BigDecimal.class));
		verify(countDao, never()).getCounts(any(NameSpace.class), any(FilterParameters.class));
	}

	@Test
	public void doCommonSetupTest() throws HttpMediaTypeNotAcceptableException {
		FilterParameters filter = new FilterParameters();
		filter.setZip("yes");
		filter.setDataProfile(Profile.STATION.toString());
		when(contentStrategy.resolveMediaTypes(any())).thenReturn(Arrays.asList(MimeType.kml.getMediaType()));

		MockHttpServletResponse response = new MockHttpServletResponse();
		BigDecimal logId = BigDecimal.ONE;
		TestBaseController.setLogId(logId);

		assertTrue(testController.doCommonSetup(request, response, filter));
		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		assertEquals("application/vnd.google-earth.kmz;charset=UTF-8", response.getHeader(HttpConstants.HEADER_CONTENT_TYPE));
		assertEquals("attachment; filename=station.kmz", response.getHeader(HttpConstants.HEADER_CONTENT_DISPOSITION));
		assertEquals("12", response.getHeader(TestBaseController.TEST_COUNT));
		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService).logHeadComplete(any(HttpServletResponse.class), isNull());
		verify(countDao).getCounts(any(NameSpace.class), any(FilterParameters.class));
	}

	@Test
	public void doCommonSetupPostCountTest() throws HttpMediaTypeNotAcceptableException {
		FilterParameters filter = new FilterParameters();
		filter.setDataProfile(Profile.STATION.toString());
		when(contentStrategy.resolveMediaTypes(any())).thenReturn(Arrays.asList(MimeType.json.getMediaType()));
		request.setMethod("POST");
		MockHttpServletResponse response = new MockHttpServletResponse();
		BigDecimal logId = BigDecimal.ONE;
		TestBaseController.setLogId(logId);

		assertTrue(testController.doCommonSetup(request, response, filter));
		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		assertEquals("application/json;charset=UTF-8", response.getHeader(HttpConstants.HEADER_CONTENT_TYPE));
		assertEquals("attachment; filename=station.json", response.getHeader(HttpConstants.HEADER_CONTENT_DISPOSITION));
		assertEquals("12", response.getHeader(TestBaseController.TEST_COUNT));
		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService).logHeadComplete(any(HttpServletResponse.class), isNull());
		verify(countDao).getCounts(any(NameSpace.class), any(FilterParameters.class));

		request.setRequestURI("endpoint/count");
		response = new MockHttpServletResponse();
		assertTrue(testController.doCommonSetup(request, response, filter));
		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		assertEquals("application/json;charset=UTF-8", response.getHeader(HttpConstants.HEADER_CONTENT_TYPE));
		assertNull(response.getHeader(HttpConstants.HEADER_CONTENT_DISPOSITION));
		assertEquals("12", response.getHeader(TestBaseController.TEST_COUNT));
		verify(logService, times(2)).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService, times(2)).logHeadComplete(any(HttpServletResponse.class), isNull());
		verify(countDao, times(2)).getCounts(any(NameSpace.class), any(FilterParameters.class));

		request.setMethod("GET");
		response = new MockHttpServletResponse();
		assertTrue(testController.doCommonSetup(request, response, filter));
		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		assertEquals("application/json;charset=UTF-8", response.getHeader(HttpConstants.HEADER_CONTENT_TYPE));
		assertEquals("attachment; filename=station.json", response.getHeader(HttpConstants.HEADER_CONTENT_DISPOSITION));
		assertEquals("12", response.getHeader(TestBaseController.TEST_COUNT));
		verify(logService, times(3)).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService, times(3)).logHeadComplete(any(HttpServletResponse.class), isNull());
		verify(countDao, times(3)).getCounts(any(NameSpace.class), any(FilterParameters.class));	
	}

	@Test
	public void determineContentType_nothingSpecified() {
		testController.determineContentType(request);
		assertFalse(TestBaseController.getZipped());
		assertEquals(MimeType.xml, TestBaseController.getMimeType());
	}

	@Test
	public void determineContentType_single() throws HttpMediaTypeNotAcceptableException {
		when(contentStrategy.resolveMediaTypes(any(NativeWebRequest.class))).thenReturn(Arrays.asList(new MediaType("application","vnd.google-earth.kmz")));
		testController.determineContentType(request);
		assertTrue(TestBaseController.getZipped());
		assertEquals(MimeType.kmz, TestBaseController.getMimeType());
	}

	@Test
	public void determineContentType_multiple() throws HttpMediaTypeNotAcceptableException {
		when(contentStrategy.resolveMediaTypes(any(NativeWebRequest.class)))
			.thenReturn(Arrays.asList(MediaType.APPLICATION_JSON, new MediaType("application","vnd.google-earth.kmz"), MediaType.APPLICATION_XML));
		testController.determineContentType(request);
		assertFalse(TestBaseController.getZipped());
		assertEquals(MimeType.json, TestBaseController.getMimeType());
	}

	@Test
	public void checkMaxRowsTest() {
		FilterParameters filter = new FilterParameters();
		Whitebox.setInternalState(configurationService, "maxResultRows", 10);
		TestBaseController small = new TestBaseController(null, null, null, null, null, configurationService);
		TestBaseController.setFilter(filter);

		//xml formats ok when less than max & always sorted
		TestBaseController.setMimeType(MimeType.xml);
		//No query parm
		doMaxRowTrueAsserts(small, "5", "yes", false);
		//now for a yes
		filter.setSorted("yes");
		doMaxRowTrueAsserts(small, "5", "yes", false);
		//now for a no
		filter.setSorted("no");
		doMaxRowTrueAsserts(small, "5", "yes", false);

		TestBaseController.setMimeType(MimeType.kml);
		//No query parm
		filter.setSorted("");
		doMaxRowTrueAsserts(small, "5", "yes", false);
		//now for a yes
		filter.setSorted("yes");
		doMaxRowTrueAsserts(small, "5", "yes", false);
		//now for a no
		filter.setSorted("no");
		doMaxRowTrueAsserts(small, "5", "yes", false);

		TestBaseController.setMimeType(MimeType.kmz);
		//No query parm
		filter.setSorted("");
		doMaxRowTrueAsserts(small, "5", "yes", false);
		//now for a yes
		filter.setSorted("yes");
		doMaxRowTrueAsserts(small, "5", "yes", false);
		//now for a no
		filter.setSorted("no");
		doMaxRowTrueAsserts(small, "5", "yes", false);

		//xml formats not ok when greater than max
		TestBaseController.setMimeType(MimeType.xml);
		//No query parm
		filter.setSorted("");
		doMaxRowFalseAsserts(small, "15");
		//now for a yes
		filter.setSorted("yes");
		doMaxRowFalseAsserts(small, "15");
		//now for a no
		filter.setSorted("no");
		doMaxRowFalseAsserts(small, "15");

		TestBaseController.setMimeType(MimeType.kml);
		//No query parm
		filter.setSorted("");
		doMaxRowFalseAsserts(small, "15");
		//now for a yes
		filter.setSorted("yes");
		doMaxRowFalseAsserts(small, "15");
		//now for a no
		filter.setSorted("no");
		doMaxRowFalseAsserts(small, "15");

		TestBaseController.setMimeType(MimeType.kmz);
		//No query parm
		filter.setSorted("");
		doMaxRowFalseAsserts(small, "15");
		//now for a yes
		filter.setSorted("yes");
		doMaxRowFalseAsserts(small, "15");
		//now for a no
		filter.setSorted("no");
		doMaxRowFalseAsserts(small, "15");

		//other formats less than max always ok & sorting based on given (or lack of) sorted query parameter
		TestBaseController.setMimeType(MimeType.csv);
		//No query parm
		filter.setSorted("");
		doMaxRowTrueAsserts(small, "5", "yes", false);
		//now for a yes
		filter.setSorted("yes");
		doMaxRowTrueAsserts(small, "5", "yes", false);
		//now for a no
		filter.setSorted("no");
		doMaxRowTrueAsserts(small, "5", "no", false);

		TestBaseController.setMimeType(MimeType.tsv);
		//No query parm
		filter.setSorted("");
		doMaxRowTrueAsserts(small, "5", "yes", false);
		//now for a yes
		filter.setSorted("yes");
		doMaxRowTrueAsserts(small, "5", "yes", false);
		//now for a no
		filter.setSorted("no");
		doMaxRowTrueAsserts(small, "5", "no", false);

		TestBaseController.setMimeType(MimeType.xlsx);
		//No query parm
		filter.setSorted("");
		doMaxRowTrueAsserts(small, "5", "yes", false);
		//now for a yes
		filter.setSorted("yes");
		doMaxRowTrueAsserts(small, "5", "yes", false);
		//now for a no
		filter.setSorted("no");
		doMaxRowTrueAsserts(small, "5", "no", false);

		//other formats are ok, but not sorted when greater than max - and warning header given
		TestBaseController.setMimeType(MimeType.csv);
		//No query parm
		filter.setSorted("");
		doMaxRowTrueAsserts(small, "15", "no", true);
		//now for a yes
		filter.setSorted("yes");
		doMaxRowTrueAsserts(small, "15", "no", true);
		//now for a no
		filter.setSorted("no");
		doMaxRowTrueAsserts(small, "15", "no", true);

		TestBaseController.setMimeType(MimeType.tsv);
		//No query parm
		filter.setSorted("");
		doMaxRowTrueAsserts(small, "15", "no", true);
		//now for a yes
		filter.setSorted("yes");
		doMaxRowTrueAsserts(small, "15", "no", true);
		//now for a no
		filter.setSorted("no");
		doMaxRowTrueAsserts(small, "15", "no", true);

		TestBaseController.setMimeType(MimeType.xlsx);
		//No query parm
		filter.setSorted("");
		doMaxRowTrueAsserts(small, "15", "no", true);
		//now for a yes
		filter.setSorted("yes");
		doMaxRowTrueAsserts(small, "15", "no", true);
		//now for a no
		filter.setSorted("no");
		doMaxRowTrueAsserts(small, "15", "no", true);
	}

	public void doMaxRowTrueAsserts(TestBaseController small, String rows, String expectedSort, boolean isHeaderexpected) {
		HttpServletResponse response = new MockHttpServletResponse();
		response.setHeader("total", rows);
		assertTrue(small.checkMaxRows(response, "total"));
		assertEquals(expectedSort, TestBaseController.getFilter().getSorted());
		if (isHeaderexpected) {
			assertEquals(2, response.getHeaderNames().size());
			assertTrue(response.getHeaderNames().contains(HttpConstants.HEADER_WARNING));
			assertEquals("This query will return in excess of 10 results, the data will not be sorted.",
					response.getHeader(HttpConstants.HEADER_WARNING));
		} else {
			assertEquals(1, response.getHeaderNames().size());
		}
	}

	public void doMaxRowFalseAsserts(TestBaseController small, String rows) {
		HttpServletResponse response = new MockHttpServletResponse();
		response.setHeader("total", rows);
		assertFalse(small.checkMaxRows(response, "total"));
		assertEquals(2, response.getHeaderNames().size());
		assertTrue(response.getHeaderNames().contains(HttpConstants.HEADER_WARNING));
		assertEquals("This query will return in excess of 10 results, please refine your query.",
					response.getHeader(HttpConstants.HEADER_WARNING));
		assertEquals(400, response.getStatus());
	}

	@Test
	public void processParametersTest_empty() {
		assertFalse(testController.processParameters(null));
		assertFalse(testController.processParameters(new FilterParameters()));
	}

	@Test
	public void processParametersTest_invalid() {
		FilterParameters filter = new FilterParameters();
		filter.setMinactivities("a");
		when(validator.validate(any(FilterParameters.class))).thenReturn(getConstraintViolations(filter));
		assertFalse(testController.processParameters(filter));
		assertFalse(TestBaseController.getFilter().isValid());
	}

	@Test
	public void processParametersTest_valid() {
		FilterParameters filter = new FilterParameters();
		filter.setMinactivities("5");
		assertTrue(testController.processParameters(filter));
		assertTrue(TestBaseController.getFilter().isValid());
	}

	@Test
	public void doPostCountRequestTest() throws HttpMediaTypeNotAcceptableException {
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterParameters filter = new FilterParameters();
		filter.setSiteid(Arrays.asList("11NPSWRD-BICA_MFG_B","WIDNR_WQX-10030952"));
		when(countDao.getCounts(any(NameSpace.class), any(FilterParameters.class))).thenReturn(getRawCounts());
		when(contentStrategy.resolveMediaTypes(any())).thenReturn(Arrays.asList(MimeType.kml.getMediaType()));

		Map<String, String> result = testController.doPostCountRequest(request, response, filter, "kmz", "yes");

		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.containsKey(HEADER_NWIS_SITE_COUNT));
		assertEquals(TEST_NWIS_STATION_COUNT, result.get(HEADER_NWIS_SITE_COUNT));
		assertTrue(result.containsKey(HttpConstants.HEADER_TOTAL_SITE_COUNT));
		assertEquals(TEST_TOTAL_STATION_COUNT, result.get(HttpConstants.HEADER_TOTAL_SITE_COUNT));
		assertEquals("kmz", filter.getMimeType());
		assertEquals("yes", filter.getZip());
		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService).logHeadComplete(any(HttpServletResponse.class), isNull());
		verify(logService).logRequestComplete(isNull(), anyString(), isNull());
	}

	@Test
	public void doPostCountRequestNoMimeZip() throws HttpMediaTypeNotAcceptableException {
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterParameters filter = new FilterParameters();
		filter.setSiteid(Arrays.asList("11NPSWRD-BICA_MFG_B","WIDNR_WQX-10030952"));
		when(countDao.getCounts(any(NameSpace.class), any(FilterParameters.class))).thenReturn(getRawCounts());
		when(contentStrategy.resolveMediaTypes(any())).thenReturn(Arrays.asList(MimeType.kml.getMediaType()));

		Map<String, String> result = testController.doPostCountRequest(request, response, filter, null, null);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.containsKey(HEADER_NWIS_SITE_COUNT));
		assertEquals(TEST_NWIS_STATION_COUNT, result.get(HEADER_NWIS_SITE_COUNT));
		assertTrue(result.containsKey(HttpConstants.HEADER_TOTAL_SITE_COUNT));
		assertEquals(TEST_TOTAL_STATION_COUNT, result.get(HttpConstants.HEADER_TOTAL_SITE_COUNT));
		assertEquals("kml", filter.getMimeType());
		assertEquals("no", filter.getZip());
		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService).logHeadComplete(any(HttpServletResponse.class), isNull());
		verify(logService).logRequestComplete(isNull(), anyString(), isNull());
	}

	@Test
	public void doDataRequest2_noMimeZip() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		testController.doDataRequest(request, response, new FilterParameters(), null, null);
		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService, never()).logHeadComplete(any(HttpServletResponse.class), any(BigDecimal.class));
		verify(logService).logRequestComplete(isNull(), anyString(), anyMap());
		verify(streamingDao, never()).stream(any(NameSpace.class), any(FilterParameters.class), any(ResultHandler.class));
	}

	@Test
	public void doDataRequest2_hasMimeZip() throws HttpMediaTypeNotAcceptableException {
		when(contentStrategy.resolveMediaTypes(any())).thenReturn(Arrays.asList(MimeType.kml.getMediaType()));
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterParameters filter = new FilterParameters();
		filter.setMinactivities("1");
		testController.doDataRequest(request, response, filter, "kml", "yes");
		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		assertEquals("application/vnd.google-earth.kmz;charset=UTF-8", response.getHeader(HttpConstants.HEADER_CONTENT_TYPE));
		assertEquals("attachment; filename=station.kmz", response.getHeader(HttpConstants.HEADER_CONTENT_DISPOSITION));
		assertEquals("12", response.getHeader(TestBaseController.TEST_COUNT));

		ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(response.getContentAsByteArray()));
		try {
			ZipEntry entry = in.getNextEntry();
			assertEquals("station.kml", entry.getName());
		} catch (IOException e) {
			fail("Should not get exception but did:" + e.getLocalizedMessage());
		}

		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService).logHeadComplete(any(HttpServletResponse.class), isNull());
		verify(logService).logRequestComplete(isNull(), anyString(), anyMap());
		verify(countDao).getCounts(any(NameSpace.class), any(FilterParameters.class));
		verify(streamingDao).stream(any(NameSpace.class), any(FilterParameters.class), any(ResultHandler.class));
	}

	@Test
	public void doDataRequestTest_NoParms() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		testController.doDataRequest(request, response, new FilterParameters());
		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService, never()).logHeadComplete(any(HttpServletResponse.class), any(BigDecimal.class));
		verify(logService).logRequestComplete(isNull(), anyString(), anyMap());
		verify(streamingDao, never()).stream(any(NameSpace.class), any(FilterParameters.class), any(ResultHandler.class));
	}

	@Test
	public void doDataRequestTest_error() {
		doThrow(new RuntimeException("yuck!")).when(streamingDao).stream(any(NameSpace.class), any(FilterParameters.class), any(ResultHandler.class));
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterParameters filter = new FilterParameters();
		filter.setMinactivities("1");
		testController.doDataRequest(request, response, filter);
		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
		assertNotNull(response.getHeader(HttpConstants.HEADER_FATAL_ERROR));
		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService).logHeadComplete(any(HttpServletResponse.class), isNull());
		verify(logService).logRequestComplete(isNull(), anyString(), anyMap());
		verify(streamingDao).stream(any(NameSpace.class), any(FilterParameters.class), any(ResultHandler.class));
	}

	@Test
	public void doDataRequestTest() throws HttpMediaTypeNotAcceptableException {
		when(contentStrategy.resolveMediaTypes(any())).thenReturn(Arrays.asList(MimeType.kmz.getMediaType()));
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterParameters filter = new FilterParameters();
		filter.setMinactivities("1");
		testController.doDataRequest(request, response, filter);
		assertEquals(HttpConstants.DEFAULT_ENCODING, response.getCharacterEncoding());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertNull(response.getHeader(HttpConstants.HEADER_WARNING));
		assertEquals("application/vnd.google-earth.kmz;charset=UTF-8", response.getHeader(HttpConstants.HEADER_CONTENT_TYPE));
		assertEquals("attachment; filename=station.kmz", response.getHeader(HttpConstants.HEADER_CONTENT_DISPOSITION));
		assertEquals("12", response.getHeader(TestBaseController.TEST_COUNT));

		ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(response.getContentAsByteArray()));
		try {
			ZipEntry entry = in.getNextEntry();
			assertEquals("station.kml", entry.getName());
		} catch (IOException e) {
			fail("Should not get exception but did:" + e.getLocalizedMessage());
		}

		verify(logService).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(FilterParameters.class));
		verify(logService).logHeadComplete(any(HttpServletResponse.class), isNull());
		verify(logService).logRequestComplete(isNull(), anyString(), anyMap());
		verify(countDao).getCounts(any(NameSpace.class), any(FilterParameters.class));
		verify(streamingDao).stream(any(NameSpace.class), any(FilterParameters.class), any(ResultHandler.class));
	}

	@Test
	public void determineZipEntryNameTest() {
		TestBaseController.setProfile(Profile.STATION);
		TestBaseController.setMimeType(MimeType.csv);
		assertEquals("station.csv", testController.determineZipEntryName());
		TestBaseController.setMimeType(MimeType.kml);
		assertEquals("station.kml", testController.determineZipEntryName());
		TestBaseController.setMimeType(MimeType.kmz);
		assertEquals("station.kml", testController.determineZipEntryName());
		TestBaseController.setProfile(Profile.PC_RESULT);
		TestBaseController.setMimeType(MimeType.xml);
		assertEquals("result.xml", testController.determineZipEntryName());
	}

	@Test
	public void determineNamespaceTest() {
		TestBaseController.setMimeType(MimeType.kml);
		assertEquals(NameSpace.STATION_KML, testController.determineNamespace());

		TestBaseController.setMimeType(MimeType.kmz);
		assertEquals(NameSpace.STATION_KML, testController.determineNamespace());
		
		TestBaseController.setMimeType(MimeType.geojson);
		TestBaseController.setProfile(Profile.SIMPLE_STATION);
		assertEquals(NameSpace.SIMPLE_STATION, testController.determineNamespace());
		
		TestBaseController.setProfile(Profile.SUMMARY_STATION);
		assertEquals(NameSpace.SUMMARY_STATION, testController.determineNamespace());

		TestBaseController.setMimeType(MimeType.csv);
		TestBaseController.setProfile(Profile.BIOLOGICAL);
		assertEquals(NameSpace.BIOLOGICAL_RESULT, testController.determineNamespace());

		TestBaseController.setProfile(Profile.PC_RESULT);
		assertEquals(NameSpace.RESULT, testController.determineNamespace());

		TestBaseController.setProfile(Profile.SIMPLE_STATION);
		assertEquals(NameSpace.SIMPLE_STATION, testController.determineNamespace());

		TestBaseController.setProfile(Profile.STATION);
		assertEquals(NameSpace.STATION, testController.determineNamespace());

		TestBaseController.setProfile(Profile.ACTIVITY);
		assertEquals(NameSpace.ACTIVITY, testController.determineNamespace());

		TestBaseController.setProfile(Profile.ACTIVITY_ALL);
		assertEquals(NameSpace.ACTIVITY_ALL, testController.determineNamespace());

		TestBaseController.setProfile(Profile.ACTIVITY_METRIC);
		assertEquals(NameSpace.ACTIVITY_METRIC, testController.determineNamespace());

		TestBaseController.setProfile(Profile.RES_DETECT_QNT_LMT);
		assertEquals(NameSpace.RES_DETECT_QNT_LMT, testController.determineNamespace());

		TestBaseController.setProfile(Profile.PROJECT);
		assertEquals(NameSpace.PROJECT, testController.determineNamespace());

		TestBaseController.setProfile(null);
		assertNull(testController.determineNamespace());
	}
	
	@Test
	public void getGeoJsonNameSpaceTest() {
		assertEquals(NameSpace.SUMMARY_STATION, testController.getGeoJsonNameSpace(Profile.SUMMARY_STATION));
		
		assertEquals(NameSpace.SIMPLE_STATION, testController.getGeoJsonNameSpace(Profile.SIMPLE_STATION));
		
		assertNull(testController.getGeoJsonNameSpace(null));
	}

	@Test
	public void determineNamespaceFromProfileTest() {
		assertEquals(NameSpace.NARROW_RESULT, testController.determineNamespaceFromProfile(Profile.NARROW_RESULT));

		assertEquals(NameSpace.BIOLOGICAL_RESULT, testController.determineNamespaceFromProfile(Profile.BIOLOGICAL));

		assertEquals(NameSpace.RESULT, testController.determineNamespaceFromProfile(Profile.PC_RESULT));

		assertEquals(NameSpace.SIMPLE_STATION, testController.determineNamespaceFromProfile(Profile.SIMPLE_STATION));

		assertEquals(NameSpace.STATION, testController.determineNamespaceFromProfile(Profile.STATION));
		
		assertEquals(NameSpace.SUMMARY_STATION, testController.determineNamespaceFromProfile(Profile.SUMMARY_STATION));

		assertEquals(NameSpace.ACTIVITY, testController.determineNamespaceFromProfile(Profile.ACTIVITY));

		assertEquals(NameSpace.ACTIVITY_ALL, testController.determineNamespaceFromProfile(Profile.ACTIVITY_ALL));

		assertEquals(NameSpace.ACTIVITY_METRIC, testController.determineNamespaceFromProfile(Profile.ACTIVITY_METRIC));

		assertEquals(NameSpace.RES_DETECT_QNT_LMT, testController.determineNamespaceFromProfile(Profile.RES_DETECT_QNT_LMT));

		assertEquals(NameSpace.PROJECT, testController.determineNamespaceFromProfile(Profile.PROJECT));

		assertEquals(NameSpace.PROJECT_MONITORING_LOCATION_WEIGHTING, testController.determineNamespaceFromProfile(Profile.PROJECT_MONITORING_LOCATION_WEIGHTING));

		assertEquals(NameSpace.ORGANIZATION, testController.determineNamespaceFromProfile(Profile.ORGANIZATION));

		assertNull(testController.determineNamespaceFromProfile(null));
	}

	@Test
	public void getTransformerTest() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TestBaseController.setMimeType(MimeType.json);
		assertTrue(testController.getTransformer(baos, null) instanceof MapToJsonTransformer);
		TestBaseController.setMimeType(MimeType.geojson);
		assertTrue(testController.getTransformer(baos, null) instanceof MapToJsonTransformer);
		TestBaseController.setMimeType(MimeType.xlsx);
		assertTrue(testController.getTransformer(baos, null) instanceof MapToXlsxTransformer);
		TestBaseController.setMimeType(MimeType.xml);
		assertTrue(testController.getTransformer(baos, null) instanceof MapToXmlTransformer);
		TestBaseController.setMimeType(MimeType.kml);
		assertTrue(testController.getTransformer(baos, null) instanceof MapToKmlTransformer);
		TestBaseController.setMimeType(MimeType.kmz);
		assertTrue(testController.getTransformer(baos, null) instanceof MapToKmlTransformer);

		TestBaseController.setMimeType(MimeType.tsv);
		Transformer t = testController.getTransformer(baos, null);
		assertTrue(t instanceof MapToDelimitedTransformer);
		assertEquals(MapToDelimitedTransformer.TAB, ((MapToDelimitedTransformer) t).getDelimiter());

		TestBaseController.setMimeType(MimeType.csv);
		t = testController.getTransformer(baos, null);
		assertTrue(t instanceof MapToDelimitedTransformer);
		assertEquals(MapToDelimitedTransformer.COMMA, ((MapToDelimitedTransformer) t).getDelimiter());
	}

	@Test
	public void determineProfileTest() {
		assertEquals(Profile.STATION, testController.determineProfile(Profile.STATION, null));

		FilterParameters filter = new FilterParameters();
		assertEquals(Profile.STATION, testController.determineProfile(Profile.STATION, filter));

		filter.setDataProfile("");
		assertEquals(Profile.STATION, testController.determineProfile(Profile.STATION, filter));

		filter.setDataProfile("abc");
		assertEquals(Profile.STATION, testController.determineProfile(Profile.STATION, filter));

		filter.setDataProfile("biological");
		assertEquals(Profile.BIOLOGICAL, testController.determineProfile(Profile.STATION, filter));
	}

	@Test
	public void writeWarningHeadersTest() throws UnsupportedEncodingException {
		MockHttpServletResponse response = new MockHttpServletResponse();
		testController.writeWarningHeaders(response);
		assertFalse(response.containsHeader("Warning"));

		FilterParameters filter = new FilterParameters();
		Set<ConstraintViolation<FilterParameters>> validationErrors = new HashSet<>();
		validationErrors.add(new TestConstraintViolation("warning1"));
		validationErrors.add(new TestConstraintViolation("warning2"));
		validationErrors.add(new TestConstraintViolation("warning3"));
		validationErrors.add(new TestConstraintViolation("warning4"));
		filter.setValidationErrors(validationErrors);
		TestBaseController.setFilter(filter);
		testController.writeWarningHeaders(response);
		assertTrue(response.containsHeader("Warning"));
		assertEquals("", response.getContentAsString());
		assertEquals(4, response.getHeaderValues("Warning").size());
		String warning = response.getHeaderValues("Warning").toString();
		assertTrue(warning.contains("299 WQP \"warning1\""));
		assertTrue(warning.contains("299 WQP \"warning2\""));
		assertTrue(warning.contains("299 WQP \"warning3\""));
		assertTrue(warning.contains("299 WQP \"warning4\""));
	}

	@Test
	public void warningHeaderTest() {
		assertEquals("299 WQP \"Unknown error\"", testController.warningHeader(null));

		assertEquals("299 WQP \"Unknown error\"", testController.warningHeader(new TestConstraintViolation("")));

		assertEquals("299 WQP \"warning1\"", testController.warningHeader(new TestConstraintViolation("warning1")));
	}

	@Test
	public void determineHeaderNameTest() {
		Map<String, Object> count = new LinkedHashMap<>();
		assertEquals("Total-", testController.determineHeaderName(null, null));
		assertEquals("Total-", testController.determineHeaderName(count, null));
		assertEquals("Total-abc", testController.determineHeaderName(null, "abc"));
		count.put("abc", "123");
		count.put("def", "456");
		count.put(BaseColumn.KEY_DATA_SOURCE, null);
		assertEquals("Total-xyz", testController.determineHeaderName(count, "xyz"));
		count.put(BaseColumn.KEY_DATA_SOURCE, "dude");
		assertEquals("dude-xyz", testController.determineHeaderName(count, "xyz"));
	}

	@Test
	public void determineHeaderValueTest() {
		Map<String, Object> count = new LinkedHashMap<>();
		assertEquals("0", testController.determineHeaderValue(null, null));
		assertEquals("0", testController.determineHeaderValue(count, null));
		assertEquals("0", testController.determineHeaderValue(null, "abc"));
		count.put("abc", "123");
		count.put("def", "456");
		count.put("ghi", null);
		assertEquals("0", testController.determineHeaderValue(count, "xyz"));
		assertEquals("123", testController.determineHeaderValue(count, "abc"));
		assertEquals("456", testController.determineHeaderValue(count, "def"));
		assertEquals("0", testController.determineHeaderValue(count, "ghi"));
	}

	@Test
	public void addCountHeadersTest() {
		MockHttpServletResponse response = new MockHttpServletResponse();

		testController.addCountHeaders(response, new ArrayList<>(), "empty-total-header", "empty-header", "empty_count");

		Object empty = response.getHeaderValue("empty-header");
		assertNull(empty);
		Object totalEmpty = response.getHeaderValue("empty-total-header");
		assertNotNull(totalEmpty);
		assertEquals("0", totalEmpty);

		Map<String, String> counts = TestBaseController.getCounts();
		assertNotNull(counts);
		assertEquals(1, counts.size());
		assertFalse(counts.containsKey("empty-header"));
		assertTrue(counts.containsKey("empty-total-header"));
		assertEquals("0", counts.get("empty-total-header"));

		testController.addCountHeaders(response, getRawCounts(), HttpConstants.HEADER_TOTAL_SITE_COUNT, HttpConstants.HEADER_SITE_COUNT, CountColumn.KEY_STATION_COUNT);

		empty = response.getHeaderValue("empty-header");
		assertNull(empty);
		totalEmpty = response.getHeaderValue("empty-total-header");
		assertNotNull(totalEmpty);
		assertEquals("0", totalEmpty);
		Object nwisSite = response.getHeaderValue(HEADER_NWIS_SITE_COUNT);
		assertNotNull(nwisSite);
		assertEquals(TEST_NWIS_STATION_COUNT, nwisSite);
		Object totalSite = response.getHeaderValue(HttpConstants.HEADER_TOTAL_SITE_COUNT);
		assertNotNull(totalSite);
		assertEquals(TEST_TOTAL_STATION_COUNT, totalSite);

		counts = TestBaseController.getCounts();
		assertNotNull(counts);
		assertEquals(3, counts.size());
		assertFalse(counts.containsKey("empty-header"));
		assertTrue(counts.containsKey("empty-total-header"));
		assertEquals("0", counts.get("empty-total-header"));
		assertTrue(counts.containsKey(HEADER_NWIS_SITE_COUNT));
		assertEquals(TEST_NWIS_STATION_COUNT, counts.get(HEADER_NWIS_SITE_COUNT));
		assertTrue(counts.containsKey(HttpConstants.HEADER_TOTAL_SITE_COUNT));
		assertEquals(TEST_TOTAL_STATION_COUNT, counts.get(HttpConstants.HEADER_TOTAL_SITE_COUNT));

		testController.addCountHeaders(response, getRawCounts(), HttpConstants.HEADER_TOTAL_RESULT_COUNT, HttpConstants.HEADER_RESULT_COUNT, CountColumn.KEY_RESULT_COUNT);

		nwisSite = response.getHeaderValue(HEADER_NWIS_SITE_COUNT);
		assertNotNull(nwisSite);
		assertEquals(TEST_NWIS_STATION_COUNT, nwisSite);
		totalSite = response.getHeaderValue(HttpConstants.HEADER_TOTAL_SITE_COUNT);
		assertNotNull(totalSite);
		assertEquals(TEST_TOTAL_STATION_COUNT, totalSite);
		Object nwisResult = response.getHeaderValue(HEADER_NWIS_RESULT_COUNT);
		assertNotNull(nwisResult);
		assertEquals(TEST_NWIS_RESULT_COUNT, nwisResult);
		Object totalResult = response.getHeaderValue(HttpConstants.HEADER_TOTAL_RESULT_COUNT);
		assertNotNull(totalResult);
		assertEquals(TEST_TOTAL_RESULT_COUNT, totalResult);

		counts = TestBaseController.getCounts();
		assertNotNull(counts);
		assertEquals(5, counts.size());
		assertFalse(counts.containsKey("empty-header"));
		assertTrue(counts.containsKey("empty-total-header"));
		assertEquals("0", counts.get("empty-total-header"));
		assertTrue(counts.containsKey(HEADER_NWIS_SITE_COUNT));
		assertEquals(TEST_NWIS_STATION_COUNT, counts.get(HEADER_NWIS_SITE_COUNT));
		assertTrue(counts.containsKey(HttpConstants.HEADER_TOTAL_SITE_COUNT));
		assertEquals(TEST_TOTAL_STATION_COUNT, counts.get(HttpConstants.HEADER_TOTAL_SITE_COUNT));
		assertTrue(counts.containsKey(HEADER_NWIS_RESULT_COUNT));
		assertEquals(TEST_NWIS_RESULT_COUNT, counts.get(HEADER_NWIS_RESULT_COUNT));
		assertTrue(counts.containsKey(HttpConstants.HEADER_TOTAL_RESULT_COUNT));
		assertEquals(TEST_TOTAL_RESULT_COUNT, counts.get(HttpConstants.HEADER_TOTAL_RESULT_COUNT));
	}

	@Test
	public void addSiteHeadersTest() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		testController.addSiteHeaders(response, getRawCounts());
		assertEquals(TEST_NWIS_STATION_COUNT, response.getHeaderValue(HEADER_NWIS_SITE_COUNT));
		assertEquals(TEST_TOTAL_STATION_COUNT, response.getHeaderValue(HttpConstants.HEADER_TOTAL_SITE_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ACTIVITY_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ACTIVITY_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ACTIVITY_METRIC_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ACTIVITY_METRIC_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_RESULT_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_RESULT_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_RES_DETECT_QNT_LMT_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_RES_DETECT_QNT_LMT_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ORGANIZATION_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ORGANIZATION_COUNT));
	}

	@Test
	public void addActivityHeadersTest() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		testController.addActivityHeaders(response, getRawCounts());
		assertNull(response.getHeaderValue(HEADER_NWIS_SITE_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_SITE_COUNT));
		assertEquals(TEST_NWIS_ACTIVITY_COUNT, response.getHeaderValue(HEADER_NWIS_ACTIVITY_COUNT));
		assertEquals(TEST_TOTAL_ACTIVITY_COUNT, response.getHeaderValue(HttpConstants.HEADER_TOTAL_ACTIVITY_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ACTIVITY_METRIC_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ACTIVITY_METRIC_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_RESULT_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_RESULT_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_RES_DETECT_QNT_LMT_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_RES_DETECT_QNT_LMT_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ORGANIZATION_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ORGANIZATION_COUNT));
	}

	@Test
	public void addActivityMetricHeadersTest() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		testController.addActivityMetricHeaders(response, getRawCounts());
		assertNull(response.getHeaderValue(HEADER_NWIS_SITE_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_SITE_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ACTIVITY_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ACTIVITY_COUNT));
		assertEquals(TEST_NWIS_ACTIVITY_METRIC_COUNT, response.getHeaderValue(HEADER_NWIS_ACTIVITY_METRIC_COUNT));
		assertEquals(TEST_TOTAL_ACTIVITY_METRIC_COUNT, response.getHeaderValue(HttpConstants.HEADER_TOTAL_ACTIVITY_METRIC_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_RESULT_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_RESULT_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_RES_DETECT_QNT_LMT_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_RES_DETECT_QNT_LMT_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ORGANIZATION_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ORGANIZATION_COUNT));
	}

	@Test
	public void addResultHeadersTest() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		testController.addResultHeaders(response, getRawCounts());
		assertNull(response.getHeaderValue(HEADER_NWIS_SITE_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_SITE_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ACTIVITY_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ACTIVITY_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ACTIVITY_METRIC_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ACTIVITY_METRIC_COUNT));
		assertEquals(TEST_NWIS_RESULT_COUNT, response.getHeaderValue(HEADER_NWIS_RESULT_COUNT));
		assertEquals(TEST_TOTAL_RESULT_COUNT, response.getHeaderValue(HttpConstants.HEADER_TOTAL_RESULT_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_RES_DETECT_QNT_LMT_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_RES_DETECT_QNT_LMT_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ORGANIZATION_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ORGANIZATION_COUNT));
	}

	@Test
	public void addResDetectQntLmtHeadersTest() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		testController.addResDetectQntLmtHeaders(response, getRawCounts());
		assertNull(response.getHeaderValue(HEADER_NWIS_SITE_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_SITE_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ACTIVITY_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ACTIVITY_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ACTIVITY_METRIC_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ACTIVITY_METRIC_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_RESULT_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_RESULT_COUNT));
		assertEquals(TEST_NWIS_RES_DETECT_QNT_LMT_COUNT, response.getHeaderValue(HEADER_NWIS_RES_DETECT_QNT_LMT_COUNT));
		assertEquals(TEST_TOTAL_RES_DETECT_QNT_LMT_COUNT, response.getHeaderValue(HttpConstants.HEADER_TOTAL_RES_DETECT_QNT_LMT_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ORGANIZATION_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ORGANIZATION_COUNT));
	}

	@Test
	public void addOrganizationHeadersTest() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		testController.addOrganizationHeaders(response, getRawCounts());
		assertNull(response.getHeaderValue(HEADER_NWIS_SITE_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_SITE_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ACTIVITY_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ACTIVITY_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_ACTIVITY_METRIC_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_ACTIVITY_METRIC_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_RESULT_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_RESULT_COUNT));
		assertNull(response.getHeaderValue(HEADER_NWIS_RES_DETECT_QNT_LMT_COUNT));
		assertNull(response.getHeaderValue(HttpConstants.HEADER_TOTAL_RES_DETECT_QNT_LMT_COUNT));
		assertEquals(TEST_NWIS_ORGANIZATION_COUNT, response.getHeaderValue(HEADER_NWIS_ORGANIZATION_COUNT));
		assertEquals(TEST_TOTAL_ORGANIZATION_COUNT, response.getHeaderValue(HttpConstants.HEADER_TOTAL_ORGANIZATION_COUNT));
	}

	public static List<Map<String, Object>> getRawCounts() {
		List<Map<String, Object>> rawCounts = new ArrayList<>();
		Map<String, Object> nwisCountRow = new HashMap<>();
		nwisCountRow.put(BaseColumn.KEY_DATA_SOURCE, NWIS);
		nwisCountRow.put(CountColumn.KEY_STATION_COUNT, TEST_NWIS_STATION_COUNT);
		nwisCountRow.put(CountColumn.KEY_ACTIVITY_COUNT, TEST_NWIS_ACTIVITY_COUNT);
		nwisCountRow.put(CountColumn.KEY_ACTIVITY_METRIC_COUNT, TEST_NWIS_ACTIVITY_METRIC_COUNT);
		nwisCountRow.put(CountColumn.KEY_RESULT_COUNT, TEST_NWIS_RESULT_COUNT);
		nwisCountRow.put(CountColumn.KEY_RES_DETECT_QNT_LMT_COUNT, TEST_NWIS_RES_DETECT_QNT_LMT_COUNT);
		nwisCountRow.put(CountColumn.KEY_PROJECT_COUNT, TEST_NWIS_PROJECT_COUNT);
		nwisCountRow.put(CountColumn.KEY_PROJECT_MONITORING_LOCATION_WEIGHTING_COUNT, TEST_NWIS_PROJECT_ML_WEIGHTING_COUNT);
		nwisCountRow.put(CountColumn.KEY_ORGANIZATION_COUNT, TEST_NWIS_ORGANIZATION_COUNT);
		rawCounts.add(nwisCountRow);

		Map<String, Object> totalCountRow = new HashMap<>();
		totalCountRow.put(BaseColumn.KEY_DATA_SOURCE, null);
		totalCountRow.put(CountColumn.KEY_STATION_COUNT, TEST_TOTAL_STATION_COUNT);
		totalCountRow.put(CountColumn.KEY_ACTIVITY_COUNT, TEST_TOTAL_ACTIVITY_COUNT);
		totalCountRow.put(CountColumn.KEY_ACTIVITY_METRIC_COUNT, TEST_TOTAL_ACTIVITY_METRIC_COUNT);
		totalCountRow.put(CountColumn.KEY_RESULT_COUNT, TEST_TOTAL_RESULT_COUNT);
		totalCountRow.put(CountColumn.KEY_RES_DETECT_QNT_LMT_COUNT, TEST_TOTAL_RES_DETECT_QNT_LMT_COUNT);
		totalCountRow.put(CountColumn.KEY_PROJECT_COUNT, TEST_TOTAL_PROJECT_COUNT);
		totalCountRow.put(CountColumn.KEY_PROJECT_MONITORING_LOCATION_WEIGHTING_COUNT, TEST_TOTAL_PROJECT_ML_WEIGHTING_COUNT);
		totalCountRow.put(CountColumn.KEY_ORGANIZATION_COUNT, TEST_TOTAL_ORGANIZATION_COUNT);
		rawCounts.add(totalCountRow);

		return rawCounts;
	}

	public <T> Set<ConstraintViolation<T>> getConstraintViolations(T object) {
		ConstraintViolation<T> x = ConstraintViolationImpl.forBeanValidation(null, null, null, null, null, null, object, object, null, null, null, object);
		return Collections.singleton(x);
	}
}
