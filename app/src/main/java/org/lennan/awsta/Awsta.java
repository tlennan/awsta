package org.lennan.awsta;

import org.lennan.awsta.data.Widget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;

@Configuration
public class Awsta {
	private int numberOfMessagesReceived = 0;

	@Autowired
	private CrudRepository<Widget, String> repository;

	public Awsta() {

	}

	@Autowired
	public void setRepository(CrudRepository<Widget, String> repository) {
		this.repository = repository;

		repository.deleteAll();

		for (int i = 0; i < 20; i++) {
			repository.save(new Widget("Widget #" + i, "This is widget number " + i));
		}
	}

	public CrudRepository<Widget, String> getRepository() {
		return repository;
	}
}
