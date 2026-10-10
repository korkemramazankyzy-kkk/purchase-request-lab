
package com.example.purchase.config;

import com.example.purchase.domain.PurchaseRequestPolicy;
import com.example.purchase.domain.Rule;
import com.example.purchase.domain.RuleChain;
import com.example.purchase.domain.TransitionRule;
import com.example.purchase.domain.UnapprovedCannotOrderRule;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.example.purchase")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Rule purchaseRequestRules() {
        return new RuleChain(List.of(
                new UnapprovedCannotOrderRule(),
                new TransitionRule(new PurchaseRequestPolicy())
        ));
    }
}
