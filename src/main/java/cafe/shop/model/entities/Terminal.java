package cafe.shop.model.entities;

import cafe.shop.model.BaseTimestamp;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "terminals")
public class Terminal extends BaseTimestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "name")
    private String name;

    @Column(name = "contact_details", nullable = false)
    private String contactDetails;

    @Column(name = "opening_time", nullable = false)
    private LocalTime openingTime;

    @Column(name = "closing_time", nullable = false)
    private LocalTime closingTime;

    @Column(name = "number_of_queues", nullable = false)
    private int numberOfQueues;

    @Column(name = "max_queue_size", nullable = false)
    private int maxQueueSize;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @OneToMany(mappedBy = "terminal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Queue> queues;
}
