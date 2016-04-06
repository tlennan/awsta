package org.lennan.awsta.controller;

import org.lennan.awsta.data.Widget;
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
@RequestMapping(value = "/widget")
public class AwstaController {
	@Autowired
	private CrudRepository<Widget, String> repository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Widget>> listAllWidgets() {
		Iterable<Widget> widgets = repository.findAll();
		if (!widgets.iterator().hasNext()) {
			return new ResponseEntity<Iterable<Widget>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Iterable<Widget>>(widgets, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Widget> getWidget(@PathVariable("id") String id) {
		System.out.println("Fetching Widget with id " + id);
		Widget widget = repository.findOne(id);
		if (widget == null) {
			System.out.println("Widget with id " + id + " not found");
			return new ResponseEntity<Widget>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Widget>(widget, HttpStatus.OK);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<Void> createWidget(@RequestBody Widget widget, UriComponentsBuilder ucBuilder) {
		System.out.println("Creating widget " + widget.getName());

		if (repository.exists(widget.getId())) {
			System.out.println("A Widget with name " + widget.getName() + " already exists");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

		repository.save(widget);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/widget/{id}").buildAndExpand(widget.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Widget> updateWidget(@PathVariable("id") String id, @RequestBody Widget widget) {
		System.out.println("Updating Widget " + id);

		Widget currentWidget = repository.findOne(id);

		if (currentWidget == null) {
			System.out.println("Widget with id " + id + " not found");
			return new ResponseEntity<Widget>(HttpStatus.NOT_FOUND);
		}

		currentWidget.setName(widget.getName());
		currentWidget.setDescription(widget.getDescription());

		repository.save(currentWidget);
		return new ResponseEntity<Widget>(currentWidget, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Widget> deleteWidget(@PathVariable("id") String id) {
		System.out.println("Fetching & Deleting Widget with id " + id);

		Widget widget = repository.findOne(id);
		if (widget == null) {
			System.out.println("Unable to delete. Widget with id " + id + " not found");
			return new ResponseEntity<Widget>(HttpStatus.NOT_FOUND);
		}

		repository.delete(id);
		return new ResponseEntity<Widget>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	public ResponseEntity<Widget> deleteAllWidgets() {
		System.out.println("Deleting All Widgets");

		repository.deleteAll();
		return new ResponseEntity<Widget>(HttpStatus.NO_CONTENT);
	}

}
