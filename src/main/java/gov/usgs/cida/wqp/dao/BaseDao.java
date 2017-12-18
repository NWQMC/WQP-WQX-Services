package gov.usgs.cida.wqp.dao;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseDao extends SqlSessionDaoSupport {
	private static final Logger LOG = LoggerFactory.getLogger(BaseDao.class);

	public static final String STATION_NAMESPACE  = "station";
	public static final String SIMPLE_STATION_NAMESPACE = "simpleStation";
	public static final String LOG_NAMESPACE = "logMapper";
	public static final String RESULT_NAMESPACE = "pcResult";
	public static final String BIOLOGICAL_RESULT_NAMESPACE = "bioResult";
	public static final String STATION_KML_NAMESPACE  = "stationKml";
	public static final String ACTIVITY_NAMESPACE = "activity";
	public static final String ACTIVITY_METRIC_NAMESPACE = "activityMetric";
	public static final String RES_DETECT_QNT_LMT_NAMESPACE = "resDetectQntLmt";
	public static final String NARROW_RESULT_NAMESPACE = "narrowResult";
	public static final String PROJECT_NAMESPACE = "project";

	public BaseDao(SqlSessionFactory sqlSessionFactory) {
		LOG.trace(getClass().getName());
		setSqlSessionFactory(sqlSessionFactory);
	}

}
