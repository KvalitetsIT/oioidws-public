package dk.sds.sts.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class WebServiceProvider {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String entityId;
	
	@Column(nullable = false)
	private String subject;

	@Column(nullable = false)
	private byte[] certificate;
}
