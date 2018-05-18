package dk.sds.sts.controller.mvc.dto.viewform;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WSCForm {
	private String id;
	private String name;
	private byte[] certificate;
	private String[] webServiceProviders;
}
