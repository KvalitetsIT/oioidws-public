package dk.sds.sts.controller.mvc.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
public class CertificateResponseDTO implements Serializable{
	byte[] certificate;
}
