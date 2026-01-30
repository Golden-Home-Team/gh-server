package kr.co.goldenhome.config;

import io.sentry.SentryOptions;
import kr.co.goldenhome.ApiHistoryContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentryConfig {

    @Bean
    SentryOptions.BeforeSendCallback beforeSendCallback() {
        return (event, hint) -> {
            event.setTag("transactionId", ApiHistoryContextHolder.get().getTransactionId());
            return event;
        };
    }
}
