package gov.usgs.cida.wqp.webservice;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import gov.usgs.cida.wqp.dao.BaseDao;
import gov.usgs.cida.wqp.dao.StreamingResultHandler;
import gov.usgs.cida.wqp.dao.intfc.ICountDao;
import gov.usgs.cida.wqp.dao.intfc.IStreamingDao;
import gov.usgs.cida.wqp.mapping.Profile;
import gov.usgs.cida.wqp.mapping.xml.IXmlMapping;
import gov.usgs.cida.wqp.parameter.IParameterHandler;
import gov.usgs.cida.wqp.parameter.ParameterMap;
import gov.usgs.cida.wqp.parameter.Parameters;
import gov.usgs.cida.wqp.service.ILogService;
import gov.usgs.cida.wqp.transform.MapToDelimitedTransformer;
import gov.usgs.cida.wqp.transform.MapToJsonTransformer;
import gov.usgs.cida.wqp.transform.MapToKmlTransformer;
import gov.usgs.cida.wqp.transform.MapToXlsxTransformer;
import gov.usgs.cida.wqp.transform.MapToXmlTransformer;
import gov.usgs.cida.wqp.transform.Transformer;
import gov.usgs.cida.wqp.util.HttpConstants;
import gov.usgs.cida.wqp.util.MimeType;
import gov.usgs.cida.wqp.util.MybatisConstants;

