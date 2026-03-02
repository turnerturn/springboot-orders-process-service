package cafe.shop.service.impl;

import cafe.shop.exception.TerminalNotFoundException;
import cafe.shop.model.dto.TerminalDto;
import cafe.shop.model.dto.TerminalRequestCreate;
import cafe.shop.model.entities.Terminal;
import cafe.shop.repository.TerminalRepository;
import cafe.shop.service.BaseService;
import cafe.shop.service.QueueService;
import cafe.shop.service.TerminalService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TerminalServiceImpl extends BaseService implements TerminalService {

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private QueueService queueService;

    @Override
    @Transactional
    public Terminal createTerminal(TerminalRequestCreate terminalRequestCreate) {
        Terminal terminal = new Terminal();
        terminal.setLocation(terminalRequestCreate.getLocation());
        terminal.setName(terminalRequestCreate.getName());
        terminal.setContactDetails(terminalRequestCreate.getContactDetails());
        terminal.setOpeningTime(terminalRequestCreate.getOpeningTime());
        terminal.setClosingTime(terminalRequestCreate.getClosingTime());
        terminal.setNumberOfQueues(terminalRequestCreate.getNumberOfQueues());
        terminal.setMaxQueueSize(terminalRequestCreate.getMaxQueueSize());
        terminal.setLatitude(terminalRequestCreate.getLatitude());
        terminal.setLongitude(terminalRequestCreate.getLongitude());

        terminal = terminalRepository.save(terminal);
        queueService.createQueueByTerminal(terminal);
        log.debug("Terminal with id: {} created successfully", terminal.getId().toString());

        return terminal;
    }

    @Override
    public Terminal updateTerminal(UUID terminalId, TerminalRequestCreate terminalRequest) {
        Optional<Terminal> optionalTerminal = terminalRepository.findById(terminalId);

        if (optionalTerminal.isPresent()) {
            Terminal terminal = optionalTerminal.get();
            terminal.setLocation(terminalRequest.getLocation());
            terminal.setName(terminalRequest.getName());
            terminal.setContactDetails(terminalRequest.getContactDetails());
            terminal.setOpeningTime(terminalRequest.getOpeningTime());
            terminal.setClosingTime(terminalRequest.getClosingTime());
            terminal.setNumberOfQueues(terminalRequest.getNumberOfQueues());
            terminal.setMaxQueueSize(terminalRequest.getMaxQueueSize());
            terminal.setLatitude(terminalRequest.getLatitude());
            terminal.setLongitude(terminalRequest.getLongitude());

            terminal = terminalRepository.save(terminal);
            log.debug("Terminal with id: {} updated successfully", terminal.getId().toString());
            return terminal;
        } else {
            throw new TerminalNotFoundException("Terminal not found with id: " + terminalId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Terminal getTerminal(UUID terminalId) {
        return terminalRepository.findById(terminalId)
                .orElseThrow(() -> new TerminalNotFoundException("Terminal not found with id: " + terminalId));
    }

    @Override
    public void deleteTerminal(UUID terminalId) {
        Terminal terminal = terminalRepository.findById(terminalId)
                .orElseThrow(() -> new TerminalNotFoundException("Terminal not found with id: " + terminalId));

        terminalRepository.delete(terminal);
        log.debug("Terminal with id: {} deleted successfully", terminalId);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Override
    public List<TerminalDto> findNearbyTerminals(double latitude, double longitude) {
        List<Terminal> terminals = terminalRepository.findAll();

        return terminals.stream()
                .map(terminal -> {
                    double distance = calculateDistance(latitude, longitude, terminal.getLatitude(), terminal.getLongitude());
                    return TerminalDto.builder()
                            .id(terminal.getId())
                            .name(terminal.getName())
                            .location(terminal.getLocation())
                            .distance(distance)
                            .build();
                })
                .sorted(Comparator.comparingDouble(TerminalDto::getDistance))
                .filter(item -> item.getDistance() <= 200)
                .collect(Collectors.toList());
    }
}
