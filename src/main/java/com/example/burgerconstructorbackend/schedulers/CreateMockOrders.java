package com.example.burgerconstructorbackend.schedulers;

import com.example.burgerconstructorbackend.auth.dto.RegisterRequest;
import com.example.burgerconstructorbackend.auth.service.AuthService;
import com.example.burgerconstructorbackend.ingredient.model.Ingredient;
import com.example.burgerconstructorbackend.ingredient.repository.IngredientRepository;
import com.example.burgerconstructorbackend.order.model.Order;
import com.example.burgerconstructorbackend.order.model.OrderStatus;
import com.example.burgerconstructorbackend.order.repository.OrderRepository;
import com.example.burgerconstructorbackend.order_ingredient.OrderIngredient;
import com.example.burgerconstructorbackend.order_ingredient.OrderIngredientKey;
import com.example.burgerconstructorbackend.user.entity.User;
import com.example.burgerconstructorbackend.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateMockOrders {

    final AuthService authService;
    final UserRepository userRepository;
    final OrderRepository orderRepository;
    final IngredientRepository ingredientRepository;
    final Random random = new Random();

    @Scheduled(cron = "0 0 * * * *")
    public void createOrder() {
        List<Ingredient> buns = ingredientRepository.findByType("bun");
        List<Ingredient> mains = ingredientRepository.findByType("main");
        List<Ingredient> sauces = ingredientRepository.findByType("sauce");
        String email = "bob@example.org";
        String name = "bob";
        String password = "bob123123";

        if (!userRepository.existsByEmail(email)) {
            authService.register(
                    RegisterRequest.builder()
                            .email(email)
                            .password(password)
                            .name(name)
                            .build()
            );
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow();

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setName(user.getName());

        OrderIngredient bunOi = getRandomIngredient(buns, order, 2);
        order.getOrderIngredients().add(bunOi);

        OrderIngredient mainOi = getRandomIngredient(mains, order, 1);
        order.getOrderIngredients().add(mainOi);

        OrderIngredient sauceOi = getRandomIngredient(sauces, order, 1);
        order.getOrderIngredients().add(sauceOi);

        orderRepository.save(order);
    }

    private OrderIngredient getRandomIngredient(List<Ingredient> list, Order order, int count) {
        OrderIngredient oi = new OrderIngredient();
        Ingredient ingredient = list.get(random.nextInt(list.size()));
        oi.setId(OrderIngredientKey.builder().orderId(order.getId()).ingredientId(ingredient.getId()).build());
        oi.setOrder(order);
        oi.setIngredient(ingredient);
        oi.setQuantity(count);
        return oi;
    }
}