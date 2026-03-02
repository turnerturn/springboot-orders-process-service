package cafe.shop.service;

import cafe.shop.model.dto.TerminalDto;
import cafe.shop.model.dto.TerminalRequestCreate;
import cafe.shop.model.entities.Terminal;

import java.util.List;
import java.util.UUID;

public interface TerminalService {

    Terminal createTerminal(TerminalRequestCreate terminalRequestCreate);

    Terminal updateTerminal(UUID terminalId, TerminalRequestCreate terminalRequest);

    Terminal getTerminal(UUID terminalId);

    void deleteTerminal(UUID terminalId);

    List<TerminalDto> findNearbyTerminals(double latitude, double longitude);
}
