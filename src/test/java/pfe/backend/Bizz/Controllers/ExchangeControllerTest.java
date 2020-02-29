package pfe.backend.Bizz.Controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pfe.backend.DAL.Models.Exchange;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExchangeControllerTest {

    @MockBean
    private ExchangeController exchangeController;

    @Test
    public void contextLoad() {
        assertThat(exchangeController).isNotNull();
    }

    @Test
    public void getAllEmpty() {
        assertThat(this.exchangeController.getAll()).isEmpty();
    }

    @Test
    public void getAll() {
        List<Exchange> list = Stream.of(new Exchange(), new Exchange()).collect(Collectors.toList());
        when(exchangeController.getAll()).thenReturn(list);
        assertThat(this.exchangeController.getAll()).isEqualTo(list);
    }
}
