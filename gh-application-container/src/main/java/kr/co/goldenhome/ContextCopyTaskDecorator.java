package kr.co.goldenhome;

import lombok.NonNull;
import org.springframework.core.task.TaskDecorator;

public class ContextCopyTaskDecorator implements TaskDecorator {

    @NonNull
    @Override
    public Runnable decorate(@NonNull Runnable runnable) {

        ApiHistoryContext context = ApiHistoryContextHolder.get();
        return () -> {
            try {
                if (context != null) {
                    ApiHistoryContextHolder.set(context);
                }
                runnable.run();
            } finally {
                ApiHistoryContextHolder.destroy();
            }
        };

    }
}
