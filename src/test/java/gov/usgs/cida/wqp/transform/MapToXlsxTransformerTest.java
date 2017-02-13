package gov.usgs.cida.wqp.transform;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import gov.usgs.cida.wqp.service.ILogService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MapToXlsxTransformerTest {

	@Mock
	protected ILogService logService;
	protected BigDecimal logId = new BigDecimal(1);
	private int rowCount = 1;
	protected MapToXlsxTransformer transformer;
	protected ByteArrayOutputStream baos;
	protected Map<String, String> mapping;

	@Before
	public void initTest() {
		MockitoAnnotations.initMocks(this);
		baos = new ByteArrayOutputStream();
		mapping = new LinkedHashMap<>();
		mapping.put("A", "colA");
		mapping.put("B", "colB");
		mapping.put("C", "colC");
		mapping.put("D", "colD");
		mapping.put("E", "colE");
		mapping.put("F", "colF");
		mapping.put("ACTIVITY_ID", "ActMetURL");
		transformer = new MapToXlsxTransformer(baos, mapping, logService, logId, "ROOTURL");
	}

	@After
	public void closeTest() throws IOException {
		transformer.close();
	}

	@Test
	public void writeTest() {
		try {
			transformer.write(getTestRow());
			transformer.end();
			
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			Workbook wb = new XSSFWorkbook(bais);
			assertNotNull(wb);
			Sheet sheet = wb.getSheet("report");
			assertNotNull(sheet);
			assertEquals(1, sheet.getLastRowNum());
			Row row0 = sheet.getRow(0);
			assertNotNull(row0);
			Iterator<Cell> x = row0.cellIterator();
			while (x.hasNext()) {
				Cell c = x.next();
				switch (c.getStringCellValue()) {
				case "colA":
					break;
				case "colB":
					break;
				case "colC":
					break;
				case "colD":
					break;
				case "colE":
					break;
				case "colF":
					break;
				case "ActMetURL":
					break;
				default:
					fail(c.getStringCellValue() + " is not valid");
				}
			}
			
			Row row1 = sheet.getRow(1);
			assertNotNull(row1);
			Iterator<Cell> y = row1.cellIterator();
			int i = 1;
			while (y.hasNext()) {
				y.next();
				i++;
			}
			assertEquals(7, i);
			assertEquals("data1", row1.getCell(0).getStringCellValue());
			assertEquals("data2", row1.getCell(1).getStringCellValue());
			assertEquals("1", row1.getCell(2).getStringCellValue());
			//We don't do dates - this is the ugly default JAVA .toString() format
			assertEquals("Wed Dec 31 18:00:10 CST 1969", row1.getCell(3).getStringCellValue());
			assertNull(row1.getCell(4));
			assertEquals(29382.2398, row1.getCell(5).getNumericCellValue(), 0);
			assertEquals("Was expecting something more from you.", "ROOTURL/activities/1234/activitymetrics", row1.getCell(6).getStringCellValue());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void bunchOfRows() {
		try {
			for (int i = 0; i < 1000; i++) {
				transformer.write(getTestRow());
			}
			transformer.end();
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			Workbook wb = new XSSFWorkbook(bais);
			assertNotNull(wb);
			Sheet sheet = wb.getSheet("report");
			assertNotNull(sheet);
			Row row1 = sheet.getRow(0);
			assertNotNull(row1);
			assertEquals("colA", row1.getCell(0).getStringCellValue());
			
			Row lastRow = sheet.getRow(1000);
			assertNotNull(lastRow);
			assertEquals("1000", lastRow.getCell(2).getStringCellValue());
			assertEquals(1000, sheet.getLastRowNum());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}
	
	public Map<String, Object> getTestRow() {
		Map<String, Object> record = new LinkedHashMap<String, Object>();
		record.put("A", "data1");
		record.put("B", "data2");
		record.put("C", rowCount++);
		record.put("D", new Date(10000));
		record.put("E", null);
		record.put("F", new BigDecimal(29382.2398));
		record.put("G", "nocando");
		record.put("ACTIVITY_ID", 1234);

		return record;
	}
	
	@Test
	public void endNullPointerExceptionTest() {
		//should not fail with NullPointerException without any data having been written
		transformer.end();
	}
}