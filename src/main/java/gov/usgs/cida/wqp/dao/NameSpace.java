package gov.usgs.cida.wqp.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import gov.usgs.cida.wqp.parameter.Parameters;

public enum NameSpace {

	ORGANIZATION ("organization", null),

	STATION ("station", null),
	SIMPLE_STATION ("simpleStation", null),
	SUMMARY_STATION ("summaryStation", null),
	
	STATION_KML ("stationKml", null),

	PROJECT ("project", null),

	PROJECT_MONITORING_LOCATION_WEIGHTING ("projectMonitoringLocationWeighting", null),

	ACTIVITY ("activity", Parameters.MIN_ACTIVITIES),

	ACTIVITY_ALL ("activity", Parameters.MIN_ACTIVITIES),

	ACTIVITY_METRIC ("activityMetric", null),

	RESULT ("pcResult", Parameters.MIN_RESULTS),
	BIOLOGICAL_RESULT ("bioResult", Parameters.MIN_RESULTS),
	NARROW_RESULT ("narrowResult", Parameters.MIN_RESULTS),

	RES_DETECT_QNT_LMT ("resDetectQntLmt", null),

	LOG_MAPPER ("logMapper", null);

	private final String name;
	//Count queries for activity and result data must have the min_*** parameter with a value of at least one or sites get counted which have no data.
	//This is an artifact of the summary tables and how queries get build for the header counts.
	private final Parameters adjustParameter;
	private static Map<String, NameSpace> nameSpaceMap = new HashMap<>();

	static {
		for (NameSpace nameSpace : NameSpace.values()) {
			nameSpaceMap.put(nameSpace.name, nameSpace);
		}
	}

	private NameSpace(String value, Parameters parm) {
		name = value;
		adjustParameter = parm;
	}

	@Override
	public String toString() {
		return name;
	}

	public Parameters getAdjustParameter() {
		return adjustParameter;
	}

	public static NameSpace fromString(String nameSpace) {
		if (nameSpace == null || nameSpace.isEmpty()) {
			return null;
		}
		try {
			return nameSpaceMap.get(nameSpace);
		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}
	}

	public static Set<String> getValues() {
		return nameSpaceMap.keySet();
	}
}
