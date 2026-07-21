package io.quarkusdroneshop.web.infrastructure;

import io.quarkusdroneshop.web.domain.Customer360;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * dataproduct-customer-360 (bsite の customer_360 Flink ジョブが発行する顧客統合プロファイル) を
 * 独立したコンシューマグループで購読し、customerName ごとに受信したプロファイル更新の履歴を
 * オンメモリに蓄積する。History 画面はここから取得したデータをそのまま表示する。
 * Pod 再起動でリセットされるキャッシュであり永続ストアではない。
 */
@ApplicationScoped
public class Customer360HistoryStore {

    private static final int MAX_ITEMS_PER_CUSTOMER = 50;

    private final Logger logger = Logger.getLogger(Customer360HistoryStore.class);

    private final ConcurrentHashMap<String, Deque<Customer360>> historyByCustomer = new ConcurrentHashMap<>();

    @Incoming("customer-360-history")
    public void onCustomer360Update(Customer360 profile) {
        if (profile == null || profile.customerName == null) {
            return;
        }

        Deque<Customer360> history = historyByCustomer.computeIfAbsent(
                profile.customerName, k -> new ArrayDeque<>());

        synchronized (history) {
            history.addFirst(profile);
            while (history.size() > MAX_ITEMS_PER_CUSTOMER) {
                history.removeLast();
            }
        }

        logger.debugf("customer360 history recorded for %s: totalOrders=%s", profile.customerName, profile.totalOrders);
    }

    public List<Customer360> getHistory(String customerName) {
        if (customerName == null) {
            return Collections.emptyList();
        }
        Deque<Customer360> history = historyByCustomer.get(customerName);
        if (history == null) {
            return Collections.emptyList();
        }
        synchronized (history) {
            return history.stream().collect(Collectors.toList());
        }
    }
}
