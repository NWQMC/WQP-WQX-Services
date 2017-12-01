package gov.usgs.cida.wqp.mapping;

public class ProjectColumn extends BaseColumn {
	
	//ResultSet Keys
	public static final String KEY_PROJECT_IDENTIFIER = "PROJECT_IDENTIFIER";
	public static final String KEY_PROJECT_NAME = "PROJECT_NAME";
	public static final String KEY_PROJECT_DESCRIPTION = "DESCRIPTION";
	public static final String KEY_SAMPLING_DESIGN_TYPE_CODE = "SAMPLING_DESIGN_TYPE_CODE";
	public static final String KEY_QAPP_APPROVED_INDICATOR = "QAPP_APPROVED_INDICATOR";
	public static final String KEY_QAPP_APPROVAL_AGENCY_NAME = "QAPP_APPROVAL_AGENCY_NAME";
	public static final String KEY_PROJECT_FILE_URL = "PROJECT_FILE_URL";
	public static final String KEY_MONITORING_LOCATION_WEIGHT_URL = "MONITORING_LOCATION_WEIGHT_URL";
	
	//Profile Mapping of the Keys
	public static final ColumnProfile PROJECT_IDENTIFIER = new ColumnProfile(KEY_PROJECT_IDENTIFIER, Profile.PROJECT);
	public static final ColumnProfile PROJECT_NAME = new ColumnProfile(KEY_PROJECT_NAME, Profile.PROJECT);
	public static final ColumnProfile PROJECT_DESCRIPTION = new ColumnProfile(KEY_PROJECT_DESCRIPTION, Profile.PROJECT);
	public static final ColumnProfile SAMPLING_DESIGN_TYPE_CODE = new ColumnProfile(KEY_SAMPLING_DESIGN_TYPE_CODE, Profile.PROJECT);
	public static final ColumnProfile QAPP_APPROVED_INDICATOR = new ColumnProfile(KEY_QAPP_APPROVED_INDICATOR, Profile.PROJECT);
	public static final ColumnProfile QAPP_APPROVAL_AGENCY_NAME = new ColumnProfile(KEY_QAPP_APPROVAL_AGENCY_NAME, Profile.PROJECT);
	public static final ColumnProfile PROJECT_FILE_URL = new ColumnProfile(KEY_PROJECT_FILE_URL, Profile.PROJECT);
	public static final ColumnProfile MONITORING_LOCATION_WEIGHT_URL = new ColumnProfile(KEY_MONITORING_LOCATION_WEIGHT_URL, Profile.PROJECT);
	
	private ProjectColumn () {
	}
}
