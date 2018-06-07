package io.tim.fastdata.unzipsink;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.assertj.core.util.Files;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnzipSinkApplicationTests {

	@Autowired
	private Sink sink;
	
	@Test
	public void contextLoads() {
	}

	@Test
	public void success() {
		File file = new File("src/test/resources/200_th_fattureNonSaldate.txt.zip");
		file = new File(file.getAbsolutePath());
		
		Message<File> msg = new GenericMessage<File>(file);
		sink.input().send(msg);
		
		File target = new File(new File("src/test/resources/200_th.txt").getAbsolutePath());
		assertTrue(target.exists());
		
		Files.delete(target);

		assertFalse(target.exists());
	}


}
