package integration.helper;

import com.opinta.entity.Address;
import com.opinta.entity.Client;
import com.opinta.entity.Counterparty;
import com.opinta.entity.Phone;
import com.opinta.entity.PostOffice;
import com.opinta.entity.PostcodePool;
import com.opinta.entity.Shipment;
import com.opinta.entity.ShipmentGroup;
import com.opinta.service.AddressService;
import com.opinta.service.ClientService;
import com.opinta.service.CounterpartyService;
import com.opinta.service.PhoneService;
import com.opinta.service.PostOfficeService;
import com.opinta.service.PostcodePoolService;
import com.opinta.service.ShipmentGroupService;
import com.opinta.service.ShipmentService;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.opinta.entity.DeliveryType.D2D;

@Component
@Slf4j
public class TestHelper {
    private final ClientService clientService;
    private final AddressService addressService;
    private final CounterpartyService counterpartyService;
    private final PostcodePoolService postcodePoolService;
    private final ShipmentService shipmentService;
    private final PostOfficeService postOfficeService;
    private final PhoneService phoneService;
    private final ShipmentGroupService shipmentGroupService;

    @Autowired
    public TestHelper(ClientService clientService, AddressService addressService,
                      CounterpartyService counterpartyService, PostcodePoolService postcodePoolService,
                      ShipmentService shipmentService, PostOfficeService postOfficeService,
                      PhoneService phoneService,
                      ShipmentGroupService shipmentGroupService) {
        this.clientService = clientService;
        this.addressService = addressService;
        this.counterpartyService = counterpartyService;
        this.postcodePoolService = postcodePoolService;
        this.shipmentService = shipmentService;
        this.postOfficeService = postOfficeService;
        this.phoneService = phoneService;
        this.shipmentGroupService = shipmentGroupService;
    }

    public PostOffice createPostOffice() {
        PostOffice postOffice = new PostOffice("Lviv post office", createAddress(), createPostcodePool());
        return postOfficeService.saveEntity(postOffice);
    }

    public void deletePostOffice(PostOffice postOffice) {
        postOfficeService.delete(postOffice.getId());
        postcodePoolService.delete(postOffice.getPostcodePool().getUuid());
    }

    public Shipment createShipment() throws Exception {
        Shipment shipment = new Shipment(createClient(), createClient(),
                D2D, 4.0F, 3.8F, new BigDecimal(200), new BigDecimal(30), new BigDecimal(35.2));
        return shipmentService.saveEntity(shipment, shipment.getSender().getCounterparty().getUser());
    }

    public void deleteShipment(Shipment shipment) throws Exception {
        try {
            shipmentService.delete(shipment.getUuid(), shipment.getSender().getCounterparty().getUser());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        try {
            clientService.delete(shipment.getSender().getUuid(), shipment.getSender().getCounterparty().getUser());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        try {
            clientService.delete(shipment.getRecipient().getUuid(), shipment.getRecipient().getCounterparty().getUser());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    public Client createClient() throws Exception {
        Client client = new Client("FOP Ivanov", "001", createAddress(), createPhone(),
                createCounterparty());
        return clientService.saveEntity(client, client.getCounterparty().getUser());
    }

    public void deleteClient(Client client) throws Exception {
        try {
            clientService.delete(client.getUuid(), client.getCounterparty().getUser());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        try {
            addressService.delete(client.getAddress().getId());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        try {
            deleteCounterpartyWithPostcodePool(client.getCounterparty());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    public ShipmentGroup createShipmentGroup() throws Exception {
        ShipmentGroup shipmentGroup = new ShipmentGroup();
        shipmentGroup.setName("Group 1");
        shipmentGroup.setCounterparty(createCounterparty());
        return shipmentGroupService.saveEntity(shipmentGroup, shipmentGroup.getCounterparty().getUser());
    }

    public void deleteShipmentGroup(ShipmentGroup shipmentGroup) throws Exception {
        try {
            shipmentGroupService.delete(shipmentGroup.getUuid(), shipmentGroup.getCounterparty().getUser());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        try {
            deleteCounterpartyWithPostcodePool(shipmentGroup.getCounterparty());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    private Phone createPhone() {
        return phoneService.saveEntity(new Phone("0934314522"));
    }

    public Address createAddress() {
        Address address = new Address("00001", "Ternopil", "Monastiriska",
                "Monastiriska", "Sadova", "51", "");
        return addressService.saveEntity(address);
    }

    public Counterparty createCounterparty() throws Exception {
        Counterparty counterparty = new Counterparty("Modna kasta", createPostcodePool());
        return counterpartyService.saveEntity(counterparty);
    }

    public PostcodePool createPostcodePool() {
        return postcodePoolService.saveEntity(new PostcodePool("12345", false));
    }

    public void deleteCounterpartyWithPostcodePool(Counterparty counterparty) throws Exception{
        try {
            counterpartyService.delete(counterparty.getUuid(), counterparty.getUser());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    public JSONObject getJsonObjectFromFile(String filePath) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        return (JSONObject) jsonParser.parse(new FileReader(getFileFromResources(filePath)));
    }

    public String getJsonFromFile(String filePath) throws IOException, ParseException {
        return getJsonObjectFromFile(filePath).toString();
    }

    public File getFileFromResources(String path) {
        return new File(getClass().getClassLoader().getResource(path).getFile());
    }
}
