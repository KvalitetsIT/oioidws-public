package dk.sds.sts.dao;

import org.springframework.data.repository.CrudRepository;

import dk.sds.sts.dao.model.WebServiceConsumer;

public interface WebServiceConsumerDao extends CrudRepository<WebServiceConsumer, Long> {
	WebServiceConsumer getById(long id);
	WebServiceConsumer getBySubject(String subject);
}
