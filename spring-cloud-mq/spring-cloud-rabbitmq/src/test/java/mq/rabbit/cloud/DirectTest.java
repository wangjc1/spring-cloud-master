package mq.rabbit.cloud;

import mq.rabbit.cloud.service.direct.HelloSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Direct Exchange:直接匹配,通过Exchange名称+RountingKey来发送与接收消息.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DirectTest {

	@Autowired
	private HelloSender helloSender;

	@Test
	public void hello() throws Exception {
		helloSender.send();
	}

}