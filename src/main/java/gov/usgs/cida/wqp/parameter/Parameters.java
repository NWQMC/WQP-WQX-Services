package gov.usgs.cida.wqp.parameter;

import java.util.HashMap;
import java.util.Map;

public enum Parameters {
	ACTIVITY("activity"),
	ANALYTICAL_METHOD("analyticalmethod"),
	ASSEMBLAGE("assemblage"),
	AVOID("command.avoid"),
	BBOX("bBox"),
	CHARACTERISTIC_NAME("characteristicName"),
	CHARACTERISTIC_TYPE("characteristicType"),
	COUNTRY("countrycode"),
	COUNTY("countycode"),
	DATA_PROFILE("dataProfile"),
	HUC("huc"),
	LATITUDE("lat"),
	LONGITUDE("long"),
	MIMETYPE("mimeType"),
	MIN_ACTIVITIES("minactivities"),
	MIN_RESULTS("minresults"),
	NLDIURL("nldiurl"),
	ORGANIZATION("organization"),
	PCODE("pCode"),
	PROJECT("project"),
	PROVIDERS("providers"),
	RESULT("result"),
	SAMPLE_MEDIA("sampleMedia"),
	SITEID("siteid"),
	SITE_TYPE("siteType"),
	SORTED("sorted"),
	START_DATE_HI("startDateHi"),
	START_DATE_LO("startDateLo"),
	STATE("statecode"),
	SUBJECT_TAXONOMIC_NAME("subjectTaxonomicName"),
	SUMMARY_YEARS("summaryYears"),
	WITHIN("within"),
	ZIP("zip");

	private final String parameterName;
	private static Map<String, Parameters> validParameterNames;

	private Parameters(String value) {
		parameterName = value;
		putParameterName(value, this);
	}

	private static void putParameterName(String value, Parameters param) {
		if (value == null || getValidParameterNames().containsKey(value)) {
			//This is a configuration error and can only happen if you duplicate values above...not unit testable
			throw new IllegalStateException("overloaded parameter value: " + value + ", please verify the enum definition for Parameters");
		} else {
			getValidParameterNames().put(value, param);
		}
	}

	private static Map<String, Parameters> getValidParameterNames() {
		if (validParameterNames == null) {
			validParameterNames = new HashMap<String, Parameters>();
		}
		return validParameterNames;
	}

	@Override
	public String toString() {
		return parameterName;
	}

}