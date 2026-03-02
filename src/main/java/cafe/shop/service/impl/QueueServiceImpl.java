package cafe.shop.service.impl;

import cafe.shop.exception.ResourceNotFoundException;
import cafe.shop.model.constant.OrderStatus;
import cafe.shop.model.dto.*;
import cafe.shop.model.entities.*;
import cafe.shop.model.entities.Queue;
import cafe.shop.repository.*;
import cafe.shop.service.BaseService;
import cafe.shop.service.QueueService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Log4j2
public class QueueServiceImpl extends BaseService implements QueueService {

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerQueueRepository customerQueueRepository;

    @Autowired
    private CustomerScoreRepository customerScoreRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createQueueByTerminal(Terminal terminal) {
        List<Queue> queueList = new ArrayList<>();
        for (int i = 0; i < terminal.getNumberOfQueues(); i++) {
            Queue queue = new Queue();
            queue.setTerminal(terminal);
            queue.setQueueNumber(i + 1);
            queueList.add(queue);
        }
        queueRepository.saveAll(queueList);
    }

    @Override
    public QueueStatusDto getQueueStatus(UUID queueId) {
        Queue queue = queueRepository.findById(queueId)
                .orElseThrow(() -> new ResourceNotFoundException("Queue not found"));

        List<CustomerQueue> waitingCustomers = customerQueueRepository.findAllByQueueOrderByCreatedAtAsc(queue);

        QueueStatusDto status = new QueueStatusDto();
        status.setQueueId(queueId);
        status.setQueueNumber(queue.getQueueNumber());
        status.setNumberOfWaitingCustomers(waitingCustomers.size());

        return status;
    }

    @Override
    public List<CustomerQueueDto> getOrdersInQueue(UUID queueId) {
        Queue queue = queueRepository.findById(queueId)
                .orElseThrow(() -> new ResourceNotFoundException("Queue not found"));

        AtomicInteger positionCounter = new AtomicInteger(1);
        return customerQueueRepository.findAllByQueueOrderByCreatedAtAsc(queue).stream().map(customerQueue -> {
            CustomerQueueDto dto = new CustomerQueueDto();
            dto.setOrderId(customerQueue.getId());
            dto.setOrderNumber(customerQueue.getOrder().getOrderNumber());
            dto.setQueuePosition(positionCounter.getAndIncrement());
            dto.setCustomerName(customerQueue.getCustomer().getFirstName());
            dto.setCustomerPhone(customerQueue.getCustomer().getMobileNumber());
            dto.setCustomerQueueId(customerQueue.getId());
            dto.setRecipeName(customerQueue.getOrder().getRecipe().getName());
            dto.setVolume(customerQueue.getOrder().getVolume());
            dto.setDestinationId(customerQueue.getOrder().getDestinationId());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<QueueDto> getQueuesByTerminal(Terminal terminal) {
        return queueRepository.findAllByTerminal(terminal).stream()
                .map(queue -> QueueDto.builder()
                    .id(queue.getId())
                    .queueNumber(queue.getQueueNumber())
                    .build()
                ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void serviceCustomer(UUID queueId, UUID customerQueueId) {
        Queue queue = queueRepository.findById(queueId)
                .orElseThrow(() -> new ResourceNotFoundException("Queue not found"));

        CustomerQueue customerQueue = customerQueueRepository.findById(customerQueueId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not in queue"));

        Order order = customerQueue.getOrder();
        order.setStatus(OrderStatus.SUCCESS);
        orderRepository.save(order);
        log.info("[serviceQueue: {}] - Mark order status: {}", queueId, order.getId());

        queue.getCustomerQueues().remove(customerQueue);
        customerQueueRepository.delete(customerQueue);
        log.info("[serviceQueue: {}] - Removed customer from queue", queueId);

        log.info("[serviceQueue: {}] - Find and update the customer score: {}, ", queueId, order.getCustomer());
        CustomerScore customerScore = customerScoreRepository.findByCustomer(order.getCustomer()).orElse(null);
        if (customerScore == null) {
            customerScore = new CustomerScore(order.getCustomer(), order.getTerminal());
        }
        customerScore.setScore(customerScore.getScore() + 1);
        customerScoreRepository.save(customerScore);
    }

    @Override
    public Queue addOrderToQueue(Order order, User customer) {
        Queue queue = findAvailableQueue(order.getTerminal());
        if (queue == null) {
            throw new ResourceNotFoundException("No available queue for the terminal");
        }

        CustomerQueue customerQueue = new CustomerQueue();
        customerQueue.setCustomer(customer);
        customerQueue.setQueue(queue);
        customerQueue.setWaitingSince(LocalDateTime.now());
        customerQueue.setOrder(order);
        customerQueueRepository.save(customerQueue);

        return queue;
    }

    @Override
    public int getCustomerQueueSize(Queue queue) {
        List<CustomerQueue> customerQueueList = customerQueueRepository.findAllByQueueOrderByCreatedAtAsc(queue);
        return customerQueueList.size();
    }

    @Override
    public void removeOrderFromQueue(Queue queue, Order order) {
        Optional<CustomerQueue> customerQueueOptional = customerQueueRepository.findByOrder(order);
        if (customerQueueOptional.isPresent()) {
            CustomerQueue customerQueue = customerQueueOptional.get();
            customerQueueRepository.delete(customerQueue);
        } else {
            throw new ResourceNotFoundException("Customer not found in queue");
        }
    }

    private Queue findAvailableQueue(Terminal terminal) {
        List<Queue> queueList = queueRepository.findAllByTerminal(terminal);
        return queueList.stream()
                .filter(queue -> this.getCustomerQueueSize(queue) < terminal.getMaxQueueSize())
                .findFirst()
                .orElse(null);
    }
}
