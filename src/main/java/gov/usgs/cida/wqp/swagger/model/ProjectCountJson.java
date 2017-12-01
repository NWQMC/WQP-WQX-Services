package gov.usgs.cida.wqp.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import gov.usgs.cida.wqp.util.HttpConstants;

public class ProjectCountJson {
	
	public static final String HEADER_NWIS_PROJECT_COUNT = StationCountJson.NWIS + HttpConstants.HEADER_DELIMITER + HttpConstants.HEADER_PROJECT_COUNT;
	public static final String HEADER_STEWARDS_PROJECT_COUNT = StationCountJson.STEWARDS + HttpConstants.HEADER_DELIMITER + HttpConstants.HEADER_PROJECT_COUNT;
	public static final String HEADER_STORET_PROJECT_COUNT = StationCountJson.STORET + HttpConstants.HEADER_DELIMITER + HttpConstants.HEADER_PROJECT_COUNT;
	public static final String HEADER_BIODATA_PROJECT_COUNT = StationCountJson.BIODATA + HttpConstants.HEADER_DELIMITER + HttpConstants.HEADER_PROJECT_COUNT;
	
	@JsonProperty(HttpConstants.HEADER_TOTAL_PROJECT_COUNT)
	String totalProjectCount;
	@JsonProperty(HEADER_NWIS_PROJECT_COUNT)
	String nwisProjectCount;
	@JsonProperty(HEADER_STEWARDS_PROJECT_COUNT)
	String stewardsProjectCount;
	@JsonProperty(HEADER_STORET_PROJECT_COUNT)
	String storetProjectCount;
	@JsonProperty(HEADER_BIODATA_PROJECT_COUNT)
	String biodataProjectCount;
	
}
