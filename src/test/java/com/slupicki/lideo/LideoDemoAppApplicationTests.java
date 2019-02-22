package com.slupicki.lideo;

import com.slupicki.lideo.misc.TimeProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class LideoDemoAppApplicationTests {

  @Autowired
  private TimeProvider timeProvider;

  @Test
  public void contextLoads() {
    System.out.println("********************");
    System.out.println("Time provider: " + timeProvider.getClass().getCanonicalName());
    System.out.println("********************");
  }

}

