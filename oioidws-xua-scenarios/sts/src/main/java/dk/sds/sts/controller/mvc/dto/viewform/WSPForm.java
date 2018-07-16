package dk.sds.sts.controller.mvc.dto.viewform;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WSPForm {
	private String id;
	private String name;
	private String entityId;
	private byte[] certificate;
}
