package gov.usgs.cida.wqp.mapping;

public class StationColumn extends BaseColumn {

	//ResultSet Keys
	public static final String KEY_STATION_NAME = "STATION_NAME";
	public static final String KEY_MONITORING_LOCATION_TYPE = "STATION_TYPE_NAME";
	public static final String KEY_MONITORING_LOCATION_DESCRIPTION = "DESCRIPTION_TEXT";
	public static final String KEY_HUC_8 = "HUC_8";
	public static final String KEY_HUC_12 = "HUC_12";
	public static final String KEY_CONTRIB_DRAIN_AREA_VALUE = "CONTRIB_DRAIN_AREA_VALUE";
	public static final String KEY_CONTRIB_DRAIN_AREA_UNIT = "CONTRIB_DRAIN_AREA_UNIT";
	public static final String KEY_DRAIN_AREA_VALUE = "DRAIN_AREA_VALUE";
	public static final String KEY_DRAIN_AREA_UNIT = "DRAIN_AREA_UNIT";
	public static final String KEY_LATITUDE  = "LATITUDE";
	public static final String KEY_LONGITUDE = "LONGITUDE";
	public static final String KEY_SITE_TYPE = "SITE_TYPE";
	public static final String KEY_SOURCE_MAP_SCALE = "MAP_SCALE";
	public static final String KEY_HORIZONTAL_ACCY_VALUE = "GEOPOSITION_ACCY_VALUE";
	public static final String KEY_HORIZONTAL_ACCY_UNIT = "GEOPOSITION_ACCY_UNIT";
	public static final String KEY_HORIZONTAL_COLLECTION_METHOD = "GEOPOSITIONING_METHOD";
	public static final String KEY_HORIZONTAL_DATUM = "HDATUM_ID_CODE";
	public static final String KEY_VERTICAL_MEASURE_VALUE = "ELEVATION_VALUE";
	public static final String KEY_VERTICAL_MEASURE_UNIT = "ELEVATION_UNIT";
	public static final String KEY_VERTICAL_ACCY_VALUE = "VERTICAL_ACCURACY_VALUE";
	public static final String KEY_VERTICAL_ACCY_UNIT = "VERTICAL_ACCURACY_UNIT";
	public static final String KEY_VERTICAL_COLLECTION_METHOD = "ELEVATION_METHOD";
	public static final String KEY_VERTICAL_DATUM = "VDATUM_ID_CODE";
	public static final String KEY_COUNTRY_CODE = "COUNTRY_CODE";
	public static final String KEY_STATE_CODE = "STATE_FIPS_CODE";
	public static final String KEY_COUNTY_CODE = "COUNTY_FIPS_CODE";
	public static final String KEY_NAT_AQFR_NAME = "NAT_AQFR_NAME";
	public static final String KEY_AQFR_NAME = "AQFR_NAME";
	public static final String KEY_AQFR_TYPE_NAME = "AQFR_TYPE_NAME";
	public static final String KEY_CONSTRUCTION_DATE = "CONSTRUCTION_DATE";
	public static final String KEY_WELL_DEPTH_VALUE = "WELL_DEPTH_VALUE";
	public static final String KEY_WELL_DEPTH_UNIT = "WELL_DEPTH_UNIT";
	public static final String KEY_HOLE_DEPTH_VALUE = "HOLE_DEPTH_VALUE";
	public static final String KEY_HOLE_DEPTH_UNIT = "HOLE_DEPTH_UNIT";
        public static final String KEY_STATE_NAME = "STATE_NAME";
        public static final String KEY_COUNTY_NAME = "COUNTY_NAME";
	public static final String KEY_SUMMARY_PAST_12_MONTHS = "SUMMARY_PAST_12_MONTHS";
	public static final String KEY_SUMMARY_PAST_60_MONTHS = "SUMMARY_PAST_60_MONTHS";
	public static final String KEY_SUMMARY_ALL_MONTHS = "SUMMARY_ALL_MONTHS";

