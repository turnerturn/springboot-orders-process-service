package cafe.shop.controller;

import cafe.shop.model.dto.ErrorResponse;
import cafe.shop.model.dto.TerminalDto;
import cafe.shop.model.dto.TerminalRequestCreate;
import cafe.shop.model.entities.Terminal;
import cafe.shop.service.TerminalService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/terminals")
@Log4j2
public class TerminalController {

    @Autowired
    private TerminalService terminalService;

    @PostMapping
    public ResponseEntity<Terminal> createTerminal(@RequestBody TerminalRequestCreate terminalDto) {
        Terminal terminal = terminalService.createTerminal(terminalDto);
        return ResponseEntity.ok(terminal);
    }

    @PutMapping("/{terminalId}")
    public ResponseEntity<Terminal> updateTerminal(
            @PathVariable UUID terminalId,
            @RequestBody TerminalRequestCreate terminalDto) {
        Terminal updatedTerminal = terminalService.updateTerminal(terminalId, terminalDto);
        return ResponseEntity.ok(updatedTerminal);
    }

    @GetMapping("/{terminalId}")
    public ResponseEntity<Terminal> getTerminal(@PathVariable UUID terminalId) {
        Terminal terminal = terminalService.getTerminal(terminalId);
        return ResponseEntity.ok(terminal);
    }

    @DeleteMapping("/{terminalId}")
    public ResponseEntity<Void> deleteTerminal(@PathVariable UUID terminalId) {
        terminalService.deleteTerminal(terminalId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyTerminals(@RequestParam double latitude, @RequestParam double longitude) {
        try {
            List<TerminalDto> terminals = terminalService.findNearbyTerminals(latitude, longitude);
            return ResponseEntity.ok(terminals);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Server error", "An error occurred while retrieving terminals"));
        }
    }
}
