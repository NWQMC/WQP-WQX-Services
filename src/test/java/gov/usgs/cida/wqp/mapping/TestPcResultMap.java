package gov.usgs.cida.wqp.mapping;

import static gov.usgs.cida.wqp.mapping.ActivityColumn.*;
import static gov.usgs.cida.wqp.mapping.ResultColumn.*;
import static gov.usgs.cida.wqp.mapping.StationColumn.*;
import static gov.usgs.cida.wqp.swagger.model.ResultCountJson.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestPcResultMap {

	public static final Map<String, Object> PC_RESULT;
	static {
		PC_RESULT = new LinkedHashMap<String, Object>();
		PC_RESULT.put(KEY_DATA_SOURCE_ID, BigDecimal.valueOf(3));
		PC_RESULT.put(KEY_DATA_SOURCE, STORET);
		PC_RESULT.put(KEY_STATION_ID, BigDecimal.valueOf(888));
		PC_RESULT.put(KEY_SITE_ID, "organization-siteId");
		PC_RESULT.put(KEY_EVENT_DATE, "1999-12-28");
		PC_RESULT.put(KEY_ACTIVITY, "activity");
		PC_RESULT.put(KEY_SAMPLE_MEDIA, "sampleMedia");
		PC_RESULT.put(KEY_ORGANIZATION, "organization");
		PC_RESULT.put(KEY_SITE_TYPE, "siteType");
		PC_RESULT.put(KEY_HUC, "0000");
		PC_RESULT.put(KEY_GOVERNMENTAL_UNIT_CODE, "XX:44:555");
		PC_RESULT.put(KEY_ORGANIZATION_NAME, "organizationName");
		PC_RESULT.put(KEY_ACTIVITY_ID, BigDecimal.valueOf(11));
		PC_RESULT.put(KEY_ACTIVITY_TYPE_CODE, "activityTypeCode");
		PC_RESULT.put(KEY_ACTIVITY_MEDIA_SUBDIV_NAME, "activityMediaSubdivName");
		PC_RESULT.put(KEY_ACTIVITY_START_TIME, "activityStartTime");
		PC_RESULT.put(KEY_ACT_START_TIME_ZONE, "actStartTimeZone");
		PC_RESULT.put(KEY_ACTIVITY_STOP_DATE, "activityStopDate");
		PC_RESULT.put(KEY_ACTIVITY_STOP_TIME, "activityStopTime");
		PC_RESULT.put(KEY_ACT_STOP_TIME_ZONE, "actStopTimeZone");
		PC_RESULT.put(KEY_ACTIVITY_DEPTH, "activityDepth");
		PC_RESULT.put(KEY_ACTIVITY_DEPTH_UNIT, "activityDepthUnit");
		PC_RESULT.put(KEY_ACTIVITY_DEPTH_REF_POINT, "activityDepthRefPoint");
		PC_RESULT.put(KEY_ACTIVITY_UPPER_DEPTH, "activityUpperDepth");
		PC_RESULT.put(KEY_ACTIVITY_UPPER_DEPTH_UNIT, "activityUpperDepthUnit");
		PC_RESULT.put(KEY_ACTIVITY_LOWER_DEPTH, "activityLowerDepth");
		PC_RESULT.put(KEY_ACTIVITY_LOWER_DEPTH_UNIT, "activityLowerDepthUnit");
		PC_RESULT.put(KEY_ACTIVITY_PROJECTS, "projectId");
		PC_RESULT.put(KEY_ACTIVITY_CONDUCTING_ORG, "activityConductingOrg");
		PC_RESULT.put(KEY_ACTIVITY_COMMENT, "activityComment");
		PC_RESULT.put(KEY_SAMPLE_AQFR_NAME, "sampleAqfrName");
		PC_RESULT.put(KEY_HYDROLOGIC_CONDITION_NAME, "hydrologicConditionName");
		PC_RESULT.put(KEY_HYDROLOGIC_EVENT_NAME, "hydrologicEventName");
		PC_RESULT.put(KEY_SAMPLE_COLLECT_METHOD_ID, "sampleCollectMethodId");
		PC_RESULT.put(KEY_SAMPLE_COLLECT_METHOD_CTX, "sampleCollectMethodCtx");
		PC_RESULT.put(KEY_SAMPLE_COLLECT_METHOD_NAME, "sampleCollectMethodName");
		PC_RESULT.put(KEY_SAMPLE_COLLECT_EQUIP_NAME, "sampleCollectEquipName");

		PC_RESULT.put(KEY_RESULT_ID, BigDecimal.valueOf(42));
		PC_RESULT.put(KEY_RESULT_DETECTION_CONDITION_TX, "resultDetectionConditionTx");
		PC_RESULT.put(KEY_CHARACTERISTIC_NAME, "characteristicName");
		PC_RESULT.put(KEY_CHARACTERISTIC_TYPE, "characteristicType");
		PC_RESULT.put(KEY_SAMPLE_FRACTION_TYPE, "sampleFractionType");
		PC_RESULT.put(KEY_RESULT_MEASURE_VALUE, "resultMeasureValue");
		PC_RESULT.put(KEY_RESULT_UNIT, "resultUnit");
		PC_RESULT.put(KEY_RESULT_MEAS_QUAL_CODE, "resultMeasQualCode");
		PC_RESULT.put(KEY_RESULT_VALUE_STATUS, "resultValueStatus");
		PC_RESULT.put(KEY_STATISTIC_TYPE, "statisticType");
		PC_RESULT.put(KEY_RESULT_VALUE_TYPE, "resultValueType");
		PC_RESULT.put(KEY_WEIGHT_BASIS_TYPE, "weightBasisType");
		PC_RESULT.put(KEY_DURATION_BASIS, "durationBasis");
		PC_RESULT.put(KEY_TEMPERATURE_BASIS_LEVEL, "temperatureBasisLevel");
		PC_RESULT.put(KEY_PARTICLE_SIZE, "particleSize");
		PC_RESULT.put(KEY_PRECISION, "precision");
		PC_RESULT.put(KEY_RESULT_COMMENT, "resultComment");
		PC_RESULT.put(KEY_P_CODE, "99999");
		PC_RESULT.put(KEY_RESULT_DEPTH_MEAS_VALUE, "resultDepthMeasValue");
		PC_RESULT.put(KEY_RESULT_DEPTH_MEAS_UNIT_CODE, "resultDepthMeasUnitCode");
		PC_RESULT.put(KEY_RESULT_DEPTH_ALT_REF_PT_TXT, "resultDepthAltRefPtTxt");
		PC_RESULT.put(KEY_SAMPLE_TISSUE_TAXONOMIC_NAME, "sampleTissueTaxonomicName");
		PC_RESULT.put(KEY_SAMPLE_TISSUE_ANATOMY_NAME, "sampleTissueAnatomyName");
		PC_RESULT.put(KEY_ANALYTICAL_PROCEDURE_ID, "analyticalProcedureId");
		PC_RESULT.put(KEY_ANALYTICAL_PROCEDURE_SOURCE, "analyticalProcedureSource");
		PC_RESULT.put(KEY_ANALYTICAL_METHOD_NAME, "analyticalMethodName");
		PC_RESULT.put(KEY_ANALYTICAL_METHOD_CITATION, "analyticalMethodCitation");
		PC_RESULT.put(KEY_LAB_NAME, "labName");
		PC_RESULT.put(KEY_ANALYSIS_START_DATE, "analysisStartDate");
		PC_RESULT.put(KEY_LAB_REMARK, "labRemark");
		PC_RESULT.put(KEY_DETECTION_LIMIT_DESC, "detectionLimitDesc");
		PC_RESULT.put(KEY_DETECTION_LIMIT, "detectionLimit");
		PC_RESULT.put(KEY_DETECTION_LIMIT_UNIT, "detectionLimitUnit");
		PC_RESULT.put(KEY_ANALYSIS_PREP_DATE_TX, "analysisPrepDateTx");
		}

	private TestPcResultMap() {
	}
}
