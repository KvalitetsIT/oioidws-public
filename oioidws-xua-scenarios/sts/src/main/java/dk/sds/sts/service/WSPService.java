package dk.sds.sts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dk.sds.sts.dao.WebServiceProviderDao;
import dk.sds.sts.dao.model.WebServiceProvider;

@Service
public class WSPService {

	@Autowired
	private WebServiceProviderDao wspDao;

	public void delete(Long id) {
		wspDao.delete(id);
	}

	public Iterable<WebServiceProvider> findAll() {
		return wspDao.findAll();
	}

	public WebServiceProvider save(WebServiceProvider entity) {
		return wspDao.save(entity);
	}

	public WebServiceProvider getById(long id) {
		return wspDao.getById(id);
	}

	public WebServiceProvider getByEntityId(String entityId) {
		return wspDao.getByEntityId(entityId);
	}
}
