package gov.usgs.cida.wqp.transform;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.usgs.cida.wqp.mapping.ActivityColumn;
import gov.usgs.cida.wqp.mapping.ColumnProfile;
import gov.usgs.cida.wqp.mapping.xml.IXmlMapping;
import gov.usgs.cida.wqp.service.ILogService;
import gov.usgs.cida.wqp.util.HttpConstants;

public class MapToXmlTransformer extends Transformer {

	private static final Logger LOG = LoggerFactory.getLogger(MapToXmlTransformer.class);

	protected Deque<String> nodes = new LinkedList<>();

	protected IXmlMapping fieldMapping;
	protected Map<String, String> hardBreaks;

	protected final String header;
	protected final String profile;

	private final String siteUrlBase;

	public MapToXmlTransformer(OutputStream target, IXmlMapping mapping, ILogService logService, BigDecimal logId, String profile, String siteUrlBase) {
		super(target, null, logService, logId);
		fieldMapping = mapping;
		this.siteUrlBase = siteUrlBase;
		this.header = XmlConstants.XML_HEADER + fieldMapping.getHeader();
		hardBreaks = new LinkedHashMap<>();
		for (String key : fieldMapping.getHardBreak().keySet()) {
			hardBreaks.put(key, null);
		}
		this.profile = profile;
		init();
	}

	@Override
	protected void init() {
		writeHeader();
	}

	/** Output the file header. */
	protected void writeHeader() {
		writeToStream(header);
		nodes.push(fieldMapping.getRoot());
	}

	/**
	 * Extract the data and write to the stream.
	 */
	protected void writeData(Map<String, Object> resultMap) {
		//TODO write bytes as we get them, rather than a "record" at a time
		StringBuilder sb = new StringBuilder();

		for (String key : fieldMapping.getHardBreak().keySet()) {
			Object value = resultMap.get(key);
			if (value != null) {
				String encode = encode(value.toString());
				if ( ! encode.equalsIgnoreCase(hardBreaks.get(key)) ) {
					if (nodes.contains(fieldMapping.getHardBreak().get(key))) {
						closeNodes(sb, fieldMapping.getHardBreak().get(key));
					}
					doGrouping(resultMap, sb, key);
					hardBreaks.put(key, encode);
				}
			}
		}
		writeToStream(sb.toString());
	}

	protected void doGrouping(Map<String, Object> map, StringBuilder sb, String key) {
		List<ColumnProfile> columnProfiles = fieldMapping.getGrouping().get(key);
		Iterator<ColumnProfile> i = columnProfiles.iterator();
		while (i.hasNext()) {
			ColumnProfile columnProfile = i.next();
			String col = columnProfile.getKey();
			Object obj = map.get(col);
			if (!columnProfile.getProfiles().contains(profile) || obj==null) {
				continue;
			}
			String val = encode(obj.toString());
			doNode(sb, col, val);
		}
	}

	protected void doNode(StringBuilder sb, String key, String val) {
		String lNode = fieldMapping.getEntryNodeName();
		List<String> pos = fieldMapping.getStructure().get(key);
		for (String node : pos) {
			LOG.trace("node - positionNode:" + node + " lastNode:" + lNode);
			if ( ! nodes.contains(node) ) {
				if ( ! lNode.equalsIgnoreCase(nodes.peek()) ) {
					closeNodes(sb, lNode);
				}
				sb.append("<").append(node).append(">");
				nodes.push(node);
			}
			lNode = node;
		}
		if (key.equals(ActivityColumn.KEY_ACTIVITY_ID)) {
			val = siteUrlBase + HttpConstants.ACTIVITY_METRIC_REST_ENPOINT.replace("{activity}", val);
		}
		sb.append(val);
		sb.append(getClosingNodeText(nodes.pop()));
	}

	protected void closeNodes(StringBuilder sb, String targetNode) {
		LOG.trace("closeNodes - targetNode: " + targetNode);

		while (!nodes.isEmpty() && !fieldMapping.getRoot().equalsIgnoreCase(nodes.peek())) {
			String node = nodes.peek();
			LOG.trace("closeNodes - currentNode: " + node);
			if (targetNode.equalsIgnoreCase(node)) {
				break;
			} else {
				sb.append(getClosingNodeText(node));
				nodes.pop();
			}
		}

	}

	/** output the closing tag. */
	@Override
	public void end() {
		while (!nodes.isEmpty()) {
			writeToStream(getClosingNodeText(nodes.element()));
			nodes.pop();
		}
		super.end();
	}

	@Override
	public String encode(String value) {
		return StringEscapeUtils.escapeXml10(value);
	}

	protected String getClosingNodeText(String rawText) {
		if (StringUtils.isBlank(rawText)) {
			return "";
		} else {
			StringBuilder closingNode = new StringBuilder("</");
			if (rawText.trim().contains(" ")) {
				closingNode.append(rawText.trim().substring(0, rawText.trim().indexOf(" ")));
			} else {
				closingNode.append(rawText.trim());
			}
			closingNode.append(">");
			return closingNode.toString();
		}
	}

}
