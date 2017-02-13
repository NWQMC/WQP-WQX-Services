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

import gov.usgs.cida.wqp.mapping.BaseColumn;
import gov.usgs.cida.wqp.mapping.Profile;
import gov.usgs.cida.wqp.mapping.StationColumn;
import gov.usgs.cida.wqp.mapping.xml.BaseWqx;
import gov.usgs.cida.wqp.mapping.xml.IXmlMapping;
import gov.usgs.cida.wqp.mapping.xml.StationWqx;
import gov.usgs.cida.wqp.service.ILogService;
import gov.usgs.cida.wqp.util.HttpConstants;

public class MapToXmlTransformerTest {

	@Mock
	protected ILogService logService;
	protected BigDecimal logId = new BigDecimal(1);
	protected IXmlMapping fieldMapping = new StationWqx();
	protected MapToXmlTransformer transformer;
	protected ByteArrayOutputStream baos;

	@Before
	public void initTest() {
		MockitoAnnotations.initMocks(this);
		baos = new ByteArrayOutputStream();
		transformer = new MapToXmlTransformer(baos, fieldMapping, logService, logId, Profile.STATION, "");
	}

	@After
	public void closeTest() throws IOException {
		transformer.close();
	}

	@Test
	public void writeHeaderTest() {
		try {
			assertEquals(transformer.header.length(), baos.size());
			assertEquals(transformer.header,
					new String(baos.toByteArray(), HttpConstants.DEFAULT_ENCODING));
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void encodeTest() {
		assertEquals("abc", transformer.encode("abc"));
		assertEquals("&lt;dae&gt;", transformer.encode("<dae>"));
	}

	@Test
	public void closeNodesTest() {
		StringBuilder sb = new StringBuilder();
		transformer.closeNodes(sb, "xyz");
		assertEquals("", sb.toString());

		transformer.nodes.push(fieldMapping.getRoot());
		transformer.closeNodes(sb, "xyz");
		assertEquals("", sb.toString());

		transformer.nodes.push("xyz");
		transformer.nodes.push("abc");
		transformer.nodes.push("qed");
		transformer.closeNodes(sb, "qed");
		assertEquals("", sb.toString());

		transformer.closeNodes(sb, "xyz");
		assertEquals("</qed></abc>", sb.toString());
	}

	@Test
	public void doNodeTest() {
		StringBuilder sb = new StringBuilder();
		transformer.nodes.push(BaseWqx.WQX_PROVIDER);
		transformer.nodes.push(BaseWqx.WQX_ORGANIZATION);
		transformer.nodes.push(BaseWqx.WQX_MONITORING_LOCATION);
		transformer.nodes.push(BaseWqx.WQX_MONITORING_LOCATION_IDENTITY);
		transformer.doNode(sb, StationColumn.KEY_LONGITUDE, "23jkh4kl213");
		assertEquals("</MonitoringLocationIdentity><MonitoringLocationGeospatial><LongitudeMeasure>23jkh4kl213</LongitudeMeasure>",
				sb.toString());
	}

	@Test
	public void doGroupingTest() {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		map.put(StationColumn.KEY_LONGITUDE, "23jk<h4kl213");
		map.put(StationColumn.KEY_STATION_NAME, "DG-10/13E/19-0891");
		transformer.doGrouping(map, sb, StationColumn.KEY_SITE_ID);
		assertEquals("<Organization><MonitoringLocation><MonitoringLocationIdentity><MonitoringLocationName>DG-10/13E/19-0891</MonitoringLocationName></MonitoringLocationIdentity><MonitoringLocationGeospatial><LongitudeMeasure>23jk&lt;h4kl213</LongitudeMeasure>",
				sb.toString());
	}

	@Test
	public void doGroupingSimpleStationTest() throws IOException {
		MapToXmlTransformer bt = new MapToXmlTransformer(baos, fieldMapping, logService, logId, Profile.SIMPLE_STATION, "");
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		map.put(StationColumn.KEY_LONGITUDE, "23jk<h4kl213");
		map.put(StationColumn.KEY_CONTRIB_DRAIN_AREA_UNIT, "ft/lbs");
		map.put(StationColumn.KEY_STATION_NAME, "DG-10/13E/19-0891");
		bt.doGrouping(map, sb, StationColumn.KEY_SITE_ID);
		assertEquals("<Organization><MonitoringLocation><MonitoringLocationIdentity><MonitoringLocationName>DG-10/13E/19-0891</MonitoringLocationName></MonitoringLocationIdentity><MonitoringLocationGeospatial><LongitudeMeasure>23jk&lt;h4kl213</LongitudeMeasure>",
				sb.toString());
		bt.close();
	}

	@Test
	public void writeDataTest() {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put(BaseColumn.KEY_ORGANIZATION, "d");
			map.put(StationColumn.KEY_SITE_ID, 1);
			map.put(StationColumn.KEY_LONGITUDE, "23jk<h4kl213");
			map.put(StationColumn.KEY_STATION_NAME, "DG-10/13E/19-0891");
			transformer.writeData(map);
			map = new HashMap<>();
			map.put(BaseColumn.KEY_ORGANIZATION, "d");
			map.put(StationColumn.KEY_SITE_ID, 2);
			map.put(StationColumn.KEY_LONGITUDE, "2");
			map.put(StationColumn.KEY_STATION_NAME, "two");
			transformer.writeData(map);
			map = new HashMap<>();
			map.put(BaseColumn.KEY_ORGANIZATION, null);
			transformer.writeData(map);
			assertEquals(transformer.header.length() + 713, baos.size());
			assertEquals(transformer.header
					+ "<Organization><OrganizationDescription><OrganizationIdentifier>d</OrganizationIdentifier></OrganizationDescription><MonitoringLocation><MonitoringLocationIdentity><MonitoringLocationIdentifier>1</MonitoringLocationIdentifier><MonitoringLocationName>DG-10/13E/19-0891</MonitoringLocationName></MonitoringLocationIdentity><MonitoringLocationGeospatial><LongitudeMeasure>23jk&lt;h4kl213</LongitudeMeasure></MonitoringLocationGeospatial></MonitoringLocation><MonitoringLocation><MonitoringLocationIdentity><MonitoringLocationIdentifier>2</MonitoringLocationIdentifier><MonitoringLocationName>two</MonitoringLocationName></MonitoringLocationIdentity><MonitoringLocationGeospatial><LongitudeMeasure>2</LongitudeMeasure>",
					new String(baos.toByteArray(), HttpConstants.DEFAULT_ENCODING));
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void endTest() {
		try {
			transformer.nodes.push(BaseWqx.WQX_PROVIDER);
			transformer.nodes.push(BaseWqx.WQX_ORGANIZATION);
			transformer.end();
			assertEquals(transformer.header.length() + 32, baos.size());
			assertEquals(transformer.header + "</Organization></Provider></WQX>",
					new String(baos.toByteArray(), HttpConstants.DEFAULT_ENCODING));
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void getClosingNodeTextTest() {
		assertEquals("", transformer.getClosingNodeText(null));
		assertEquals("", transformer.getClosingNodeText(""));
		assertEquals("", transformer.getClosingNodeText(" "));

		assertEquals("</a>", transformer.getClosingNodeText("a"));
		assertEquals("</a>", transformer.getClosingNodeText(" a "));
		assertEquals("</a>", transformer.getClosingNodeText("a b"));
		assertEquals("</a>", transformer.getClosingNodeText("a b c"));
		assertEquals("</a>", transformer.getClosingNodeText(" a b c d "));
	}
}
