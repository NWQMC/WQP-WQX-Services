package gov.usgs.cida.wqp.mapping.xml;

import static gov.usgs.cida.wqp.mapping.BaseColumn.KEY_ORGANIZATION;
import static gov.usgs.cida.wqp.mapping.BaseColumn.ORGANIZATION;
import static gov.usgs.cida.wqp.mapping.BaseColumn.ORGANIZATION_NAME;
import static gov.usgs.cida.wqp.mapping.ProjectColumn.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import gov.usgs.cida.wqp.mapping.ColumnProfile;

public class ProjectWqx extends BaseWqx implements IXmlMapping {
	
	public static final Map<String, String> HARD_BREAK = new LinkedHashMap<>();
	public static final Map<String, List<String>> COLUMN_POSITION = new LinkedHashMap<>();
	public static final Map<String, List<ColumnProfile>> GROUPING = new LinkedHashMap<>();

	static {
		HARD_BREAK.put(KEY_ORGANIZATION, ROOT_NODE);
		HARD_BREAK.put(KEY_PROJECT_IDENTIFIER, WQX_ORGANIZATION);
	}
	
	static {
		COLUMN_POSITION.put(KEY_ORGANIZATION, 
				new LinkedList<String>(Arrays.asList(
						WQX_ORGANIZATION,
						WQX_ORGANIZATION_DESCRIPTION,
						WQX_ORGANIZATION_IDENTIFIER)));
		COLUMN_POSITION.put(KEY_ORGANIZATION_NAME,
				new LinkedList<String>(Arrays.asList(
						WQX_ORGANIZATION,
						WQX_ORGANIZATION_DESCRIPTION,
						WQX_ORGANIZATION_FORMAL_NAME)));
		COLUMN_POSITION.put(KEY_PROJECT_IDENTIFIER,
				new LinkedList<String>(Arrays.asList(
						WQX_ORGANIZATION,
						WQX_PROJECT,
						WQX_PROJECT_IDENTIFIER)));
		COLUMN_POSITION.put(KEY_PROJECT_NAME,
				new LinkedList<String>(Arrays.asList(
						WQX_ORGANIZATION,
						WQX_PROJECT,
						WQX_PROJECT_NAME)));
		COLUMN_POSITION.put(KEY_PROJECT_DESCRIPTION,
				new LinkedList<String>(Arrays.asList(
						WQX_ORGANIZATION,
						WQX_PROJECT,
						WQX_PROJECT_DESCRIPTION_TEXT)));
		COLUMN_POSITION.put(KEY_SAMPLING_DESIGN_TYPE_CODE,
				new LinkedList<String>(Arrays.asList(
						WQX_ORGANIZATION,
						WQX_PROJECT,
						WQX_SAMPLING_DESIGN_TYPE_CODE)));
		COLUMN_POSITION.put(KEY_QAPP_APPROVED_INDICATOR,
				new LinkedList<String>(Arrays.asList(
						WQX_ORGANIZATION,
						WQX_PROJECT,
						WQX_QAPP_APPROVED_INDICATOR)));
		COLUMN_POSITION.put(KEY_QAPP_APPROVAL_AGENCY_NAME,
				new LinkedList<String>(Arrays.asList(
						WQX_ORGANIZATION,
						WQX_PROJECT,
						WQX_QAPP_APPROVAL_AGENCY_NAME)));
		COLUMN_POSITION.put(KEY_PROJECT_FILE_URL,
				new LinkedList<String>(Arrays.asList(
						WQX_ORGANIZATION,
						WQX_PROJECT,
						WQX_PROJECT_FILE_URL)));
		COLUMN_POSITION.put(KEY_MONITORING_LOCATION_WEIGHT_URL,
				new LinkedList<String>(Arrays.asList(
						WQX_ORGANIZATION,
						WQX_PROJECT,
						WQX_PROJECT_MONITORING_LOCATION_WEIGHT_URL)));
	}
	
	static {
		GROUPING.put(KEY_ORGANIZATION,
				new LinkedList<ColumnProfile>(Arrays.asList(ORGANIZATION, ORGANIZATION_NAME)));
		GROUPING.put(KEY_PROJECT_IDENTIFIER,
				new LinkedList<ColumnProfile>(Arrays.asList(
						PROJECT_IDENTIFIER,
						PROJECT_NAME,
						PROJECT_DESCRIPTION,
						SAMPLING_DESIGN_TYPE_CODE,
						QAPP_APPROVED_INDICATOR,
						QAPP_APPROVAL_AGENCY_NAME,
						PROJECT_FILE_URL,
						MONITORING_LOCATION_WEIGHT_URL)));
	}

	public Map<String, List<String>> getStructure() {
		return COLUMN_POSITION;
	}

	public Map<String, String> getHardBreak() {
		return HARD_BREAK;
	}

	public Map<String, List<ColumnProfile>> getGrouping() {
		return GROUPING;
	}

	public String getEntryNodeName() {
		return WQX_PROVIDER;
	}
}
