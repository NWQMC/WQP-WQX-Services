package gov.usgs.cida.wqp.webservice;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;

import gov.usgs.cida.wqp.dao.NameSpace;
import gov.usgs.cida.wqp.dao.StreamingResultHandler;
import gov.usgs.cida.wqp.dao.intfc.ICountDao;
import gov.usgs.cida.wqp.dao.intfc.IStreamingDao;
import gov.usgs.cida.wqp.mapping.BaseColumn;
import gov.usgs.cida.wqp.mapping.CountColumn;
import gov.usgs.cida.wqp.mapping.Profile;
import gov.usgs.cida.wqp.mapping.xml.IXmlMapping;
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

public abstract class BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

	protected final IStreamingDao streamingDao;
	protected final ICountDao countDao;
	protected final ILogService logService;
	protected final ConfigurationService configurationService;
	protected final ContentNegotiationStrategy contentStrategy;
	protected final Validator validator;

	private static ThreadLocal<FilterParameters> filter = new ThreadLocal<>();
	private static ThreadLocal<MimeType> mimeType = new ThreadLocal<>();
	private static ThreadLocal<Boolean> zipped = new ThreadLocal<>();
	private static ThreadLocal<BigDecimal> logId = new ThreadLocal<>();
	private static ThreadLocal<Map<String, String>> counts = new ThreadLocal<>();
	private static ThreadLocal<NameSpace> mybatisNamespace = new ThreadLocal<>();
	private static ThreadLocal<Profile> profile = new ThreadLocal<>();
	private static ThreadLocal<Map<String, Integer>> downloadDetails = new ThreadLocal<>();

	public BaseController(IStreamingDao inStreamingDao, ICountDao inCountDao, ILogService inLogService,
			ContentNegotiationStrategy inContentStrategy,
			Validator inValidator, ConfigurationService configurationService) {
		LOG.trace(getClass().getName());

		streamingDao = inStreamingDao;
		countDao = inCountDao;
		logService = inLogService;
		contentStrategy = inContentStrategy;
		validator = inValidator;
		this.configurationService = configurationService;
	}

	public static FilterParameters getFilter() {
		if (null == filter.get()) {
			filter.set(new FilterParameters());
		}
		return filter.get();
	}
	public static void setFilter(FilterParameters inFilter) {
		filter.set(inFilter);
	}
	public static MimeType getMimeType() {
		return mimeType.get();
	}
	public static void setMimeType(MimeType inMimeType) {
		getFilter().setMimeType(null == inMimeType ? null : inMimeType.getExtension());
		mimeType.set(inMimeType);
	}
	public static boolean getZipped() {
		return zipped.get();
	}
	public static void setZipped(Boolean inZipped) {
		getFilter().setZip(inZipped ? "yes" : "no");
		zipped.set(inZipped);
	}
	public static BigDecimal getLogId() {
		return logId.get();
	}
	public static void setLogId(BigDecimal inLogId) {
		logId.set(inLogId);
	}
	public static Map<String, String> getCounts() {
		return counts.get();
	}
	public static void setCounts(Map<String, String> inCounts) {
		counts.set(inCounts);
	}
	public static NameSpace getMybatisNamespace() {
		return mybatisNamespace.get();
	}
	public static void setMybatisNamespace(NameSpace inMybatisNamespace) {
		mybatisNamespace.set(inMybatisNamespace);
	}
	public static Profile getProfile() {
		return profile.get();
	}
	public static void setProfile(Profile inProfile) {
		getFilter().setDataProfile(null == inProfile ? null : inProfile.toString());
		profile.set(inProfile);
	}
	public static Map<String, Integer> getDownloadDetails() {
		if (null == downloadDetails.get()) {
			downloadDetails.set(new HashMap<String, Integer>());
		}
		return downloadDetails.get();
	}
	public static void setDownloadDetails(Map<String, Integer> inDownloadDetails) {
		downloadDetails.set(inDownloadDetails);
	}

	public static void remove() {
		filter.remove();
		mimeType.remove();
		zipped.remove();
		logId.remove();
		counts.remove();
		mybatisNamespace.remove();
		profile.remove();
		downloadDetails.remove();
	}

	protected void determineZipped(MediaType mediaType) {
		Boolean isZipFilter = false;
		FilterParameters filter = getFilter();
		if (null != filter && StringUtils.isNotBlank(filter.getZip())) {
			isZipFilter = StringUtils.containsAny("yes", filter.getZip().trim());
		}
		setZipped(MimeType.kmz.getMediaType().equals(mediaType) || isZipFilter);
	}

	protected OutputStream getOutputStream(HttpServletResponse response, boolean zipped, String fileName) {
		try {
			OutputStream buffered = new BufferedOutputStream(response.getOutputStream());
			if (zipped) {
				ZipOutputStream zipOut = new ZipOutputStream(buffered);
				zipOut.putNextEntry(new ZipEntry(fileName));
				return (OutputStream) zipOut;
			} else {
				return buffered;
			}
		} catch (IOException e) {
			throw new RuntimeException("Error getting OutputStream", e);
		}
	}

	protected MimeType determineMimeType(MimeType mimeType) {
		if (getZipped() && MimeType.kml.equals(mimeType)) {
			return MimeType.kmz;
		} else {
			return mimeType;
		}
	}

	protected void closeZip(OutputStream out) {
		try {
			((ZipOutputStream) out).closeEntry();
			((ZipOutputStream) out).finish();
		} catch (IOException e) {
			throw new RuntimeException("Error closing ZipOutputStream", e);
		}
	}

	protected String getContentHeader() {
		if (getZipped()) {
			switch (getMimeType()) {
			case kml:
			case kmz:
				return MimeType.kmz.getMimeType();
			default:
				return HttpConstants.MIME_TYPE_ZIP;
			}
		} else {
			return getMimeType().getMimeType();
		}
	}

	protected String getAttachementFileName() {
		StringBuilder ret = new StringBuilder(getProfile().getBaseFileName());
		ret.append(".");
		if (getZipped()) {
			switch (getMimeType()) {
			case kml:
			case kmz:
				ret.append(MimeType.kmz);
				break;
			default:
				ret.append("zip");
				break;
			}
		} else {
			ret.append(getMimeType());
		}
		return ret.toString();
	}

	protected void doHeadRequest(HttpServletRequest request, HttpServletResponse response, FilterParameters filter, String mimeType, String zip) {
		if (StringUtils.isNotBlank(mimeType)) {
			filter.setMimeType(mimeType);
		}
		if (StringUtils.isNotBlank(zip)) {
			filter.setZip(zip);
		}
		doHeadRequest(request, response, filter);
	}

	protected void doHeadRequest(HttpServletRequest request, HttpServletResponse response, FilterParameters filter) {
		LOG.info("Processing Head: {}", filter.toJson());

		try {
			doCommonSetup(request, response, filter);
		} finally {
			logService.logRequestComplete(getLogId(), String.valueOf(response.getStatus()), null);
			LOG.info("Processing Head complete: {}", filter.toJson());
			remove();
		}
	}

	protected boolean doCommonSetup(HttpServletRequest request, HttpServletResponse response, FilterParameters filter) {
		setLogId(logService.logRequest(request, response, filter));

		response.setCharacterEncoding(HttpConstants.DEFAULT_ENCODING);
		if (!processParameters(filter)) {
			writeWarningHeaders(response);
			LOG.info("Processing Head invalid params end:{}", null != getFilter() ? getFilter().getValidationErrors() : "No Filter");
			return false;
		}

		determineContentType(request);
		setProfile(determineProfile(getFilter()));
		setMybatisNamespace(determineNamespace());

		addCustomRequestParams();

		List<Map<String, Object>> counts = countDao.getCounts(getMybatisNamespace(), getFilter());

		response.setCharacterEncoding(HttpConstants.DEFAULT_ENCODING);
		response.addHeader(HttpConstants.HEADER_CONTENT_TYPE, getContentHeader());
		if (RequestMethod.POST.toString().equalsIgnoreCase(request.getMethod())
				&& request.getRequestURI().endsWith("count")) {
			//skip the content disposition header on POST counts
		} else {
			response.setHeader(HttpConstants.HEADER_CONTENT_DISPOSITION,"attachment; filename=" + getAttachementFileName());
		}
		String totalHeader = addCountHeaders(response, counts);

		logService.logHeadComplete(response, getLogId());

		return checkMaxRows(response, totalHeader);
	}

	protected void determineContentType(HttpServletRequest request) {
		try {
			MediaType mediaType = MediaType.APPLICATION_XML;
			List<MediaType> mediaTypes = contentStrategy.resolveMediaTypes(new ServletWebRequest(request));
			switch (mediaTypes.size()) {
			case 0:
				LOG.info("Didn't get any content type choice.");
				break;
			case 1:
				mediaType = mediaTypes.get(0);
				break;
			default:
				mediaType = mediaTypes.get(0);
				LOG.info("Got multiple content type choices: " + mediaTypes.size());
				break;
			}
			determineZipped(mediaType);
			setMimeType(determineMimeType(MimeType.csv.fromMediaType(mediaType)));
		} catch (HttpMediaTypeNotAcceptableException e) {
			LOG.info(e.getLocalizedMessage());
			throw new RuntimeException(e);
		}
	}

	protected boolean checkMaxRows(HttpServletResponse response, String totalHeader) {
		Integer totalRows = Integer.valueOf(response.getHeader(totalHeader));
		FilterParameters filter = getFilter();
		switch (getMimeType()) {
		case kml:
		case kmz:
		case xml:
			if (totalRows <= configurationService.getMaxResultRows()) {
				filter.setSorted("yes");
				return true;
			} else {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				response.addHeader(HttpConstants.HEADER_WARNING, "This query will return in excess of " + configurationService.getMaxResultRows() + " results, please refine your query.");
				return false;
			}
		default:
			if (totalRows > configurationService.getMaxResultRows()) {
				filter.setSorted("no");
				response.addHeader(HttpConstants.HEADER_WARNING, "This query will return in excess of " + configurationService.getMaxResultRows() + " results, the data will not be sorted.");
			} else {
				if (null != filter && StringUtils.isBlank(filter.getSorted())) {
					filter.setSorted("yes");
				}
			}
			return true;
		}
	}

	protected boolean processParameters(FilterParameters filter) {
		boolean rtn = false;
		if (null == filter || filter.isEmpty()) {
			LOG.debug("No parameters");
		} else {
			LOG.trace("got parameters");
			LOG.debug("requestParams: {}", filter.toJson());
			filter.setValidationErrors(validator.validate(filter));
			LOG.debug("errors: " + filter.getValidationErrors().toString());
			setFilter(filter);
			rtn = filter.isValid();
		}
		return rtn;
	}

	protected abstract String addCountHeaders(HttpServletResponse response, List<Map<String, Object>> counts);

	protected void addCustomRequestParams() {
		//default is to add nothing
	};

	protected Map<String, String> doPostCountRequest(HttpServletRequest request, HttpServletResponse response, FilterParameters filter, String mimeType, String zip) {
		LOG.info("Processing Post Count");
		if (StringUtils.isNotBlank(mimeType)) {
			filter.setMimeType(mimeType);
		}
		if (StringUtils.isNotBlank(zip)) {
			filter.setZip(zip);
		}
		Map<String, String> counts = null;

		try {
			doCommonSetup(request, response, filter);
			counts = getCounts();
		} finally {
			logService.logRequestComplete(getLogId(), String.valueOf(response.getStatus()), null);
			LOG.info("Processing Post Count complete");
			remove();
		}

		return counts;
	}

	protected void doDataRequest(HttpServletRequest request, HttpServletResponse response, FilterParameters filter, String mimeType, String zip) {
		if (StringUtils.isNotBlank(mimeType)) {
			filter.setMimeType(mimeType);
		}
		if (StringUtils.isNotBlank(zip)) {
			filter.setZip(zip);
		}
		doDataRequest(request, response, filter);
	}

	protected void doDataRequest(HttpServletRequest request, HttpServletResponse response, FilterParameters filter) {
		LOG.info("Processing Data: {}", filter.toJson());
		OutputStream responseStream = null;
		String realHttpStatus = String.valueOf(response.getStatus());

		try {
			if (doCommonSetup(request, response, filter)) {
				responseStream = getOutputStream(response, getZipped(), determineZipEntryName());
				Transformer transformer = getTransformer(responseStream, getLogId());

				ResultHandler<?> handler = new StreamingResultHandler(transformer);
				streamingDao.stream(getMybatisNamespace(), getFilter(), handler);

				transformer.end();
			}
		} catch (Throwable e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			int hashValue = response.hashCode();
			realHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR.toString() + "-" + hashValue;
			//Note: we are giving the user a generic message.  
			//Server logs can be used to troubleshoot problems.
			String msgText = "Something bad happened. Contact us with Reference Number: " + hashValue;
			LOG.error("logId: {}", BaseController.getLogId());
			LOG.error("status: {}", HttpStatus.INTERNAL_SERVER_ERROR.value());
			LOG.error(msgText, e);
			response.addHeader(HttpConstants.HEADER_FATAL_ERROR, msgText);
			if (null != responseStream) {
				try {
					responseStream.write(msgText.getBytes(HttpConstants.DEFAULT_ENCODING));
				} catch (IOException e2) {
					//Just log, cause we obviously can't tell the client
					LOG.error("Error telling client about exception", e2);
				}
			}
		} finally {
			if (null != responseStream) {
				try {
					if (getZipped()) {
						closeZip(responseStream);
					}
				} catch (Throwable e) {
					//Just log, cause we obviously can't tell the client
					LOG.error("Error closing zip", e);
				}
				try {
					responseStream.flush();
				} catch (IOException e) {
					//Just log, cause we obviously can't tell the client
					LOG.error("Error flushing response stream", e);
				}
			}
			LOG.info("Processing Data complete: {}", filter.toJson());
			logService.logRequestComplete(getLogId(), realHttpStatus, getDownloadDetails());
			remove();
		}
	}

	protected String determineZipEntryName() {
		if (MimeType.kml.equals(getMimeType()) || MimeType.kmz.equals(getMimeType())) {
			return getProfile().getBaseFileName() + "." + MimeType.kml;
		} else {
			return getProfile().getBaseFileName() + "." + getMimeType();
		}
	}

	protected NameSpace determineNamespace() {
		switch (getMimeType()) {
		case kml:
		case kmz:
			return NameSpace.STATION_KML;
		case geojson:
			return getGeoJsonNameSpace(getProfile());	
		default:
			return determineNamespaceFromProfile(getProfile());
		}
	}
	
	protected NameSpace getGeoJsonNameSpace(Profile theProfile) {
		NameSpace theNameSpace = null;
		if (theProfile == Profile.SIMPLE_STATION || theProfile == Profile.STATION) {
			theNameSpace = NameSpace.SIMPLE_STATION;
		} else if (theProfile == Profile.SUMMARY_STATION) {
			theNameSpace = NameSpace.SUMMARY_STATION;
		}
		return theNameSpace;
	}

	protected NameSpace determineNamespaceFromProfile(Profile profile) {
		if (null == profile) {
			return null;
		} else {
			switch (profile) {
				case BIOLOGICAL:
					return NameSpace.BIOLOGICAL_RESULT;
				case PC_RESULT:
					return NameSpace.RESULT;
				case NARROW_RESULT:
					return NameSpace.NARROW_RESULT;
				case STATION:
					return NameSpace.STATION;
				case SIMPLE_STATION:
					return NameSpace.SIMPLE_STATION;
				case SUMMARY_STATION:
					return NameSpace.SUMMARY_STATION;
				case ACTIVITY:
					return NameSpace.ACTIVITY;
				case ACTIVITY_ALL:
					return NameSpace.ACTIVITY_ALL;
				case ACTIVITY_METRIC:
					return NameSpace.ACTIVITY_METRIC;
				case RES_DETECT_QNT_LMT:
					return NameSpace.RES_DETECT_QNT_LMT;
				case PROJECT:
					return NameSpace.PROJECT;
				case PROJECT_MONITORING_LOCATION_WEIGHTING:
					return NameSpace.PROJECT_MONITORING_LOCATION_WEIGHTING;
				case ORGANIZATION:
					return NameSpace.ORGANIZATION;
				default:
					//Should never get here...
					return null;
			}
		}
	}

	protected Transformer getTransformer(OutputStream responseStream, BigDecimal logId) {
		Transformer transformer;
		switch (getMimeType()) {
		case json:
		case geojson:
			transformer = new MapToJsonTransformer(responseStream, null, logService, logId, configurationService.getSiteUrlBase());
			break;
		case xlsx:
			transformer = new MapToXlsxTransformer(responseStream, getMapping(getProfile()), logService, logId);
			break;
		case xml:
			transformer = new MapToXmlTransformer(responseStream, getXmlMapping(), logService, logId, getProfile());
			break;
		case kml:
		case kmz:
			transformer = new MapToKmlTransformer(responseStream, getKmlMapping(), logService, logId, getProfile());
			break;
		case tsv:
			transformer = new MapToDelimitedTransformer(responseStream, getMapping(getProfile()), logService, logId, MapToDelimitedTransformer.TAB);
			break;
		case csv:
		default:
			transformer = new MapToDelimitedTransformer(responseStream, getMapping(getProfile()), logService, logId, MapToDelimitedTransformer.COMMA);
			break;
		}
		return transformer;
	}

	protected abstract Profile determineProfile(FilterParameters filter);

	protected Profile determineProfile(Profile defaultProfile, FilterParameters filter) {
		Profile profile = defaultProfile;
		if (null != filter && !StringUtils.isAllBlank(filter.getDataProfile())) {
			profile = Profile.fromString(filter.getDataProfile());
		}
		return profile == null ? defaultProfile : profile;
	}

	protected abstract Map<String, String> getMapping(Profile profile);

	protected abstract IXmlMapping getXmlMapping();

	protected abstract IXmlMapping getKmlMapping();

	public void writeWarningHeaders(HttpServletResponse response) {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		if (null != getFilter() && null != getFilter().getValidationErrors()) {
			getFilter().getValidationErrors().forEach(msg -> response.addHeader(HttpConstants.HEADER_WARNING, warningHeader(msg)));
		}
	}

	protected String warningHeader(ConstraintViolation<FilterParameters> text) {
		StringBuilder rtn = new StringBuilder();
		rtn.append(HttpConstants.HEADER_WARNING_DEFAULT_CODE);
		rtn.append(" WQP \"");
		rtn.append(null == text || null == text.getMessage() || 0 == text.getMessage().length() ? "Unknown error" : text.getMessage());
		rtn.append("\"");
		return rtn.toString();
	}

	protected String determineHeaderName(Map<String, Object> count, String suffix) {
		String mySuffix = suffix==null ? "" : suffix;
		if (null == count) {
			return HttpConstants.HEADER_TOTAL + HttpConstants.HEADER_DELIMITER + mySuffix;
		} else {
			Object provider = count.get(BaseColumn.KEY_DATA_SOURCE);

			if (provider==null) {
				provider = HttpConstants.HEADER_TOTAL + HttpConstants.HEADER_DELIMITER + mySuffix;
			} else {
				provider = provider + HttpConstants.HEADER_DELIMITER + mySuffix;
			}
			LOG.trace("determine count header : {}", provider.toString());

			return provider.toString();
		}
	}

	protected String determineHeaderValue(Map<String, Object> count, String key) {
		if (null == count || count.isEmpty() || null == key || !count.containsKey(key)) {
			return "0";
		} else {
			Object results = count.get(key);
			results = results==null ?"0" :results;
			LOG.trace("determine count value : {}", results.toString());
			return results.toString();
		}
	}

	public void addCountHeaders(HttpServletResponse response, List<Map<String, Object>> rawCounts,
			String totalHeader, String valueHeader, String valueField) {
		LOG.trace("{} counts : {}", valueHeader, rawCounts);
		Map<String, String> counts = null==getCounts() ? new HashMap<>() : getCounts();

		response.addHeader(totalHeader, "0");
		counts.put(totalHeader, "0");

		for (Map<String, Object> count : rawCounts) {
			LOG.trace("result count : {}", count);
			String name = determineHeaderName(count, valueHeader);
			String value = determineHeaderValue(count, valueField);
			response.setHeader(name, value);
			counts.put(name, value);
		}

		setCounts(counts);
	}

	public void addSiteHeaders(HttpServletResponse response, List<Map<String, Object>> counts) {
		addCountHeaders(response, counts, HttpConstants.HEADER_TOTAL_SITE_COUNT, HttpConstants.HEADER_SITE_COUNT, CountColumn.KEY_STATION_COUNT);
	}

	public void addActivityHeaders(HttpServletResponse response, List<Map<String, Object>> counts) {
		addCountHeaders(response, counts, HttpConstants.HEADER_TOTAL_ACTIVITY_COUNT, HttpConstants.HEADER_ACTIVITY_COUNT, CountColumn.KEY_ACTIVITY_COUNT);
	}

	public void addActivityMetricHeaders(HttpServletResponse response, List<Map<String, Object>> counts) {
		addCountHeaders(response, counts, HttpConstants.HEADER_TOTAL_ACTIVITY_METRIC_COUNT, HttpConstants.HEADER_ACTIVITY_METRIC_COUNT, CountColumn.KEY_ACTIVITY_METRIC_COUNT);
	}

	public void addResultHeaders(HttpServletResponse response, List<Map<String, Object>> counts) {
		addCountHeaders(response, counts, HttpConstants.HEADER_TOTAL_RESULT_COUNT, HttpConstants.HEADER_RESULT_COUNT, CountColumn.KEY_RESULT_COUNT);
	}

	public void addResDetectQntLmtHeaders(HttpServletResponse response, List<Map<String, Object>> counts) {
		addCountHeaders(response, counts, HttpConstants.HEADER_TOTAL_RES_DETECT_QNT_LMT_COUNT, HttpConstants.HEADER_RES_DETECT_QNT_LMT_COUNT, CountColumn.KEY_RES_DETECT_QNT_LMT_COUNT);
	}

	public void addProjectHeaders(HttpServletResponse response, List<Map<String, Object>> counts) {
		addCountHeaders(response, counts, HttpConstants.HEADER_TOTAL_PROJECT_COUNT, HttpConstants.HEADER_PROJECT_COUNT, CountColumn.KEY_PROJECT_COUNT);
	}

	public void addOrganizationHeaders(HttpServletResponse response, List<Map<String, Object>> counts) {
		addCountHeaders(response, counts, HttpConstants.HEADER_TOTAL_ORGANIZATION_COUNT, HttpConstants.HEADER_ORGANIZATION_COUNT, CountColumn.KEY_ORGANIZATION_COUNT);
	}

	public void addProjectMonitoringLocationWeightingHeaders(HttpServletResponse response, List<Map<String, Object>> counts) {
		addCountHeaders(response, counts, HttpConstants.HEADER_TOTAL_PROJECT_MONITORING_LOCATION_WEIGHTING_COUNT, HttpConstants.HEADER_PROJECT_MONITORING_LOCATION_WEIGHTING_COUNT, CountColumn.KEY_PROJECT_MONITORING_LOCATION_WEIGHTING_COUNT);
	}
}