public abstract class BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

	protected final IStreamingDao streamingDao;
	protected final ICountDao countDao;
	protected final IParameterHandler parameterHandler;
	protected final ILogService logService;
	protected final Integer maxResultRows;
	protected final String siteUrlBase;

	private static ThreadLocal<ParameterMap> pm = new ThreadLocal<>();
	private static ThreadLocal<MimeType> mimeType = new ThreadLocal<>();
	private static ThreadLocal<Boolean> zipped = new ThreadLocal<>();
	private static ThreadLocal<BigDecimal> logId = new ThreadLocal<>();
	private static ThreadLocal<Map<String, String>> counts = new ThreadLocal<>();
	private static ThreadLocal<String> mybatisNamespace = new ThreadLocal<>();
	private static ThreadLocal<String> profile = new ThreadLocal<>();

	public BaseController(IStreamingDao inStreamingDao, ICountDao inCountDao,
			IParameterHandler inParameterHandler, ILogService inLogService,
			Integer inMaxResultRows, String inSiteUrlBase) {
		LOG.trace(getClass().getName());

		streamingDao = inStreamingDao;
		parameterHandler = inParameterHandler;
		countDao = inCountDao;
		logService = inLogService;
		maxResultRows = inMaxResultRows;
		siteUrlBase = inSiteUrlBase;
	}

	public static ParameterMap getPm() {
		return pm.get();
	}

	public static void setPm(ParameterMap inPm) {
		pm.set(inPm);
	}

	public static MimeType getMimeType() {
		return mimeType.get();
	}

	public static void setMimeType(MimeType inMimeType) {
		mimeType.set(inMimeType);
	}

	public static boolean getZipped() {
		return zipped.get();
	}

	public static void setZipped(Boolean inZipped) {
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

	public static String getMybatisNamespace() {
		return mybatisNamespace.get();
	}

	public static void setMybatisNamespace(String inMybatisNamespace) {
		mybatisNamespace.set(inMybatisNamespace);
	}

	public static String getProfile() {
		return profile.get();
	}

	public static void setProfile(String inProfile) {
		profile.set(inProfile);
	}

	public static void remove() {
		pm.remove();
		mimeType.remove();
		zipped.remove();
		logId.remove();
		counts.remove();
		mybatisNamespace.remove();
		profile.remove();
	}

	protected boolean determineZipped(String zipParm, String mimeType) {
		return "yes".equalsIgnoreCase(zipParm) || MimeType.kmz.toString().equalsIgnoreCase(mimeType);
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

	protected MimeType determineMimeType(MimeType mimeType, boolean zipped) {
		if (zipped && MimeType.kml.equals(mimeType)) {
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

	protected String determineBaseFileName() {
		switch (getProfile()) {
		case Profile.BIOLOGICAL:
			return "biologicalresult";
		case Profile.PC_RESULT:
			return "result";
		default:
			return getProfile().toLowerCase();
		}
	}

	protected String getAttachementFileName() {
		StringBuilder ret = new StringBuilder(determineBaseFileName());
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

	protected void doHeadRequest(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("Processing Head: {}", request.getQueryString());
		setLogId(logService.logRequest(request, response, null));

		try {
			doCommonSetup(request, response, null);
		} finally {
			logService.logRequestComplete(getLogId(), String.valueOf(response.getStatus()));
			LOG.info("Processing Head complete: {}", request.getQueryString());
			remove();
		}
	}

	protected boolean doCommonSetup(HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> postParms) {
		response.setCharacterEncoding(HttpConstants.DEFAULT_ENCODING);
		processParameters(request, postParms);
		if (!getPm().isValid() ) {
			writeWarningHeaders(response, getPm().getValidationMessages());
			LOG.info("Processing Head invalid params end:{}", getPm().getValidationMessages());
			return false;
		}

		String mimeTypeParam = (String) getPm().getQueryParameters().get(Parameters.MIMETYPE.toString());
		String zipParam = (String) getPm().getQueryParameters().get(Parameters.ZIP.toString());
		setZipped(determineZipped(zipParam, mimeTypeParam));
		setMimeType(determineMimeType(MimeType.csv.fromString(mimeTypeParam), getZipped()));
		setProfile(determineProfile(getPm().getQueryParameters()));
		setMybatisNamespace(determineNamespace());

		List<Map<String, Object>> counts = countDao.getCounts(getMybatisNamespace(), getPm().getQueryParameters());

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

		return checkMaxRows(response, response.getHeader(totalHeader));
	}

	protected boolean checkMaxRows(HttpServletResponse response, String totalRows) {
		switch (getMimeType()) {
		case kml:
		case kmz:
		case xml:
			if (Integer.valueOf(totalRows) <= maxResultRows) {
				getPm().getQueryParameters().put(Parameters.SORTED.toString(), "yes");
				return true;
			} else {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				response.addHeader(HttpConstants.HEADER_WARNING, "This query will return in excess of " + maxResultRows + " results, please refine your query.");
				return false;
			}
		default:
			if (Integer.valueOf(totalRows) > maxResultRows) {
				getPm().getQueryParameters().put(Parameters.SORTED.toString(), "no");
				response.addHeader(HttpConstants.HEADER_WARNING, "This query will return in excess of " + maxResultRows + " results, the data will not be sorted.");
			} else {
				if (!getPm().getQueryParameters().containsKey(Parameters.SORTED.toString())) {
					getPm().getQueryParameters().put(Parameters.SORTED.toString(), "yes");
				}
			}
			return true;
		}
	}

	protected void processParameters(HttpServletRequest request, Map<String, Object> postParms) {
		setPm(new ParameterMap());
		Object pathVariables = request.getAttribute("org.springframework.web.servlet.View.pathVariables");
		if (isValidRequest(request, postParms, pathVariables)) {
			LOG.trace("got parameters");
			Map<String, String[]> requestParams = new HashMap<>(request.getParameterMap());
			LOG.debug("requestParams: {}", requestParams);
			setPm(parameterHandler.validateAndTransform(requestParams, postParms, pathVariables));
			LOG.debug("pm: {}", getPm());
			LOG.debug("queryParms: {}", getPm().getQueryParameters());
		} else {
			LOG.debug("No parameters");
			getPm().setValid(false);
		}
	}

	protected boolean isValidRequest(HttpServletRequest request, Map<String, Object> postParms, Object pathVariables) {
		if (null != request && null != request.getParameterMap() && !request.getParameterMap().isEmpty()) {
			return true;
		}
		if (null != postParms && !postParms.isEmpty()) {
			return true;
		}
		return null != pathVariables;
	}

	protected abstract String addCountHeaders(HttpServletResponse response, List<Map<String, Object>> counts);

	protected void doGetRequest(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("Processing Get: {}", request.getQueryString());
		setLogId(logService.logRequest(request, response, null));
		OutputStream responseStream = null;
		String realHttpStatus = String.valueOf(response.getStatus());

		try {
			if (doCommonSetup(request, response, null)) {
				responseStream = getOutputStream(response, getZipped(), determineZipEntryName());
				Transformer transformer = getTransformer(responseStream, getLogId());

				ResultHandler<?> handler = new StreamingResultHandler(transformer);
				streamingDao.stream(getMybatisNamespace(), getPm().getQueryParameters(), handler);

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
			LOG.info("Processing Get complete: {}", request.getQueryString());
			logService.logRequestComplete(getLogId(), realHttpStatus);
			remove();
		}
	}

	protected void doPostRequest(HttpServletRequest request, HttpServletResponse response, Map<String, Object> postParms) {
		LOG.info("Processing Post: {}", postParms);
		setLogId(logService.logRequest(request, response, postParms));
		OutputStream responseStream = null;
		String realHttpStatus = String.valueOf(response.getStatus());

		try {
			if (doCommonSetup(request, response, postParms)) {
				responseStream = getOutputStream(response, getZipped(), determineZipEntryName());
				Transformer transformer = getTransformer(responseStream, getLogId());

				ResultHandler<?> handler = new StreamingResultHandler(transformer);
				streamingDao.stream(getMybatisNamespace(), getPm().getQueryParameters(), handler);

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
			LOG.info("Processing POST complete: {}", request.getQueryString());
			logService.logRequestComplete(getLogId(), realHttpStatus);
			remove();
		}
	}

	protected Map<String, String> doPostCountRequest(HttpServletRequest request, HttpServletResponse response, Map<String, Object> postParms) {
		LOG.info("Processing Post Count");
		setLogId(logService.logRequest(request, response, postParms));
		Map<String, String> counts = null;

		try {
			doCommonSetup(request, response, postParms);
			counts = getCounts();
		} finally {
			logService.logRequestComplete(getLogId(), String.valueOf(response.getStatus()));
			LOG.info("Processing Post Count complete");
			remove();
		}

		return counts;
	}

	protected String determineZipEntryName() {
		if (MimeType.kml.equals(getMimeType()) || MimeType.kmz.equals(getMimeType())) {
			return determineBaseFileName() + "." + MimeType.kml;
		} else {
			return determineBaseFileName().toLowerCase() + "." + getMimeType();
		}
	}

	protected String determineNamespace() {
		switch (getMimeType()) {
		case kml:
		case kmz:
			return BaseDao.STATION_KML_NAMESPACE;
		case geojson:
			return BaseDao.SIMPLE_STATION_NAMESPACE;
		default:
			switch (getProfile()) {
			case Profile.BIOLOGICAL:
				return BaseDao.BIOLOGICAL_RESULT_NAMESPACE;
			case Profile.PC_RESULT:
				return BaseDao.RESULT_NAMESPACE;
			case Profile.STATION:
				return BaseDao.STATION_NAMESPACE;
			case Profile.SIMPLE_STATION:
				return BaseDao.SIMPLE_STATION_NAMESPACE;
			case Profile.ACTIVITY:
				return BaseDao.ACTIVITY_NAMESPACE;
			case Profile.ACTIVITY_METRIC:
				return BaseDao.ACTIVITY_METRIC_NAMESPACE;
			default:
				//Should never get here...
				return "";
			}
		}
	}

	protected Transformer getTransformer(OutputStream responseStream, BigDecimal logId) {
		Transformer transformer;
		switch (getMimeType()) {
		case json:
		case geojson:
			transformer = new MapToJsonTransformer(responseStream, null, logService, logId, siteUrlBase);
			break;
		case xlsx:
			transformer = new MapToXlsxTransformer(responseStream, getMapping(getProfile()), logService, logId, siteUrlBase);
			break;
		case xml:
			transformer = new MapToXmlTransformer(responseStream, getXmlMapping(), logService, logId, getProfile(), siteUrlBase);
			break;
		case kml:
		case kmz:
			transformer = new MapToKmlTransformer(responseStream, getKmlMapping(), logService, logId, getProfile());
			break;
		case tsv:
			transformer = new MapToDelimitedTransformer(responseStream, getMapping(getProfile()), logService, logId, MapToDelimitedTransformer.TAB, siteUrlBase);
			break;
		case csv:
		default:
			transformer = new MapToDelimitedTransformer(responseStream, getMapping(getProfile()), logService, logId, MapToDelimitedTransformer.COMMA, siteUrlBase);
			break;
		}
		return transformer;
	}

	protected abstract String determineProfile(Map<String, Object> pm);

	protected String determineProfile(String defaultProfile, Map<String, Object> pm) {
		String profile = defaultProfile;
		if (null != pm && pm.containsKey(Parameters.DATA_PROFILE.toString())) {
			Object dataProfile = pm.get(Parameters.DATA_PROFILE.toString());
			if (dataProfile instanceof String[] && 0 < ((String[]) dataProfile).length)
			profile = ((String[]) dataProfile)[0];
		}
		return profile;
	}

	protected abstract Map<String, String> getMapping(String profile);

	protected abstract IXmlMapping getXmlMapping();

	protected abstract IXmlMapping getKmlMapping();

	public void writeWarningHeaders(HttpServletResponse response, Map<String, List<String>> validationMessages) {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		for (List<String> msgs : validationMessages.values()) {
			for (String msg : msgs) {
				response.addHeader(HttpConstants.HEADER_WARNING, warningHeader(null, msg, null));
			}
		}
	}

	protected String warningHeader(Integer code, String text, String date) {
		StringBuilder rtn = new StringBuilder();
		rtn.append(null == code ? HttpConstants.HEADER_WARNING_DEFAULT_CODE : code);
		rtn.append(" WQP \"");
		rtn.append(null == text || 0 == text.length() ? "Unknown error" : text);
		rtn.append("\" ");
		rtn.append(null == date || 0 == date.length() ? new Date().toString() : date);
		return rtn.toString();
	}

	protected String determineHeaderName(Map<String, Object> count, String suffix) {
		String mySuffix = suffix==null ? "" : suffix;
		if (null == count) {
			return HttpConstants.HEADER_TOTAL + HttpConstants.HEADER_DELIMITER + mySuffix;
		} else {
			Object provider = count.get(MybatisConstants.DATA_SOURCE);

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
		addCountHeaders(response, counts, HttpConstants.HEADER_TOTAL_SITE_COUNT, HttpConstants.HEADER_SITE_COUNT, MybatisConstants.STATION_COUNT);
	}

	public void addActivityHeaders(HttpServletResponse response, List<Map<String, Object>> counts) {
		addCountHeaders(response, counts, HttpConstants.HEADER_TOTAL_ACTIVITY_COUNT, HttpConstants.HEADER_ACTIVITY_COUNT, MybatisConstants.ACTIVITY_COUNT);
	}

	public void addActivityMetricHeaders(HttpServletResponse response, List<Map<String, Object>> counts) {
		addCountHeaders(response, counts, HttpConstants.HEADER_TOTAL_ACTIVITY_METRIC_COUNT, HttpConstants.HEADER_ACTIVITY_METRIC_COUNT, MybatisConstants.ACTIVITY_METRIC_COUNT);
	}

	public void addResultHeaders(HttpServletResponse response, List<Map<String, Object>> counts) {
		addCountHeaders(response, counts, HttpConstants.HEADER_TOTAL_RESULT_COUNT, HttpConstants.HEADER_RESULT_COUNT, MybatisConstants.RESULT_COUNT);
	}

}
