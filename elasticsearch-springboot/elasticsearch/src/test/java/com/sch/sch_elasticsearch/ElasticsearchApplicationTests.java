package com.sch.sch_elasticsearch;

import com.sch.sch_elasticsearch.domain.test.TestController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ElasticsearchApplicationTests {

    @Autowired
    private TestController testController;

    @Test
    void contextLoads() {
    }

    @Test
    public void testBoot() {
        testController.bootCheck();
    }

}
