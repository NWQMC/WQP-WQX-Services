package gov.usgs.cida.wqp.dao.streaming;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.ibatis.session.ResultContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import gov.usgs.cida.wqp.dao.StreamingResultHandler;
import gov.usgs.cida.wqp.transform.intfc.ITransformer;

public class StreamingResultHandlerTest {

	private StreamingResultHandler h;
	@Mock
	private ITransformer t;
	@Mock
	private ResultContext<Object> context;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		h = new StreamingResultHandler(t);
	}

	@Test
	public void testHandleResult() {
		when(context.getResultObject()).thenReturn("Hello");
		h.handleResult(context);
		verify(t).write(any());
	}

	@Test
	public void testHandleNullResult() {
		h.handleResult(null);
		verify(t, never()).write(any());
	}

}
