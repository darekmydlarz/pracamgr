package pl.edu.agh.twitter;

import org.junit.Assert;
import org.junit.Test;

public class RetweetUserIdFillerTest {
    @Test
    public void shouldGetUsername() {
        RetweetUserIdFiller filler = new RetweetUserIdFiller();
        String text = "RT @ManUtd: How are you man?!";
        String username = filler.extractUsername(text);
        Assert.assertTrue("manutd".equals(username));
    }
    @Test
    public void shouldNotFindUsernamge() {
        RetweetUserIdFiller filler = new RetweetUserIdFiller();
        String text = "RT @ManUtd How are you man?!";
        String username = filler.extractUsername(text);
        Assert.assertTrue("".equals(username));
    }
}