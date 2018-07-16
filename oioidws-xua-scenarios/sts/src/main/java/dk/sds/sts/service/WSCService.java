package dk.sds.sts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dk.sds.sts.dao.WebServiceConsumerDao;
import dk.sds.sts.dao.model.WebServiceConsumer;

@Service
public class WSCService {

	@Autowired
	private WebServiceConsumerDao wscDao;

	public WebServiceConsumer save(WebServiceConsumer entity) {
		return wscDao.save(entity);
	}

	public Iterable<WebServiceConsumer> findAll() {
		return wscDao.findAll();
	}

	public void delete(Long id) {
		wscDao.delete(id);
	}

	public void delete(WebServiceConsumer entity) {
		wscDao.delete(entity);
	}

	public WebServiceConsumer getById(long id) {
		return wscDao.getById(id);
	}
}
