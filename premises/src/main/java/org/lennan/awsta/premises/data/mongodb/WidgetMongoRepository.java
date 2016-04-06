package org.lennan.awsta.premises.data.mongodb;

import org.lennan.awsta.data.Widget;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WidgetMongoRepository extends MongoRepository<Widget, String> {

	// TODO public Customer findByFirstName(String firstName);

	// TODO public List<Customer> findByLastName(String lastName);

}