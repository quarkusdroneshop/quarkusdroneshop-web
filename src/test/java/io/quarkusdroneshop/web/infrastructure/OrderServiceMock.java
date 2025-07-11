package io.quarkusdroneshop.web.infrastructure;

import io.quarkus.test.Mock;
import io.quarkusdroneshop.domain.valueobjects.OrderResult;
import io.quarkusdroneshop.web.domain.commands.PlaceOrderCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.concurrent.CompletableFuture;


//@Mock
@Alternative
@ApplicationScoped
public class OrderServiceMock extends OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceMock.class);

    @Override
    public CompletableFuture<OrderResult> placeOrder(PlaceOrderCommand placeOrderCommand) {
        // テスト用の簡易的な返却。nullでもいいが、必要に応じてモックデータを返す。
        return CompletableFuture.completedFuture(null); 
    }

}