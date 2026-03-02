package cafe.shop.service.impl;

import cafe.shop.common.GenericSpecification;
import cafe.shop.exception.CustomerAlreadyExistsException;
import cafe.shop.exception.ResourceNotFoundException;
import cafe.shop.exception.TerminalNotFoundException;
import cafe.shop.model.constant.OrderStatus;
import cafe.shop.model.dto.*;
import cafe.shop.model.entities.*;
import cafe.shop.repository.OrderRepository;
import cafe.shop.repository.RecipeRepository;
import cafe.shop.repository.TerminalRepository;
import cafe.shop.repository.UserRepository;
import cafe.shop.service.BaseService;
import cafe.shop.service.OrderService;
import cafe.shop.service.QueueService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@Log4j2
public class OrderServiceImpl extends BaseService implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private QueueService queueService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Override
    public Page<OrderDto> getOrderListByCustomer(Map<String, Object> params, Pageable pageable) {
        String customerId = currentUser().getId();
        userRepository.findById(UUID.fromString(customerId)).ifPresent(customer -> params.put("customer", customer));

        Specification<Order> spec = new GenericSpecification<>(params, pageable);
        Page<Order> orderPage = orderRepository.findAll(spec, pageable);

        return orderPage.map(order -> OrderDto
                .builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderStatus(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build()
        );
    }

    @Override
    @Transactional
    public OrderDto placeOrder(OrderRequest request) {
        Terminal terminal = terminalRepository.findById(request.getTerminalId())
                .orElseThrow(() -> new TerminalNotFoundException("Terminal not found"));
        User user = userRepository.findById(UUID.fromString(currentUser().getId()))
                .orElseThrow(() -> new CustomerAlreadyExistsException("User not found"));
        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        Order order = new Order();
        order.setCustomer(user);
        order.setTerminal(terminal);
        order.setRecipe(recipe);
        order.setVolume(request.getVolume());
        order.setDestinationId(request.getDestinationId());
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.NEW);
        orderRepository.save(order);

        log.info("[processOrder: {}] - Created new order", order.getId());

        Queue queue = queueService.addOrderToQueue(order, user);
        order.setQueue(queue);
        order.setStatus(OrderStatus.PROCESS);
        orderRepository.save(order);
        log.info("[processOrder: {}] - Added order to queue {}", order.getId(), queue.getId());

        int queuePosition = queueService.getCustomerQueueSize(queue);
        int waitingTime = calculateExpectedWaitingTime(terminal);

        return OrderDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .queuePosition(queuePosition)
                .expectedWaitingTime(waitingTime)
                .build();
    }

    @Override
    public QueuePositionDto getQueuePosition(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Queue queue = order.getQueue();
        int queuePosition = queueService.getCustomerQueueSize(queue);
        int waitingTime = calculateExpectedWaitingTime(order.getTerminal());

        return new QueuePositionDto(queuePosition, waitingTime);
    }

    @Override
    public OrderDetailDto getOrderDetail(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return OrderDetailDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerName(order.getCustomer().getFirstName())
                .customerPhone(order.getCustomer().getMobileNumber())
                .status(order.getStatus())
                .recipeName(order.getRecipe().getName())
                .volume(order.getVolume())
                .destinationId(order.getDestinationId())
                .build();
    }

    @Override
    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Queue queue = order.getQueue();

        queueService.removeOrderFromQueue(queue, order);
        log.info("[processCancelOrder: {}] - Removed order in queue {}", order.getId(), queue.getId());

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        log.info("[processCancelOrder: {}] - Mark cancel order", order.getId());
    }

    private int calculateExpectedWaitingTime(Terminal terminal) {
        return 15;
    }

    private String generateOrderNumber() {
        return "O-" + RandomStringUtils.randomAlphanumeric(12).toUpperCase();
    }
}
