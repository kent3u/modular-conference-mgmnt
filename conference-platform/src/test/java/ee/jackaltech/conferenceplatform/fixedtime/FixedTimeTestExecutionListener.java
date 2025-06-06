package ee.jackaltech.conferenceplatform.fixedtime;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.mockito.Mockito.when;

@Slf4j
@RequiredArgsConstructor
public class FixedTimeTestExecutionListener implements TestExecutionListener {
    private Clock fixedClock;

    @Override
    public void beforeTestClass(TestContext testContext) {
        fixedClock = testContext.getApplicationContext().getBean(Clock.class);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) {
        FixedTime fixedTime = AnnotatedElementUtils.findMergedAnnotation(testContext.getTestMethod(), FixedTime.class);
        setFixedClockValue(fixedTime);
    }

    @Override
    public void afterTestMethod(@NonNull TestContext testContext) {
        TestClock.reset(fixedClock);
    }

    private void setFixedClockValue(FixedTime fixedTime) {
        Optional.ofNullable(fixedTime)
            .map(FixedTime::value)
            .map(LocalDateTime::parse)
            .map(localDateTime -> localDateTime.atZone(ZoneId.systemDefault()).toInstant())
            .ifPresent(value -> {
                when(fixedClock.instant()).thenReturn(value);
                log.info("Set time to: {}", fixedClock.instant());
            });
    }
}
