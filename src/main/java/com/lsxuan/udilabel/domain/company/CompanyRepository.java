package com.lsxuan.udilabel.domain.company;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {

    void save(Company company);

    Optional<Company> findById(UUID id);

    List<Company> findByUserId(UUID userId);

    void update(Company company);

    void delete(UUID id);
}
