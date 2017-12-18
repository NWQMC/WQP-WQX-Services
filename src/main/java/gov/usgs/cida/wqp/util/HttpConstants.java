package gov.usgs.cida.wqp.util;

import org.springframework.http.MediaType;

public class HttpConstants {
	public static final String DEFAULT_ENCODING = "UTF-8";

	public static final String ENDPOINT_ACTIVITY = "Activity";
	public static final String ENDPOINT_ACTIVITY_METRIC = "ActivityMetric";
	public static final String ENDPOINT_RESULT = "Result";
	public static final String ENDPOINT_STATION = "Station";
	public static final String ENDPOINT_PROJECT = "Project";
	public static final String ENDPOINT_SIMPLE_STATION = "simplestation";
	public static final String ENPOINT_SEARCH = "/search";
	public static final String ENDPOINT_RES_DETECT_QNT_LMT = "ResultDetectionQuantitationLimit";

	public static final String ACTIVITY_SEARCH_ENPOINT = "/" + ENDPOINT_ACTIVITY + ENPOINT_SEARCH;
	public static final String ACTIVITY_METRIC_SEARCH_ENPOINT = "/" + ENDPOINT_ACTIVITY_METRIC + ENPOINT_SEARCH;
	public static final String RESULT_SEARCH_ENPOINT = "/" + ENDPOINT_RESULT + ENPOINT_SEARCH;
	public static final String STATION_SEARCH_ENPOINT = "/" + ENDPOINT_STATION + ENPOINT_SEARCH;
	public static final String SIMPLE_STATION_ENDPOINT = "/" + ENDPOINT_SIMPLE_STATION + ENPOINT_SEARCH;
	public static final String RES_DETECT_QNT_LMT_SEARCH_ENPOINT = "/" + ENDPOINT_RES_DETECT_QNT_LMT + ENPOINT_SEARCH;
	public static final String PROJECT_SEARCH_ENDPOINT = '/' + ENDPOINT_PROJECT + ENPOINT_SEARCH;

	public static final String ACTIVITY_REST_ENDPOINT = "/activities/{activity}";
	public static final String ACTIVITY_METRIC_REST_ENPOINT = ACTIVITY_REST_ENDPOINT + "/activitymetrics";
	public static final String RES_DETECT_QNT_LMT_REST_ENPOINT = ACTIVITY_REST_ENDPOINT + "/results/{result}/resdetectqntlmts";

	public static final int HEADER_WARNING_DEFAULT_CODE = 299;
	public static final String HEADER_FATAL_ERROR = "FATAL-ERROR";
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HEADER_CONTENT_DISPOSITION = "Content-disposition";

	public static final String HEADER_WARNING = "Warning";
	public static final String HEADER_DELIMITER = "-";
	public static final String HEADER_COUNT = "Count";
	public static final String HEADER_SITE = "Site";
	public static final String HEADER_TOTAL = "Total";

	public static final String HEADER_SITE_COUNT = HEADER_SITE + HEADER_DELIMITER + HEADER_COUNT;
	public static final String HEADER_TOTAL_SITE_COUNT = HEADER_TOTAL + HEADER_DELIMITER + HEADER_SITE_COUNT;

	public static final String HEADER_ACTIVITY_COUNT = ENDPOINT_ACTIVITY + HEADER_DELIMITER + HEADER_COUNT;
	public static final String HEADER_TOTAL_ACTIVITY_COUNT = HEADER_TOTAL + HEADER_DELIMITER + HEADER_ACTIVITY_COUNT;

	public static final String HEADER_ACTIVITY_METRIC_COUNT = ENDPOINT_ACTIVITY_METRIC + HEADER_DELIMITER + HEADER_COUNT;
	public static final String HEADER_TOTAL_ACTIVITY_METRIC_COUNT = HEADER_TOTAL + HEADER_DELIMITER + HEADER_ACTIVITY_METRIC_COUNT;

	public static final String HEADER_RESULT_COUNT = ENDPOINT_RESULT + HEADER_DELIMITER + HEADER_COUNT;
	public static final String HEADER_TOTAL_RESULT_COUNT = HEADER_TOTAL + HEADER_DELIMITER + HEADER_RESULT_COUNT;

	public static final String HEADER_RES_DETECT_QNT_LMT_COUNT = ENDPOINT_RES_DETECT_QNT_LMT + HEADER_DELIMITER + HEADER_COUNT;
	public static final String HEADER_TOTAL_RES_DETECT_QNT_LMT_COUNT = HEADER_TOTAL + HEADER_DELIMITER + HEADER_RES_DETECT_QNT_LMT_COUNT;
	
	public static final String HEADER_PROJECT_COUNT = ENDPOINT_PROJECT + HEADER_DELIMITER + HEADER_COUNT;
	public static final String HEADER_TOTAL_PROJECT_COUNT = HEADER_TOTAL + HEADER_DELIMITER + HEADER_PROJECT_COUNT;

	public static final String MIME_TYPE_ZIP  = "application/zip";
	public static final String MIME_TYPE_CSV  = "text/csv";
	public static final String MIME_TYPE_TSV  = "text/tab-separated-values";
	public static final String MIME_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String MIME_TYPE_XML  = MediaType.APPLICATION_XML_VALUE;
	public static final String MIME_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE;
	public static final String MIME_TYPE_KML = "application/vnd.google-earth.kml+xml";
	public static final String MIME_TYPE_KMZ = "application/vnd.google-earth.kmz";
	public static final String MIME_TYPE_GEOJSON = "application/vnd.geo+json";
	public static final String MIME_TYPE_TEXT = "text/plain";

	private HttpConstants() {
	}

}