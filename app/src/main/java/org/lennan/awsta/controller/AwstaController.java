package org.lennan.awsta.controller;

import org.lennan.awsta.domain.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/asset")
public class AwstaController {
	@Autowired
	private CrudRepository<Asset, String> repository;

	@RequestMapping("/test")
	public String helloWorld() {
		return "Awsta is UP";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Asset>> listAllAssets() {
		Iterable<Asset> assets = repository.findAll();
		if (!assets.iterator().hasNext()) {
			return new ResponseEntity<Iterable<Asset>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Iterable<Asset>>(assets, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Asset> getAsset(@PathVariable("id") String id) {
		System.out.println("Fetching asset with id " + id);
		Asset asset = repository.findOne(id);
		if (asset == null) {
			System.out.println("Asset with id " + id + " not found");
			return new ResponseEntity<Asset>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Asset>(asset, HttpStatus.OK);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<Void> createAsset(@RequestBody Asset asset, UriComponentsBuilder ucBuilder) {
		System.out.println("Creating asset " + asset.getName());

		if (repository.exists(asset.getId())) {
			System.out.println("A asset with name " + asset.getName() + " already exists");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

		repository.save(asset);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/asset/{id}").buildAndExpand(asset.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Asset> updateAsset(@PathVariable("id") String id, @RequestBody Asset asset) {
		System.out.println("Updating asset " + id);

		Asset currentAsset = repository.findOne(id);

		if (currentAsset == null) {
			System.out.println("Asset with id " + id + " not found");
			return new ResponseEntity<Asset>(HttpStatus.NOT_FOUND);
		}

		currentAsset.setName(asset.getName());
		currentAsset.setDescription(asset.getDescription());

		repository.save(currentAsset);
		return new ResponseEntity<Asset>(currentAsset, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Asset> deleteAsset(@PathVariable("id") String id) {
		System.out.println("Fetching & Deleting asset with id " + id);

		Asset asset = repository.findOne(id);
		if (asset == null) {
			System.out.println("Unable to delete. Asset with id " + id + " not found");
			return new ResponseEntity<Asset>(HttpStatus.NOT_FOUND);
		}

		repository.delete(id);
		return new ResponseEntity<Asset>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	public ResponseEntity<Asset> deleteAllAssets() {
		System.out.println("Deleting All Assets");

		repository.deleteAll();
		return new ResponseEntity<Asset>(HttpStatus.NO_CONTENT);
	}

}
