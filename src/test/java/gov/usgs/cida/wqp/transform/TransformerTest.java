package gov.usgs.cida.wqp.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.usgs.cida.wqp.service.ILogService;
import gov.usgs.cida.wqp.util.HttpConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TransformerTest {

	@Mock
	protected ILogService logService;
	protected BigDecimal logId = new BigDecimal(1);

	@Before
	public void initTest() {
		MockitoAnnotations.initMocks(this);
	}

	private class TTransformer extends Transformer {
		public int initCalled = 0;
		public int writeHeaderCalled = 0;
		public int writeDataCalled = 0;
		public TTransformer(OutputStream target, Map<String, String> mapping, ILogService logService, BigDecimal logId) {
			super(target, mapping, logService, logId);
		}
		@Override
		protected void init() {
			initCalled = initCalled + 1;
		}
		@Override
		protected void writeHeader() {
			writeHeaderCalled = writeHeaderCalled + 1;
		}
		@Override
		protected void writeData(Map<String, Object> resultMap) {
			writeDataCalled = writeDataCalled + 1;
		}
		@Override
		public String encode(String value) {
			return null;
		}
		@Override
		public void end() {
		}
	}

	@Test
	@SuppressWarnings("unchecked")
	public void writeTest() {
		TTransformer transformer = new TTransformer(new ByteArrayOutputStream(), null, logService, logId);

		//Don't process null results
		try {
			transformer.write((Object) null);
			assertEquals(0, transformer.initCalled);
			assertEquals(0, transformer.writeHeaderCalled);
			assertEquals(0, transformer.writeDataCalled);
			verify(logService, never()).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(Map.class));
			verify(logService, never()).logHeadComplete(any(HttpServletResponse.class), eq(logId));
			verify(logService, never()).logFirstRowComplete(logId);
			verify(logService, never()).logRequestComplete(eq(logId), anyString());
			transformer.close();
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}

		//Don't process results that aren't a map
		try {
			transformer.write((Object) "ABCDEFG");
			assertEquals(0, transformer.initCalled);
			assertEquals(0, transformer.writeHeaderCalled);
			assertEquals(0, transformer.writeDataCalled);
			verify(logService, never()).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(Map.class));
			verify(logService, never()).logHeadComplete(any(HttpServletResponse.class), eq(logId));
			verify(logService, never()).logFirstRowComplete(logId);
			verify(logService, never()).logRequestComplete(eq(logId), anyString());
			transformer.close();
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}

		Map<String, Object> result = new HashMap<>();
		result.put("A", "1");
		result.put("B", "2");

		try {
			transformer.write((Object) result);
			transformer.write((Object) result);
			assertEquals(0, transformer.initCalled);
			assertEquals(0, transformer.writeHeaderCalled);
			assertEquals(2, transformer.writeDataCalled);
			verify(logService, never()).logRequest(any(HttpServletRequest.class), any(HttpServletResponse.class), any(Map.class));
			verify(logService, never()).logHeadComplete(any(HttpServletResponse.class), eq(logId));
			verify(logService, times(1)).logFirstRowComplete(logId);
			verify(logService, never()).logRequestComplete(eq(logId), anyString());
			transformer.close();
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void copyStringTest() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		MapToDelimitedTransformer transformer = new MapToDelimitedTransformer(baos, new HashMap<String, String>(), logService, logId, ",", "");
		try {
			transformer.writeToStream(null);
			assertEquals(0, baos.size());
			
			transformer.writeToStream("abc");
			assertEquals(3, baos.size());
			assertEquals("abc", new String(baos.toByteArray(), HttpConstants.DEFAULT_ENCODING));

			transformer.writeToStream("\u00A7\u00AEabcDE\u00A9\u00B1");
			assertEquals(16, baos.size());
			assertEquals("abc\u00A7\u00AEabcDE\u00A9\u00B1", new String(baos.toByteArray(), HttpConstants.DEFAULT_ENCODING));

			transformer.close();
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}
	}

}
