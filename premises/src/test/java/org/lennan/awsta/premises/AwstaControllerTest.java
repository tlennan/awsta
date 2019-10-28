package org.lennan.awsta.premises;

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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
	public void createAsset()
	{
	}

	@Test
	public void updateAsset()
	{
	}

	@Test
	public void deleteAsset()
	{
	}

	@Test
	public void deleteAllAssets() throws Exception
	{
		mockMvc.perform(delete("/asset/"))
				.andExpect(status().is2xxSuccessful());

		assert(repository.findAll().iterator().hasNext() == false);
	}
}