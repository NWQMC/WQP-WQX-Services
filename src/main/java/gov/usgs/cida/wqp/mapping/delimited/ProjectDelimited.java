package gov.usgs.cida.wqp.mapping.delimited;

import static gov.usgs.cida.wqp.mapping.BaseColumn.*;
import static gov.usgs.cida.wqp.mapping.ProjectColumn.*;
import static gov.usgs.cida.wqp.mapping.xml.BaseWqx.*;

import java.util.LinkedHashMap;
import java.util.Map;

import gov.usgs.cida.wqp.mapping.ColumnProfile;
import gov.usgs.cida.wqp.mapping.Profile;

public class ProjectDelimited extends BaseDelimited {
	
	//Column Headings for the Keys
	public static final String VALUE_PROJECT = WQX_PROJECT_IDENTIFIER;
	public static final String VALUE_PROJECT_NAME = WQX_PROJECT_NAME;
	public static final String VALUE_PROJECT_DESCRIPTION_TEXT = WQX_PROJECT_DESCRIPTION_TEXT;
	public static final String VALUE_SAMPLING_DESIGN_TYPE_CODE = WQX_SAMPLING_DESIGN_TYPE_CODE;
	public static final String VALUE_QAPP_APPROVED_INDICATOR = WQX_QAPP_APPROVED_INDICATOR;
	public static final String VALUE_QAPP_APPROVAL_AGENCY_NAME = WQX_QAPP_APPROVAL_AGENCY_NAME;
	public static final String VALUE_PROJECT_FILE_URL = WQX_PROJECT_FILE_URL;
	public static final String VALUE_PROJECT_MONITORING_LOCATION_WEIGHT_URL = WQX_PROJECT_MONITORING_LOCATION_WEIGHT_URL;
	
	public static final Map<ColumnProfile, String> MAPPINGS;
	static {
		MAPPINGS = new LinkedHashMap<ColumnProfile,String>();
		MAPPINGS.put(ORGANIZATION, VALUE_ORGANIZATION_IDENTIFIER);
		MAPPINGS.put(ORGANIZATION_NAME, VALUE_ORGANIZATION_FORMAL_NAME);
		MAPPINGS.put(PROJECT_IDENTIFIER, VALUE_PROJECT);
		MAPPINGS.put(PROJECT_NAME, VALUE_PROJECT_NAME);
		MAPPINGS.put(PROJECT_DESCRIPTION, VALUE_PROJECT_DESCRIPTION_TEXT);
		MAPPINGS.put(SAMPLING_DESIGN_TYPE_CODE, VALUE_SAMPLING_DESIGN_TYPE_CODE);
		MAPPINGS.put(QAPP_APPROVED_INDICATOR, VALUE_QAPP_APPROVED_INDICATOR);
		MAPPINGS.put(QAPP_APPROVAL_AGENCY_NAME, VALUE_QAPP_APPROVAL_AGENCY_NAME);
		MAPPINGS.put(PROJECT_FILE_URL, VALUE_PROJECT_FILE_URL);
		MAPPINGS.put(MONITORING_LOCATION_WEIGHT_URL, VALUE_PROJECT_MONITORING_LOCATION_WEIGHT_URL);
	}
	
	private ProjectDelimited() {
	}
	
	public static Map<String, String> getMapping(Profile profile) {
		return getMapping(MAPPINGS, profile);
	}
}
