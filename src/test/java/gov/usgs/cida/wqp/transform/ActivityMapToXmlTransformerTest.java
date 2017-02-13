package gov.usgs.cida.wqp.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import gov.usgs.cida.wqp.mapping.ActivityColumn;
import gov.usgs.cida.wqp.mapping.BaseColumn;
import gov.usgs.cida.wqp.mapping.Profile;
import gov.usgs.cida.wqp.mapping.xml.ActivityWqx;
import gov.usgs.cida.wqp.mapping.xml.IXmlMapping;
import gov.usgs.cida.wqp.service.ILogService;
import gov.usgs.cida.wqp.util.HttpConstants;

public class ActivityMapToXmlTransformerTest {
	
	private static final String ORG_OPEN = "<Organization><OrganizationDescription><OrganizationIdentifier>";
	private static final String ORG_CLOSE = "</OrganizationIdentifier></OrganizationDescription>";
	private static final String ACT_OPEN = "<Activity>";
	private static final String ACT_IDEN_OPEN = "<ActivityDescription><ActivityIdentifier>";
	private static final String ACT_IDEN_CLOSE = "</ActivityIdentifier></ActivityDescription>";
	private static final String ACT_MET_URL_OPEN = "<ActivityMetricURL>";
	private static final String ACT_MET_URL_CLOSE = "</ActivityMetricURL>";

	@Mock
	protected ILogService logService;
	protected BigDecimal logId = new BigDecimal(1);
	protected IXmlMapping fieldMapping = new ActivityWqx();
	protected MapToXmlTransformer transformer;
	protected ByteArrayOutputStream baos;

	@Before
	public void initTest() {
		MockitoAnnotations.initMocks(this);
		baos = new ByteArrayOutputStream();
		transformer = new MapToXmlTransformer(baos, fieldMapping, logService, logId, Profile.ACTIVITY, "ROOTURL");
	}

	@After
	public void closeTest() throws IOException {
		transformer.close();
	}

	@Test
	public void writeDataTest() {
		String orgName = "I am org";
		String actIden = "I am activity";
		String actMetURL = "ROOTURL/activities/1234/activitymetrics";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put(BaseColumn.KEY_ORGANIZATION, orgName);
			map.put(ActivityColumn.KEY_ACTIVITY, actIden);
			map.put(ActivityColumn.KEY_ACTIVITY_ID, 1234);
			transformer.writeData(map);
			map.put(BaseColumn.KEY_ORGANIZATION, null);
			transformer.writeData(map);
			assertEquals("Data size mismatch,", transformer.header.length() + 307, baos.size());
			StringBuilder expected = new StringBuilder(transformer.header);
			expected.append(ORG_OPEN + orgName + ORG_CLOSE);
			expected.append(ACT_OPEN);
			expected.append(ACT_IDEN_OPEN +actIden + ACT_IDEN_CLOSE);
			expected.append(ACT_MET_URL_OPEN +actMetURL + ACT_MET_URL_CLOSE);
			assertEquals("Data text mismatch,", expected.toString(),
					new String(baos.toByteArray(), HttpConstants.DEFAULT_ENCODING));
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}
}
