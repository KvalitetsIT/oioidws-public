package dk.sds.sts.dao.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WebServiceConsumer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
		
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String subject;

	@Column(nullable = false)
	private byte[] certificate;

	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "granted_access", joinColumns = { @JoinColumn(name = "wsc_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "wsp_id", referencedColumnName = "id") })
	private List<WebServiceProvider> webServiceProviders;
}
