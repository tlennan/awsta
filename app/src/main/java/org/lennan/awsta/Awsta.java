package org.lennan.awsta;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lennan.awsta.domain.Asset;
import org.lennan.awsta.repositories.AssetRepository;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Awsta {
	private int numberOfMessagesReceived = 0;
	private AssetRepository repository;
	private Log log = LogFactory.getLog(Awsta.class);

	public Awsta(AssetRepository repository) {
		this.repository = repository;

		repository.deleteAll();

		for (int i = 0; i < 20; i++) {
			String owner = "Manny";
			if (i % 3 == 1) {
				owner = "Moe";
			} else if (i % 3 == 2) {
				owner = "Jack";
			}
			repository.save(new Asset("asset_" + i, "This is asset " + i, owner));
		}
	}

	@PostConstruct
	public void awstaReady() {
		log.info("Awsta ready");
		System.out.println("--------------------------------");
		System.out.println("Listing all IDs");
		for (Asset asset : repository.findAll()) {
			System.out.println(asset.getId());
		}

		// fetch an individual asset
		System.out.println("--------------------------------");
		System.out.println("Names of assets owned by 'Manny'");
		for (Asset asset : repository.findByOwner("Manny")) {
			System.out.println(asset.getName());
		}
	}
}