	//Profile Mapping of the Keys
	public static final ColumnProfile STATION_NAME = new ColumnProfile(KEY_STATION_NAME, Profile.STATION, Profile.SIMPLE_STATION);
	public static final ColumnProfile MONITORING_LOCATION_TYPE = new ColumnProfile(KEY_MONITORING_LOCATION_TYPE, Profile.STATION);
	public static final ColumnProfile MONITORING_LOCATION_DESCRIPTION = new ColumnProfile(KEY_MONITORING_LOCATION_DESCRIPTION, Profile.STATION);
	public static final ColumnProfile SITE_TYPE = new ColumnProfile(KEY_SITE_TYPE, Profile.SIMPLE_STATION);
	public static final ColumnProfile HUC_8 = new ColumnProfile(KEY_HUC_8, Profile.STATION);
	public static final ColumnProfile HUC_12 = new ColumnProfile(KEY_HUC_12);
	public static final ColumnProfile DRAIN_AREA_VALUE = new ColumnProfile(KEY_DRAIN_AREA_VALUE, Profile.STATION);
	public static final ColumnProfile DRAIN_AREA_UNIT = new ColumnProfile(KEY_DRAIN_AREA_UNIT, Profile.STATION);
	public static final ColumnProfile CONTRIB_DRAIN_AREA_VALUE = new ColumnProfile(KEY_CONTRIB_DRAIN_AREA_VALUE, Profile.STATION);
	public static final ColumnProfile CONTRIB_DRAIN_AREA_UNIT = new ColumnProfile(KEY_CONTRIB_DRAIN_AREA_UNIT, Profile.STATION);
	public static final ColumnProfile LATITUDE = new ColumnProfile(KEY_LATITUDE, Profile.STATION, Profile.SIMPLE_STATION);
	public static final ColumnProfile LONGITUDE = new ColumnProfile(KEY_LONGITUDE, Profile.STATION, Profile.SIMPLE_STATION);
	public static final ColumnProfile SOURCE_MAP_SCALE = new ColumnProfile(KEY_SOURCE_MAP_SCALE, Profile.STATION);
	public static final ColumnProfile HORIZONTAL_ACCY_VALUE = new ColumnProfile(KEY_HORIZONTAL_ACCY_VALUE, Profile.STATION);
	public static final ColumnProfile HORIZONTAL_ACCY_UNIT = new ColumnProfile(KEY_HORIZONTAL_ACCY_UNIT, Profile.STATION);
	public static final ColumnProfile HORIZONTAL_COLLECTION_METHOD = new ColumnProfile(KEY_HORIZONTAL_COLLECTION_METHOD, Profile.STATION);
	public static final ColumnProfile HORIZONTAL_DATUM = new ColumnProfile(KEY_HORIZONTAL_DATUM, Profile.STATION);
	public static final ColumnProfile VERTICAL_MEASURE_VALUE = new ColumnProfile(KEY_VERTICAL_MEASURE_VALUE, Profile.STATION);
	public static final ColumnProfile VERTICAL_MEASURE_UNIT = new ColumnProfile(KEY_VERTICAL_MEASURE_UNIT, Profile.STATION);
	public static final ColumnProfile VERTICAL_ACCY_VALUE = new ColumnProfile(KEY_VERTICAL_ACCY_VALUE, Profile.STATION);
	public static final ColumnProfile VERTICAL_ACCY_UNIT = new ColumnProfile(KEY_VERTICAL_ACCY_UNIT, Profile.STATION);
	public static final ColumnProfile VERTICAL_COLLECTION_METHOD = new ColumnProfile(KEY_VERTICAL_COLLECTION_METHOD, Profile.STATION);
	public static final ColumnProfile VERTICAL_DATUM = new ColumnProfile(KEY_VERTICAL_DATUM, Profile.STATION);
	public static final ColumnProfile COUNTRY_CODE = new ColumnProfile(KEY_COUNTRY_CODE, Profile.STATION);
	public static final ColumnProfile STATE_CODE = new ColumnProfile(KEY_STATE_CODE, Profile.STATION);
	public static final ColumnProfile COUNTY_CODE = new ColumnProfile(KEY_COUNTY_CODE, Profile.STATION);
	public static final ColumnProfile NAT_AQFR_NAME = new ColumnProfile(KEY_NAT_AQFR_NAME, Profile.STATION);
	public static final ColumnProfile AQFR_NAME = new ColumnProfile(KEY_AQFR_NAME, Profile.STATION);
	public static final ColumnProfile AQFR_TYPE_NAME = new ColumnProfile(KEY_AQFR_TYPE_NAME, Profile.STATION);
	public static final ColumnProfile CONSTRUCTION_DATE = new ColumnProfile(KEY_CONSTRUCTION_DATE, Profile.STATION);
	public static final ColumnProfile WELL_DEPTH_VALUE = new ColumnProfile(KEY_WELL_DEPTH_VALUE, Profile.STATION);
	public static final ColumnProfile WELL_DEPTH_UNIT = new ColumnProfile(KEY_WELL_DEPTH_UNIT, Profile.STATION);
	public static final ColumnProfile HOLE_DEPTH_VALUE = new ColumnProfile(KEY_HOLE_DEPTH_VALUE, Profile.STATION);
	public static final ColumnProfile HOLE_DEPTH_UNIT = new ColumnProfile(KEY_HOLE_DEPTH_UNIT, Profile.STATION);
        public static final ColumnProfile STATE_NAME = new ColumnProfile(KEY_STATE_NAME, Profile.SIMPLE_STATION);
        public static final ColumnProfile COUNTY_NAME = new ColumnProfile(KEY_COUNTY_NAME, Profile.SIMPLE_STATION);
	public static final ColumnProfile SUMMARY_PAST_12_MONTHS = new ColumnProfile(KEY_SUMMARY_PAST_12_MONTHS, Profile.SUMMARY_STATION);
	public static final ColumnProfile SUMMARY_PAST_60_MONTHS = new ColumnProfile(KEY_SUMMARY_PAST_60_MONTHS, Profile.SUMMARY_STATION);
	public static final ColumnProfile SUMMARY_ALL_MONTHS = new ColumnProfile(KEY_SUMMARY_ALL_MONTHS, Profile.SUMMARY_STATION);
		
	private StationColumn() {
	}
}
