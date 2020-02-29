package pfe.backend.DAL.Services;

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
public class ExchangeServiceTest {

    @MockBean
    private ExchangeService exchangeService;

    @Test
    public void findAllIsEmpty() {
        assertThat(this.exchangeService.findAll()).isEmpty();
    }

    @Test
    public void findAll() {
        List<Exchange> list = Stream.of(new Exchange(), new Exchange()).collect(Collectors.toList());
        when(exchangeService.findAll()).thenReturn(list);
        assertThat(exchangeService.findAll()).isEqualTo(list);
    }
}
