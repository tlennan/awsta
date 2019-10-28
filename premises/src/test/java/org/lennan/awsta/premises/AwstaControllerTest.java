package org.lennan.awsta.premises;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lennan.awsta.controller.AwstaController;
import org.lennan.awsta.domain.Asset;
import org.lennan.awsta.premises.config.MongoConfiguration;
import org.lennan.awsta.repositories.AssetRepository;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AwstaControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AssetRepository repository;

	@Autowired
	private AwstaController awstaController;

	@Autowired
	private ObjectMapper mapper;

	private List<Asset> assets;

	@Before
	public void init()
	{
		assets = new ArrayList<>();

		repository.deleteAll();

		for (int i = 0; i < 6; i++) {
			String owner = "Manny";
			if (i % 3 == 1) {
				owner = "Moe";
			} else if (i % 3 == 2) {
				owner = "Jack";
			}
			Asset newAsset = repository.save(new Asset("Asset #" + i, "This is asset " + i, owner));
			assets.add(newAsset);
		}
	}

	@Test
	public void helloWorld() throws Exception
	{
		mockMvc.perform(get("/asset/test"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Awsta is UP")));
	}

	@Test
	public void listAllAssets() throws Exception
	{
		mockMvc.perform(get("/asset/").accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$", hasSize(6)));
	}

	@Test
	public void getAsset() throws Exception
	{
		Asset asset = assets.get(3);
		String assetId = asset.getId();
		mockMvc.perform(get("/asset/" + assetId).accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", equalTo(asset.getName())))
				.andExpect(jsonPath("$.description", equalTo(asset.getDescription())))
				.andExpect(jsonPath("$.owner", equalTo(asset.getOwner())));
	}

	@Test
	public void createAsset() throws Exception
	{
		String newName = "new";
		String newOwner = "owner";
		String newDescription = "desc";
		Asset asset = new Asset();
		asset.setName(newName);
		asset.setDescription(newDescription);
		asset.setOwner(newOwner);

		String json = mapper.writeValueAsString(asset);

		MvcResult result = mockMvc.perform(post("/asset/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", startsWith("http://localhost/asset/")))
				.andReturn();

		String location = result.getResponse().getHeader("Location");
		String newId = location.substring(location.lastIndexOf("/") + 1);

		Optional<Asset> createdAssetOptional = repository.findById(newId);
		assertThat(createdAssetOptional.isPresent() == true);

		Asset createdAsset = createdAssetOptional.get();
		assertThat(createdAsset.getName().equals(newName));
		assertThat(createdAsset.getDescription().equals(asset.getDescription()));
		assertThat(createdAsset.getOwner().equals(asset.getOwner()));
	}

	@Test
	public void updateAsset() throws Exception
	{
		String newName = "changed";
		Asset asset = assets.get(0);
		asset.setName(newName);
		String id = asset.getId();

		String json = mapper.writeValueAsString(asset);

		mockMvc.perform(put("/asset/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk());

		Optional<Asset> optionalAsset = repository.findById(id);
		assertThat(optionalAsset.isPresent() == true);

		Asset updatedAsset = optionalAsset.get();
		assertThat(updatedAsset.getId().equals(asset.getId()));
		assertThat(updatedAsset.getName().equals(newName));
		assertThat(updatedAsset.getDescription().equals(asset.getDescription()));
		assertThat(updatedAsset.getOwner().equals(asset.getOwner()));
	}

	@Test
	public void deleteAsset() throws Exception
	{
		Asset asset = assets.get(2);
		String assetId = asset.getId();
		mockMvc.perform(delete("/asset/" + assetId).accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().is2xxSuccessful());

		Optional<Asset> deletedAsset = repository.findById(assetId);
		assertThat(deletedAsset.isPresent() == false);
	}

	@Test
	public void deleteAllAssets() throws Exception
	{
		mockMvc.perform(delete("/asset/"))
				.andExpect(status().is2xxSuccessful());

		assert(repository.findAll().iterator().hasNext() == false);
	}
}