package org.lennan.awsta.premises;

import org.lennan.awsta.Awsta;
import org.lennan.awsta.data.Widget;
import org.lennan.awsta.premises.data.mongodb.WidgetMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PremisesApplication implements CommandLineRunner {

	@Autowired
	private WidgetMongoRepository repository;
	Awsta awsta;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(PremisesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		awsta = new Awsta();
		awsta.setRepository(repository);
		// repository.deleteAll();

		// save a couple of customers
		// repository.save(new Widget("Alice", "Smith"));
		// repository.save(new Widget("Bob", "Smith"));

		// fetch all customers
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
		for (Widget widget : this.repository.findAll()) {
			System.out.println(widget);
		}
		System.out.println();

		// fetch an individual customer
		// TODO System.out.println("Customer found with
		// findByFirstName('Alice'):");
		// System.out.println("--------------------------------");
		// System.out.println(this.repository.findByFirstName("Alice"));
		//
		// System.out.println("Customers found with findByLastName('Smith'):");
		// System.out.println("--------------------------------");
		// for (Customer customer : this.repository.findByLastName("Smith")) {
		// System.out.println(customer);
		// }
	}

}