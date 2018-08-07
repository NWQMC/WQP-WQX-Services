package gov.usgs.cida.wqp.util;

import org.springframework.http.MediaType;

public class HttpConstants {
	public static final String DEFAULT_ENCODING = "UTF-8";

	public static final String ENDPOINT_ORGANIZATION = "Organization";
	public static final String ENDPOINT_ACTIVITY = "Activity";
	public static final String ENDPOINT_ACTIVITY_METRIC = "ActivityMetric";
	public static final String ENDPOINT_RESULT = "Result";
	public static final String ENDPOINT_STATION = "Station";
	public static final String ENDPOINT_PROJECT = "Project";
	public static final String ENDPOINT_PROJECT_MONITORING_LOCATION_WEIGHTING = "ProjectMonitoringLocationWeighting";
	public static final String ENDPOINT_SIMPLE_STATION = "simplestation";
	public static final String ENDPOINT_SEARCH = "/search";
	public static final String ENDPOINT_SUMMARY_STATION = "summarystation";
	public static final String ENDPOINT_RES_DETECT_QNT_LMT = "ResultDetectionQuantitationLimit";
	public static final String ENDPOINT_FILE = "/files";

	public static final String ACTIVITY_SEARCH_ENDPOINT = "/" + ENDPOINT_ACTIVITY + ENDPOINT_SEARCH;
	public static final String ACTIVITY_METRIC_SEARCH_ENDPOINT = "/" + ENDPOINT_ACTIVITY_METRIC + ENDPOINT_SEARCH;
	public static final String RESULT_SEARCH_ENDPOINT = "/" + ENDPOINT_RESULT + ENDPOINT_SEARCH;
	public static final String STATION_SEARCH_ENDPOINT = "/" + ENDPOINT_STATION + ENDPOINT_SEARCH;
	public static final String SIMPLE_STATION_ENDPOINT = "/" + ENDPOINT_SIMPLE_STATION + ENDPOINT_SEARCH;
	public static final String SUMMARY_STATION_ENDPOINT = "/" + ENDPOINT_SUMMARY_STATION + ENDPOINT_SEARCH;
	public static final String RES_DETECT_QNT_LMT_SEARCH_ENDPOINT = "/" + ENDPOINT_RES_DETECT_QNT_LMT + ENDPOINT_SEARCH;
	public static final String PROJECT_SEARCH_ENDPOINT = '/' + ENDPOINT_PROJECT + ENDPOINT_SEARCH;
	public static final String PROJECT_MONITORING_LOCATION_WEIGHTING_SEARCH_ENDPOINT = '/' + ENDPOINT_PROJECT_MONITORING_LOCATION_WEIGHTING + ENDPOINT_SEARCH;
	public static final String ORGANIZATION_SEARCH_ENDPOINT = "/" + ENDPOINT_ORGANIZATION + ENDPOINT_SEARCH;

	public static final String PROJECT_MONITORING_LOCATION_WEIGHTING_REST_ENDPOINT = "/organizations/{organization}/projects/{projectIdentifier}/projectMonitoringLocationWeightings";

	public static final String ACTIVITY_REST_ENDPOINT = "/activities/{activity}";
	public static final String ACTIVITY_METRIC_REST_ENDPOINT = ACTIVITY_REST_ENDPOINT + "/activitymetrics";
	public static final String RES_DETECT_QNT_LMT_REST_ENDPOINT = ACTIVITY_REST_ENDPOINT + "/results/{result}/resdetectqntlmts";

	public static final String ORGANIZATION_REST_ENDPOINT = "/organizations/{organization}";
	public static final String MONITORING_LOCATION_FILE_REST_ENDPOINT = ORGANIZATION_REST_ENDPOINT + "/monitoringlocations/{monitoringLocation}" + ENDPOINT_FILE;
	public static final String PROJECT_FILE_REST_ENDPOINT = ORGANIZATION_REST_ENDPOINT + "/projects/{project}" + ENDPOINT_FILE;
	public static final String ACTIVITY_FILE_REST_ENDPOINT = ORGANIZATION_REST_ENDPOINT + ACTIVITY_REST_ENDPOINT + ENDPOINT_FILE;
	public static final String RESULT_FILE_REST_ENDPOINT = ORGANIZATION_REST_ENDPOINT + ACTIVITY_REST_ENDPOINT + "/results/{result}" + ENDPOINT_FILE;

	public static final int HEADER_WARNING_DEFAULT_CODE = 299;
	public static final String HEADER_FATAL_ERROR = "FATAL-ERROR";
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HEADER_CONTENT_DISPOSITION = "Content-disposition";

	public static final String HEADER_WARNING = "Warning";
	public static final String HEADER_DELIMITER = "-";
	public static final String HEADER_COUNT = "Count";
	public static final String HEADER_SITE = "Site";
	public static final String HEADER_TOTAL = "Total";

	public static final String HEADER_ORGANIZATION_COUNT = ENDPOINT_ORGANIZATION + HEADER_DELIMITER + HEADER_COUNT;
	public static final String HEADER_TOTAL_ORGANIZATION_COUNT = HEADER_TOTAL + HEADER_DELIMITER + HEADER_ORGANIZATION_COUNT;

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

	public static final String HEADER_PROJECT_MONITORING_LOCATION_WEIGHTING_COUNT = ENDPOINT_PROJECT_MONITORING_LOCATION_WEIGHTING + HEADER_DELIMITER + HEADER_COUNT;
	public static final String HEADER_TOTAL_PROJECT_MONITORING_LOCATION_WEIGHTING_COUNT = HEADER_TOTAL + HEADER_DELIMITER + HEADER_PROJECT_MONITORING_LOCATION_WEIGHTING_COUNT;

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