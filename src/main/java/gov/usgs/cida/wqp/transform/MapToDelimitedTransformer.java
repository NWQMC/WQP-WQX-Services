package gov.usgs.cida.wqp.transform;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import gov.usgs.cida.wqp.mapping.ActivityColumn;
import gov.usgs.cida.wqp.service.ILogService;
import gov.usgs.cida.wqp.util.HttpConstants;

public class MapToDelimitedTransformer extends Transformer {

	protected static final String LF = StringUtils.LF;
	protected static final String CR = StringUtils.CR;
	public static final String TAB = "\t";
	public static final String COMMA = ",";
	protected static final String SPACE = " ";

	protected final String delimiter;
	private final String siteUrlBase;

	public MapToDelimitedTransformer(OutputStream target, Map<String, String> mapping, ILogService logService, BigDecimal logId, String delimiter, String siteUrlBase) {
		super(target, mapping, logService, logId);
		this.delimiter = delimiter;
		this.siteUrlBase = siteUrlBase;
		init();
	}

	protected void init() {
		writeHeader();
	}

	protected void writeHeader() {
		boolean firstPass = true;
		for (Entry<?, ?> entry: mapping.entrySet()) {
			String value = getMappedName(entry);
			if (firstPass) {
				firstPass = false;
			} else {
				writeToStream(delimiter);
			}
			writeToStream(encode(value));
		}
	}

	protected void writeData(Map<String, Object> result) {
		writeToStream(LF);
		boolean firstPass = true;
		for (String col: mapping.keySet()) {
			if (firstPass) {
				firstPass = false;
			} else {
				writeToStream(delimiter);
			}
			Object value = result.get(col);
			if (null != value) {
				if (col.equals(ActivityColumn.KEY_ACTIVITY_ID)) {
					value = siteUrlBase + HttpConstants.ACTIVITY_METRIC_REST_ENPOINT.replace("{activity}", value.toString());
					System.out.println(value.toString());
				}
				writeToStream(encode(value.toString()));
			}
		}
	}

	public String encode(String value) {
		if (delimiter.contentEquals(TAB)) {
			return value.replace(LF, SPACE).replace(CR, SPACE).replace(TAB, SPACE);
		} else {
			return StringEscapeUtils.escapeCsv(value);
		}
	}

	public String getDelimiter() {
		return delimiter;
	}
}
