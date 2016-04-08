package org.lennan.awsta.repositories;

import java.util.List;

import org.lennan.awsta.domain.Asset;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends PagingAndSortingRepository<Asset, String> {

	public Asset findByName(String name);

	public List<Asset> findByOwner(String owner);
}