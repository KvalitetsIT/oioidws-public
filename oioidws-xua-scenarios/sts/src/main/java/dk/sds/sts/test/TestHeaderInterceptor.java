package dk.sds.sts.test;

import java.util.List;
import java.util.Map;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.http.HttpStatus;

public class TestHeaderInterceptor extends AbstractPhaseInterceptor<Message> {
	private static final String TESTHEADER = "testheader";
	public static enum FAULT {
		SERVER_ERROR,
		EXPIRED_TOKEN,
		BAD_CERTIFICATE,
		BAD_TOKENTYPE,
		NONE
	};

	// very hackish way to drive the test-cases for the STS (also not thread-safe ;))
	public static FAULT fault = FAULT.NONE;
	
	public TestHeaderInterceptor() {
		super(Phase.READ);
	}

	
	@Override
	public void handleMessage(Message message) throws Fault {
		fault = FAULT.NONE; // reset

		@SuppressWarnings("unchecked")
		Map<String, List<String>> inHeaders = (Map<String, List<String>>) message.getExchange().getInMessage().get(Message.PROTOCOL_HEADERS);
		
		if (inHeaders != null) {			
			if (inHeaders.containsKey(TESTHEADER)) {
				if (inHeaders.get(TESTHEADER).isEmpty()) {
					throw new Fault(new Exception("Found testheader but the value was missing."));
				}

				String value = inHeaders.get(TESTHEADER).get(0);
								
				if (value.equals(FAULT.SERVER_ERROR.toString())) {
					throw new Fault(new Exception(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
				}
				else if (value.equals(FAULT.BAD_CERTIFICATE.toString())) {
					fault = FAULT.BAD_CERTIFICATE;
				}
				else if (value.equals(FAULT.BAD_TOKENTYPE.toString())) {
					fault = FAULT.BAD_TOKENTYPE;
				}
				else if (value.equals(FAULT.EXPIRED_TOKEN.toString())) {
					fault = FAULT.EXPIRED_TOKEN;
				}
			}
		}
	}
}
