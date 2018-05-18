package dk.sds.sts.dao;

import org.springframework.data.repository.CrudRepository;

import dk.sds.sts.dao.model.WebServiceProvider;

public interface WebServiceProviderDao extends CrudRepository<WebServiceProvider, Long> {
	WebServiceProvider getById(long id);
	WebServiceProvider getByEntityId(String entityId);
}
