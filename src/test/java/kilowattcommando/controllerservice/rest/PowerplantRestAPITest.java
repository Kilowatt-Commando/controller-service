package kilowattcommando.controllerservice.rest;

import kilowattcommando.controllerservice.ControllerServiceApplication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PowerplantRestController.class)
public class PowerplantRestAPITest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void startPowerplantWithId_1_OK() throws Exception {
        mockMvc.perform(put("/powerplant/1/start")).andExpect(status().isOk());
    }

    @Test
    public void stopPowerplantWithId_1_OK() throws Exception {
        mockMvc.perform(put("/powerplant/1/stop")).andExpect(status().isOk());
    }

    @Test
    public void closeGateOnPlant_1_OK() throws Exception {
        mockMvc.perform(put("/powerplant/1/gateClosure").contentType(MediaType.APPLICATION_JSON).content("{\"closure\":\"CLOSED\"}")).andExpect(status().isOk());
    }

    @Test
    public void openGateOnPlant_1_OK() throws Exception {
        mockMvc.perform(put("/powerplant/1/gateClosure").contentType(MediaType.APPLICATION_JSON).content("{\"closure\":\"OPEN\"}")).andExpect(status().isOk());
    }

    @Test
    public void setGateToQuarterOnPlant_1_OK() throws Exception {
        mockMvc.perform(put("/powerplant/1/gateClosure").contentType(MediaType.APPLICATION_JSON).content("{\"closure\":\"QUARTER\"}")).andExpect(status().isOk());
    }

    @Test
    public void setGateToHalfOnPlant_1_OK() throws Exception {
        mockMvc.perform(put("/powerplant/1/gateClosure").contentType(MediaType.APPLICATION_JSON).content("{\"closure\":\"HALF\"}")).andExpect(status().isOk());
    }

    @Test
    public void setGateToThreeQuartersOnPlant_1_OK() throws Exception {
        mockMvc.perform(put("/powerplant/1/gateClosure").contentType(MediaType.APPLICATION_JSON).content("{\"closure\":\"THREE_QUARTERS\"}")).andExpect(status().isOk());
    }

    @Test
    public void invalidGateClosureOnPlant_1_400() throws Exception {
        mockMvc.perform(put("/powerplant/1/gateClosure").contentType(MediaType.APPLICATION_JSON).content("{\"closure\":\"THIRD\"}")).andExpect(status().is(400));
    }
}
