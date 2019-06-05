package com.prototype.microservice.repository;

import com.prototype.microservice.aeprotal.AbstractCrmPwmServiceTest;
import com.prototype.microservice.etl.repository.EtlCommonRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EtlRepositoryTest extends AbstractCrmPwmServiceTest {
    private EtlCommonRepository etlCommonRepository;

    @Autowired
    public void setEtlCommonRepository(EtlCommonRepository etlCommonRepository) {
        this.etlCommonRepository = etlCommonRepository;
    }

    @Test
    public void testSearch() {
        etlCommonRepository.test();
    }

}
