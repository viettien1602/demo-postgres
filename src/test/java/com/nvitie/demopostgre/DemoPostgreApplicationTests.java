package com.nvitie.demopostgre;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;




class DemoPostgreApplicationTests {

	@Test
	void contextLoads() {
		Assertions.assertThat(10).isEqualTo(10);
	}

}
