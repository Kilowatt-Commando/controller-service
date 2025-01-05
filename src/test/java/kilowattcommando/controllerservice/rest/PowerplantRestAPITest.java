package kilowattcommando.controllerservice.rest;

import dto.PowerplantCommand;
import kilowattcommando.controllerservice.kafka.PowerPlantCommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PowerplantRestController.class)
public class PowerplantRestAPITest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PowerPlantCommandSender powerPlantCommandSender;

    @TestConfiguration
    static class PowerplantRestTestConfiguration {
        @Bean
        public PowerPlantCommandSender powerPlantCommandSender() {
            return mock(PowerPlantCommandSender.class);
        }
    }

    @BeforeEach
    public void reset() {
        Mockito.reset(powerPlantCommandSender);
    }

    @Test
    public void startPowerplantWithId_1_OK() throws Exception {
        mockMvc.perform(put("/powerplant/1/start")).andExpect(status().isOk());

        verify(powerPlantCommandSender, times(1)).sendCommand("1", PowerplantCommand.start);
    }

    @Test
    public void stopPowerplantWithId_1_OK() throws Exception {
        mockMvc.perform(put("/powerplant/1/stop")).andExpect(status().isOk());

        verify(powerPlantCommandSender, times(1)).sendCommand("1", PowerplantCommand.stop);
    }

    @Test
    public void closeGateOnPlant_1_OK() throws Exception {
        mockMvc.perform(put("/powerplant/1/gateClosure").contentType(MediaType.APPLICATION_JSON).content("{\"closure\":\"CLOSED\"}")).andExpect(status().isOk());

        verify(powerPlantCommandSender, times(1)).sendCommand("1", PowerplantCommand.gateClose);
    }

    @Test
    public void openGateOnPlant_1_OK() throws Exception {
        mockMvc.perform(put("/powerplant/1/gateClosure").contentType(MediaType.APPLICATION_JSON).content("{\"closure\":\"OPEN\"}")).andExpect(status().isOk());

        verify(powerPlantCommandSender, times(1)).sendCommand("1", PowerplantCommand.gateOpen);
    }

    @Test
    public void setGateToQuarterOnPlant_1_OK() throws Exception {
        mockMvc.perform(put("/powerplant/1/gateClosure").contentType(MediaType.APPLICATION_JSON).content("{\"closure\":\"QUARTER\"}")).andExpect(status().isOk());

        verify(powerPlantCommandSender, times(1)).sendCommand("1", PowerplantCommand.gateQuarter);
    }

    @Test
    public void setGateToHalfOnPlant_1_OK() throws Exception {
        mockMvc.perform(put("/powerplant/1/gateClosure").contentType(MediaType.APPLICATION_JSON).content("{\"closure\":\"HALF\"}")).andExpect(status().isOk());

        verify(powerPlantCommandSender, times(1)).sendCommand("1", PowerplantCommand.gateHalf);
    }

    @Test
    public void setGateToThreeQuartersOnPlant_1_OK() throws Exception {
        mockMvc.perform(put("/powerplant/1/gateClosure").contentType(MediaType.APPLICATION_JSON).content("{\"closure\":\"THREE_QUARTERS\"}")).andExpect(status().isOk());

        verify(powerPlantCommandSender, times(1)).sendCommand("1", PowerplantCommand.gateThreeQuarters);
    }

    @Test
    public void invalidGateClosureOnPlant_1_400() throws Exception {
        mockMvc.perform(put("/powerplant/1/gateClosure").contentType(MediaType.APPLICATION_JSON).content("{\"closure\":\"THIRD\"}")).andExpect(status().is(400));

        verify(powerPlantCommandSender, times(0)).sendCommand(anyString(), any());
    }
}
