package dk.sds.sts.test;

import java.util.List;
import java.util.Map;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.http.HttpStatus;

public class TestHeadersInterceptor extends AbstractPhaseInterceptor<Message> {
	private static final String TESTHEADER = "testheader";

	public TestHeadersInterceptor() {
		super(Phase.READ);
	}

	@Override
	public void handleMessage(Message message) throws Fault {

		@SuppressWarnings("unchecked")
		Map<String, List<String>> inHeaders = (Map<String, List<String>>) message.getExchange().getInMessage().get(Message.PROTOCOL_HEADERS);

		if (inHeaders != null) {
			if (inHeaders.containsKey(TESTHEADER)) {
				if (inHeaders.get(TESTHEADER).isEmpty()) {
					throw new Fault(new Exception("Found testheader but the value was missing."));
				}

				String value = inHeaders.get(TESTHEADER).get(0);
				if (value.equals("500")) {
					throw new Fault(new Exception(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
				}
			}
		}
	}
}
