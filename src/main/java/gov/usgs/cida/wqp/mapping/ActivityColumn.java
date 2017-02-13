package gov.usgs.cida.wqp.mapping;

public class ActivityColumn extends BaseColumn {

	//ResultSet Keys
	public static final String KEY_ACTIVITY_ID = "ACTIVITY_ID";
	public static final String KEY_ACTIVITY = "ACTIVITY";
	public static final String KEY_ACTIVITY_TYPE_CODE = "ACTIVITY_TYPE_CODE";
	public static final String KEY_SAMPLE_MEDIA = "SAMPLE_MEDIA";
	public static final String KEY_ACTIVITY_MEDIA_SUBDIV_NAME = "ACTIVITY_MEDIA_SUBDIV_NAME";
	public static final String KEY_EVENT_DATE = "EVENT_DATE";
	public static final String KEY_ACTIVITY_START_TIME = "ACTIVITY_START_TIME";
	public static final String KEY_ACT_START_TIME_ZONE = "ACT_START_TIME_ZONE";
	public static final String KEY_ACTIVITY_STOP_DATE = "ACTIVITY_STOP_DATE";
	public static final String KEY_ACTIVITY_STOP_TIME = "ACTIVITY_STOP_TIME";
	public static final String KEY_ACT_STOP_TIME_ZONE = "ACT_STOP_TIME_ZONE";
	public static final String KEY_ACTIVITY_RELATIVE_DEPTH = "ACTIVITY_RELATIVE_DEPTH_NAME";
	public static final String KEY_ACTIVITY_DEPTH = "ACTIVITY_DEPTH";
	public static final String KEY_ACTIVITY_DEPTH_UNIT = "ACTIVITY_DEPTH_UNIT";
	public static final String KEY_ACTIVITY_DEPTH_REF_POINT = "ACTIVITY_DEPTH_REF_POINT";
	public static final String KEY_ACTIVITY_UPPER_DEPTH = "ACTIVITY_UPPER_DEPTH";
	public static final String KEY_ACTIVITY_UPPER_DEPTH_UNIT = "ACTIVITY_UPPER_DEPTH_UNIT";
	public static final String KEY_ACTIVITY_LOWER_DEPTH = "ACTIVITY_LOWER_DEPTH";
	public static final String KEY_ACTIVITY_LOWER_DEPTH_UNIT = "ACTIVITY_LOWER_DEPTH_UNIT";
	public static final String KEY_PROJECT_ID = "PROJECT_ID";
	public static final String KEY_ACTIVITY_CONDUCTING_ORG = "ACTIVITY_CONDUCTING_ORG";
	public static final String KEY_ACTIVITY_COMMENT = "ACTIVITY_COMMENT";
	public static final String KEY_SAMPLE_AQFR_NAME = "SAMPLE_AQFR_NAME";
	public static final String KEY_HYDROLOGIC_CONDITION_NAME = "HYDROLOGIC_CONDITION_NAME";
	public static final String KEY_HYDROLOGIC_EVENT_NAME = "HYDROLOGIC_EVENT_NAME";
	public static final String KEY_ACTIVITY_LATITUDE = "ACTIVITY_LATITUDE";
	public static final String KEY_ACTIVITY_LONGITUDE = "ACTIVITY_LONGITUDE";
	public static final String KEY_ACTIVITY_SOURCE_MAP_SCALE = "ACTIVITY_SOURCE_MAP_SCALE";
	public static final String KEY_ACT_HORIZONTAL_ACCURACY = "ACT_HORIZONTAL_ACCURACY";
	public static final String KEY_ACT_HORIZONTAL_ACCURACY_UNIT = "ACT_HORIZONTAL_ACCURACY_UNIT";
	public static final String KEY_ACT_HORIZONTAL_COLLECT_METHOD = "ACT_HORIZONTAL_COLLECT_METHOD";
	public static final String KEY_ACT_HORIZONTAL_DATUM_NAME = "ACT_HORIZONTAL_DATUM_NAME";
	public static final String KEY_ASSEMBLAGE_SAMPLED_NAME = "ASSEMBLAGE_SAMPLED_NAME";
	public static final String KEY_ACT_COLLECTION_DURATION = "ACT_COLLECTION_DURATION";
	public static final String KEY_ACT_COLLECTION_DURATION_UNIT = "ACT_COLLECTION_DURATION_UNIT";
	public static final String KEY_ACT_SAM_COMPNT_NAME = "ACT_SAM_COMPNT_NAME";
	public static final String KEY_ACT_SAM_COMPNT_PLACE_IN_SERIES = "ACT_SAM_COMPNT_PLACE_IN_SERIES";
	public static final String KEY_ACT_REACH_LENGTH = "ACT_REACH_LENGTH";
	public static final String KEY_ACT_REACH_LENGTH_UNIT = "ACT_REACH_LENGTH_UNIT";
	public static final String KEY_ACT_REACH_WIDTH = "ACT_REACH_WIDTH";
	public static final String KEY_ACT_REACH_WIDTH_UNIT = "ACT_REACH_WIDTH_UNIT";
	public static final String KEY_ACT_PASS_COUNT = "ACT_PASS_COUNT";
	public static final String KEY_NET_TYPE_NAME = "NET_TYPE_NAME";
	public static final String KEY_ACT_NET_SURFACE_AREA = "ACT_NET_SURFACE_AREA";
	public static final String KEY_ACT_NET_SURFACE_AREA_UNIT = "ACT_NET_SURFACE_AREA_UNIT";
	public static final String KEY_ACT_NET_MESH_SIZE = "ACT_NET_MESH_SIZE";
	public static final String KEY_ACT_NET_MESH_SIZE_UNIT = "ACT_NET_MESH_SIZE_UNIT";
	public static final String KEY_ACT_BOAT_SPEED = "ACT_BOAT_SPEED";
	public static final String KEY_ACT_BOAT_SPEED_UNIT = "ACT_BOAT_SPEED_UNIT";
	public static final String KEY_ACT_CURRENT_SPEED = "ACT_CURRENT_SPEED";
	public static final String KEY_ACT_CURRENT_SPEED_UNIT = "ACT_CURRENT_SPEED_UNIT";
	public static final String KEY_TOXICITY_TEST_TYPE_NAME = "TOXICITY_TEST_TYPE_NAME";
	public static final String KEY_SAMPLE_COLLECT_METHOD_ID = "SAMPLE_COLLECT_METHOD_ID";
	public static final String KEY_SAMPLE_COLLECT_METHOD_CTX = "SAMPLE_COLLECT_METHOD_CTX";
	public static final String KEY_SAMPLE_COLLECT_METHOD_NAME = "SAMPLE_COLLECT_METHOD_NAME";
	public static final String KEY_ACT_SAM_COLLECT_METH_QUAL_TYPE = "ACT_SAM_COLLECT_METH_QUAL_TYPE";
	public static final String KEY_ACT_SAM_COLLECT_METH_DESC = "ACT_SAM_COLLECT_METH_DESC";
	public static final String KEY_SAMPLE_COLLECT_EQUIP_NAME = "SAMPLE_COLLECT_EQUIP_NAME";
	public static final String KEY_ACT_SAM_COLLECT_EQUIP_COMMENTS = "ACT_SAM_COLLECT_EQUIP_COMMENTS";
	public static final String KEY_ACT_SAM_PREP_METH_ID = "ACT_SAM_PREP_METH_ID";
	public static final String KEY_ACT_SAM_PREP_METH_CONTEXT = "ACT_SAM_PREP_METH_CONTEXT";
	public static final String KEY_ACT_SAM_PREP_METH_NAME = "ACT_SAM_PREP_METH_NAME";
	public static final String KEY_ACT_SAM_PREP_METH_QUAL_TYPE = "ACT_SAM_PREP_METH_QUAL_TYPE";
	public static final String KEY_ACT_SAM_PREP_METH_DESC = "ACT_SAM_PREP_METH_DESC";
	public static final String KEY_SAMPLE_CONTAINER_TYPE = "SAMPLE_CONTAINER_TYPE";
	public static final String KEY_SAMPLE_CONTAINER_COLOR = "SAMPLE_CONTAINER_COLOR";
	public static final String KEY_ACT_SAM_CHEMICAL_PRESERVATIVE = "ACT_SAM_CHEMICAL_PRESERVATIVE";
	public static final String KEY_THERMAL_PRESERVATIVE_NAME = "THERMAL_PRESERVATIVE_NAME";
	public static final String KEY_ACT_SAM_TRANSPORT_STORAGE_DESC = "ACT_SAM_TRANSPORT_STORAGE_DESC";


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Profile Mapping of the Keys
	public static final ColumnProfile ACTIVITY = new ColumnProfile(KEY_ACTIVITY, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY, Profile.ACTIVITY_METRIC);
	public static final ColumnProfile ACTIVITY_TYPE_CODE = new ColumnProfile(KEY_ACTIVITY_TYPE_CODE, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile SAMPLE_MEDIA = new ColumnProfile(KEY_SAMPLE_MEDIA, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_MEDIA_SUBDIV_NAME = new ColumnProfile(KEY_ACTIVITY_MEDIA_SUBDIV_NAME, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile EVENT_DATE = new ColumnProfile(KEY_EVENT_DATE, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_START_TIME = new ColumnProfile(KEY_ACTIVITY_START_TIME, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACT_START_TIME_ZONE = new ColumnProfile(KEY_ACT_START_TIME_ZONE, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_STOP_DATE = new ColumnProfile(KEY_ACTIVITY_STOP_DATE, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_STOP_TIME = new ColumnProfile(KEY_ACTIVITY_STOP_TIME, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACT_STOP_TIME_ZONE = new ColumnProfile(KEY_ACT_STOP_TIME_ZONE, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_RELATIVE_DEPTH = new ColumnProfile(KEY_ACTIVITY_RELATIVE_DEPTH, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_DEPTH = new ColumnProfile(KEY_ACTIVITY_DEPTH, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_DEPTH_UNIT = new ColumnProfile(KEY_ACTIVITY_DEPTH_UNIT, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_DEPTH_REF_POINT = new ColumnProfile(KEY_ACTIVITY_DEPTH_REF_POINT, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_UPPER_DEPTH = new ColumnProfile(KEY_ACTIVITY_UPPER_DEPTH, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_UPPER_DEPTH_UNIT = new ColumnProfile(KEY_ACTIVITY_UPPER_DEPTH_UNIT, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_LOWER_DEPTH = new ColumnProfile(KEY_ACTIVITY_LOWER_DEPTH, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_LOWER_DEPTH_UNIT = new ColumnProfile(KEY_ACTIVITY_LOWER_DEPTH_UNIT, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile PROJECT_ID = new ColumnProfile(KEY_PROJECT_ID, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_CONDUCTING_ORG = new ColumnProfile(KEY_ACTIVITY_CONDUCTING_ORG, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_COMMENT = new ColumnProfile(KEY_ACTIVITY_COMMENT, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile SAMPLE_AQFR_NAME = new ColumnProfile(KEY_SAMPLE_AQFR_NAME, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile HYDROLOGIC_CONDITION_NAME = new ColumnProfile(KEY_HYDROLOGIC_CONDITION_NAME, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile HYDROLOGIC_EVENT_NAME = new ColumnProfile(KEY_HYDROLOGIC_EVENT_NAME, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_LATITUDE = new ColumnProfile(KEY_ACTIVITY_LATITUDE, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_LONGITUDE = new ColumnProfile(KEY_ACTIVITY_LONGITUDE, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACTIVITY_SOURCE_MAP_SCALE = new ColumnProfile(KEY_ACTIVITY_SOURCE_MAP_SCALE, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_HORIZONTAL_ACCURACY = new ColumnProfile(KEY_ACT_HORIZONTAL_ACCURACY, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_HORIZONTAL_ACCURACY_UNIT = new ColumnProfile(KEY_ACT_HORIZONTAL_ACCURACY_UNIT, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_HORIZONTAL_COLLECT_METHOD = new ColumnProfile(KEY_ACT_HORIZONTAL_COLLECT_METHOD, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_HORIZONTAL_DATUM_NAME = new ColumnProfile(KEY_ACT_HORIZONTAL_DATUM_NAME, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ASSEMBLAGE_SAMPLED_NAME = new ColumnProfile(KEY_ASSEMBLAGE_SAMPLED_NAME, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_COLLECTION_DURATION = new ColumnProfile(KEY_ACT_COLLECTION_DURATION, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_COLLECTION_DURATION_UNIT = new ColumnProfile(KEY_ACT_COLLECTION_DURATION_UNIT, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_SAM_COMPNT_NAME = new ColumnProfile(KEY_ACT_SAM_COMPNT_NAME, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_SAM_COMPNT_PLACE_IN_SERIES = new ColumnProfile(KEY_ACT_SAM_COMPNT_PLACE_IN_SERIES, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_REACH_LENGTH = new ColumnProfile(KEY_ACT_REACH_LENGTH, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_REACH_LENGTH_UNIT = new ColumnProfile(KEY_ACT_REACH_LENGTH_UNIT, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_REACH_WIDTH = new ColumnProfile(KEY_ACT_REACH_WIDTH, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_REACH_WIDTH_UNIT = new ColumnProfile(KEY_ACT_REACH_WIDTH_UNIT, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_PASS_COUNT = new ColumnProfile(KEY_ACT_PASS_COUNT, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile NET_TYPE_NAME = new ColumnProfile(KEY_NET_TYPE_NAME, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_NET_SURFACE_AREA = new ColumnProfile(KEY_ACT_NET_SURFACE_AREA, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_NET_SURFACE_AREA_UNIT = new ColumnProfile(KEY_ACT_NET_SURFACE_AREA_UNIT, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_NET_MESH_SIZE = new ColumnProfile(KEY_ACT_NET_MESH_SIZE, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_NET_MESH_SIZE_UNIT = new ColumnProfile(KEY_ACT_NET_MESH_SIZE_UNIT, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_BOAT_SPEED = new ColumnProfile(KEY_ACT_BOAT_SPEED, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_BOAT_SPEED_UNIT = new ColumnProfile(KEY_ACT_BOAT_SPEED_UNIT, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_CURRENT_SPEED = new ColumnProfile(KEY_ACT_CURRENT_SPEED, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_CURRENT_SPEED_UNIT = new ColumnProfile(KEY_ACT_CURRENT_SPEED_UNIT, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile TOXICITY_TEST_TYPE_NAME = new ColumnProfile(KEY_TOXICITY_TEST_TYPE_NAME, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile SAMPLE_COLLECT_METHOD_ID = new ColumnProfile(KEY_SAMPLE_COLLECT_METHOD_ID, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile SAMPLE_COLLECT_METHOD_CTX = new ColumnProfile(KEY_SAMPLE_COLLECT_METHOD_CTX, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile SAMPLE_COLLECT_METHOD_NAME = new ColumnProfile(KEY_SAMPLE_COLLECT_METHOD_NAME, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACT_SAM_COLLECT_METH_QUAL_TYPE = new ColumnProfile(KEY_ACT_SAM_COLLECT_METH_QUAL_TYPE, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_SAM_COLLECT_METH_DESC = new ColumnProfile(KEY_ACT_SAM_COLLECT_METH_DESC, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile SAMPLE_COLLECT_EQUIP_NAME = new ColumnProfile(KEY_SAMPLE_COLLECT_EQUIP_NAME, Profile.BIOLOGICAL, Profile.PC_RESULT, Profile.ACTIVITY);
	public static final ColumnProfile ACT_SAM_COLLECT_EQUIP_COMMENTS = new ColumnProfile(KEY_ACT_SAM_COLLECT_EQUIP_COMMENTS, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_SAM_PREP_METH_ID = new ColumnProfile(KEY_ACT_SAM_PREP_METH_ID, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_SAM_PREP_METH_CONTEXT = new ColumnProfile(KEY_ACT_SAM_PREP_METH_CONTEXT, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_SAM_PREP_METH_NAME = new ColumnProfile(KEY_ACT_SAM_PREP_METH_NAME, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_SAM_PREP_METH_QUAL_TYPE = new ColumnProfile(KEY_ACT_SAM_PREP_METH_QUAL_TYPE, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_SAM_PREP_METH_DESC = new ColumnProfile(KEY_ACT_SAM_PREP_METH_DESC, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile SAMPLE_CONTAINER_TYPE = new ColumnProfile(KEY_SAMPLE_CONTAINER_TYPE, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile SAMPLE_CONTAINER_COLOR = new ColumnProfile(KEY_SAMPLE_CONTAINER_COLOR, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_SAM_CHEMICAL_PRESERVATIVE = new ColumnProfile(KEY_ACT_SAM_CHEMICAL_PRESERVATIVE, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile THERMAL_PRESERVATIVE_NAME = new ColumnProfile(KEY_THERMAL_PRESERVATIVE_NAME, Profile.BIOLOGICAL, Profile.ACTIVITY);
	public static final ColumnProfile ACT_SAM_TRANSPORT_STORAGE_DESC = new ColumnProfile(KEY_ACT_SAM_TRANSPORT_STORAGE_DESC, Profile.BIOLOGICAL, Profile.ACTIVITY);

	public static final ColumnProfile ACTIVITY_ID = new ColumnProfile(KEY_ACTIVITY_ID, Profile.ACTIVITY);

	private ActivityColumn() {
	}
}
