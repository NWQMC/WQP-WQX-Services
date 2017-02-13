package gov.usgs.cida.wqp.transform;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import gov.usgs.cida.wqp.mapping.ActivityColumn;
import gov.usgs.cida.wqp.service.ILogService;
import gov.usgs.cida.wqp.util.HttpConstants;

public class MapToXlsxTransformer extends Transformer {

	/** The work book to which we will add a sheet for this export. */
	private XSSFWorkbook workbook = null;

	/** The sheet that we are working on. */
	private XSSFSheet sheet = null;

	/** Within the workbook, the name of the spreadsheet tab. */
	private String sheetName = "report";

	/** Name of the zip entry holding sheet data, e.g. /xl/worksheets/report.xml */
	private String sheetRef = "";

	/** Keep track of the row we are writing. 0-based. */
	private int rowCount = 0;

	/** Zip output stream for the spreadsheet. */
	private ZipOutputStream zos;
	
	/** Default output buffer size. */
	private static final int DEFAULT_BUFFER_SIZE = 1024;

	private final String siteUrlBase;

	public MapToXlsxTransformer(OutputStream target, Map<String, String> mapping, ILogService logService, BigDecimal logId, String siteUrlBase) {
		super(target, mapping, logService, logId);
		this.siteUrlBase = siteUrlBase;
		init();
	}

	/**
	 * Initialize a workbook if needed, a sheet to work on and write the
	 * template to the stream.
	 */
	@Override
	protected void init() {
		if (null == workbook) {
			workbook = new XSSFWorkbook();
		}

		if (null == sheet) {
			sheet = workbook.createSheet(sheetName);
		}

		sheetRef = sheet.getPackagePart().getPartName().getName().substring(1);
		zos = new ZipOutputStream(target);

		// stream the template - don't include the empty sheet (sheetRef).
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			workbook.write(os);
			ZipInputStream strm = new ZipInputStream(new ByteArrayInputStream(os.toByteArray()));
			ZipEntry ze;
			while (null != (ze = strm.getNextEntry())) {
				if (!ze.getName().equals(sheetRef)) {
					zos.putNextEntry(new ZipEntry(ze.getName()));
					copyStream(strm);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Error starting spreadsheet", e);
		}
		writeHeader();
	}

	/** 
	 * Writes a byte array to the stream. 
	 * @param in the byte array to stream.
	 */
	private void copyStream(final InputStream in) {
		try {
			byte[] chunk = new byte[DEFAULT_BUFFER_SIZE];
			int count;
			while ((count = in.read(chunk)) >= 0) {
				zos.write(chunk, 0, count);
			}
		} catch (IOException e) {
			throw new RuntimeException("Error copying stream", e);
		}
	}

	/**
	 * Write the header.
	 */
	protected void writeHeader() {
		try {
			zos.putNextEntry(new ZipEntry(sheetRef));
			beginSheet();
			insertRow();
			int cellCount = 0;
			for (Entry<?, ?> entry: mapping.entrySet()) {
				createCell(cellCount, getMappedName(entry));
				cellCount++;
			}
			endRow();
		} catch (IOException e) {
			throw new RuntimeException("Error writing header", e);
		}
	}

	/**
	 * Write the data.  Null cells are skipped to cut some of the bloat out of the file.
	 */
	protected void writeData(Map<String, Object> result) {
		insertRow();
		int cellCount = 0;
		for (String column : mapping.keySet()) {
			if (result.containsKey(column) && null != result.get(column) ) {
				Object obj = result.get(column);
				if (obj instanceof BigDecimal) {
					createCell(cellCount, ((BigDecimal) obj).doubleValue());
				} else if (column.equals(ActivityColumn.KEY_ACTIVITY_ID)) {
					String value = siteUrlBase + HttpConstants.ACTIVITY_METRIC_REST_ENPOINT.replace("{activity}", obj.toString());
					createCell(cellCount, value);
				} else {
					createCell(cellCount, obj.toString());
				}
			}

			cellCount++;
		}
		endRow();
	}

	/**
	 * Once we are done with the filter, the sheet needs it's ending tag.
	 */
	@Override
	public void end() {
		try {
			//zos will be null if we did not process any data - in which case we do not need to close the sheet or zos.
			if (null != zos) {
				endSheet();
				zos.finish();
			}
			super.end();
		} catch (IOException e) {
			throw new RuntimeException("Error ending spreadsheet", e);
		}
	}

	/** 
	 * Converts a string to a byte array and stream it.
	 * @param in the string to be streamed.
	 */
	@Override
	protected void writeToStream(final String in){
		try {
			if (null != in) {
				zos.write(in.getBytes(HttpConstants.DEFAULT_ENCODING));
			}
		} catch (IOException e) {
			throw new RuntimeException("Error writing to stream", e);
		}
	}

	/** 
	 * Output the xml required at the beginning of a sheet.
	 */
	public void beginSheet() {
		writeToStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
		writeToStream("<sheetData>\n");
	}

	/** 
	 * Output the xml required at the end of sheet.
	 */
	public void endSheet() {
		writeToStream("</sheetData>");
		writeToStream("</worksheet>");
	}

	/**
	 * Output the xml required at the beginning of a row. 
	 */
	public void insertRow() {
		writeToStream("<row r=\"" + (rowCount + 1) + "\">\n");
	}

	/**
	 * Output the xml required at the end of a row and increment the counter.
	 */
	public void endRow() {
		writeToStream("</row>\n");
		rowCount++;
	}

	/** 
	 * Output the xml for a string cell at the given index in the current row. 
	 * @param columnIndex - 0-based index of the cell within the current row.
	 * @param value - the string value to populate the column with.  The method handles escaping necessary characters.
	 */
	public void createCell(final int columnIndex, final String value) {
		String ref = new CellReference(rowCount, columnIndex).formatAsString();
		writeToStream("<c r=\"" + ref + "\" t=\"inlineStr\"" + ">");
		writeToStream("<is><t>" + encode(value)	+ "</t></is>");
		writeToStream("</c>");
	}

	/** 
	 * Output the xml for a numeric cell at the given index in the current row. 
	 * @param columnIndex - 0-based index of the cell within the current row.
	 * @param value - the numeric value to populate the column with.
	 */
	public void createCell(final int columnIndex, final double value) {
		String ref = new CellReference(rowCount, columnIndex).formatAsString();
		writeToStream("<c r=\"" + ref + "\" t=\"n\"" + ">");
		writeToStream("<v>" + value + "</v>");
		writeToStream("</c>");
	}

	@Override
	public String encode(String value) {
		return StringEscapeUtils.escapeXml10(value);
	}

}
