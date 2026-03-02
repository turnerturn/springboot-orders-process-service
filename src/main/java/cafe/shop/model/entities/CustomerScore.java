package cafe.shop.model.entities;

import cafe.shop.model.BaseTimestamp;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "customer_scores")
public class CustomerScore extends BaseTimestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "terminal_id", nullable = false)
    private Terminal terminal;

    @Column(name = "score", nullable = false)
    private int score;

    public CustomerScore() {
    }

    public CustomerScore(User customer, Terminal terminal) {
        this.customer = customer;
        this.terminal = terminal;
    }
}
