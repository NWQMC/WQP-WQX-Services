package gov.usgs.cida.wqp.transform;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import gov.usgs.cida.wqp.service.ILogService;

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

public class MapToDelimitedTransformerTest {

	public static final String CSV_HEADER = "colA,colB";
	public static final String TSV_HEADER = "colA\tcolB";

	@Mock
	protected ILogService logService;
	protected BigDecimal logId = new BigDecimal(1);
	protected MapToDelimitedTransformer transformer;
	protected ByteArrayOutputStream baos;
	protected Map<String, String> mapping;

	@Before
	public void initTest() {
		MockitoAnnotations.initMocks(this);
		baos = new ByteArrayOutputStream();
		mapping = new HashMap<>();
		mapping.put("A", "colA");
		mapping.put("B", "colB");
	}

	@After
	public void closeTest() throws IOException {
		transformer.close();
	}

	@Test
	public void writeDataTest() {
		transformer = new MapToDelimitedTransformer(baos, mapping, logService, logId, ",", "");
		Map<String, Object> result = new HashMap<>();
		result.put("A", "1");
		result.put("B", "2");
		result.put("C", "3");

		try {
			transformer.write(result);
			assertEquals(13, baos.size());
			assertEquals(CSV_HEADER + "\n1,2", new String(baos.toByteArray(), "UTF-8"));

			result.put("A", "11");
			result.put("B", "12");
			transformer.write(result);
			assertEquals(19, baos.size());
			assertEquals(CSV_HEADER + "\n1,2\n11,12", new String(baos.toByteArray(), "UTF-8"));

		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void writeDataMapTest() {
		transformer = new MapToDelimitedTransformer(baos, mapping, logService, logId, "\t", "");
		Map<String, Object> result = new HashMap<>();
		result.put("A", "1\r\n\t");
		result.put("B", "2");
		result.put("C", "3");

		try {
			transformer.writeData(result);
			assertEquals(16, baos.size());
			assertEquals(TSV_HEADER +  "\n1   \t2", new String(baos.toByteArray(), "UTF-8"));

			baos.reset();
			result.clear();
			result.put("C", "3");
			result.put("A", "1");
			result.put("B", "2");
			transformer.writeData(result);
			assertEquals(4, baos.size());
			assertEquals("\n1\t2", new String(baos.toByteArray(), "UTF-8"));

			baos.reset();
			result.put("A", null);
			transformer.writeData(result);
			assertEquals(3, baos.size());
			assertEquals("\n\t2", new String(baos.toByteArray(), "UTF-8"));
			transformer.close();

			transformer = new MapToDelimitedTransformer(baos, mapping, logService, logId, ",", "");
			baos.reset();
			result.clear();
			result.put("A", "b,1");
			result.put("B", "2\"x\td\"34\"");
			transformer.writeData(result);
			assertEquals(21, baos.size());
			assertEquals("\n\"b,1\",\"2\"\"x\td\"\"34\"\"\"", new String(baos.toByteArray(), "UTF-8"));

			baos.reset();
			result.clear();
			result.put("A", "\"sdjfhjks\"");
			result.put("B", "\"");
			transformer.writeData(result);
			assertEquals(20, baos.size());
			assertEquals("\n\"\"\"sdjfhjks\"\"\",\"\"\"\"", new String(baos.toByteArray(), "UTF-8"));

		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void writeHeaderTest() {
		transformer = new MapToDelimitedTransformer(baos, mapping, logService, logId, ",", "");
		try {
			assertEquals(9, baos.size());
			assertEquals(CSV_HEADER, new String(baos.toByteArray(), "UTF-8"));
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}

}
